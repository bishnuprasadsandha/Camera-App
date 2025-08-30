package com.example.cameraapp.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import com.example.cameraapp.data.Repository

@Composable
fun AppNav(vm: SessionViewModel) {
    val nav = rememberNavController()
    NavHost(nav, "home") {
        composable("home") { HomeScreen(vm) { nav.navigate("capture/$it") } }
        composable("capture/{sid}") { backStack ->
            val sid = backStack.arguments?.getString("sid")!!
            CaptureScreen(vm, sid) { nav.navigate("details/$sid") }
        }
        composable("details/{sid}") { backStack ->
            val sid = backStack.arguments?.getString("sid")!!
            SessionDetailScreen(sid)
        }
    }
}

@Composable
fun HomeScreen(vm: SessionViewModel, onStart: (String)->Unit) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    val sessions by vm.sessions.collectAsStateWithLifecycle()

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("New Session", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(name, { name = it }, label = { Text("Name") })
        OutlinedTextField(age, { age = it.filter { ch -> ch.isDigit() } }, label = { Text("Age") })
        Button(enabled = name.isNotBlank() && age.isNotBlank(), onClick = {
            vm.createSession(name.trim(), age.toInt(), onStart)
        }) { Text("Start & Capture") }

        Divider(Modifier.padding(vertical = 8.dp))
        Text("Search Sessions", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(query, { query = it }, label = { Text("Search by Name/ID") })
        val results by vm.search(query).collectAsState(initial = sessions)
        results.take(10).forEach {
            ListItem(
                headlineContent = { Text("${it.name} (Age ${it.age})") },
                supportingContent = { Text("ID: ${it.sessionId}") },
                trailingContent = {
                    TextButton(onClick = { onStart(it.sessionId) }) { Text("Open") }
                }
            )
        }
    }
}

@Composable
fun CaptureScreen(vm: SessionViewModel, sessionId: String, onDone: ()->Unit) {
    val imageCapture = rememberImageCapture()
    val msg by vm.message.collectAsStateWithLifecycle()
    Scaffold(
        bottomBar = {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = { vm.saveImage(sessionId, imageCapture) }) { Text("Capture") }
                OutlinedButton(onClick = onDone) { Text("Finish") }
            }
        }
    ) { paddings ->
        Box(Modifier.fillMaxSize().padding(paddings)) {
            CameraPreview(Modifier.fillMaxSize(), imageCapture)
            if (msg != null) Snackbar(Modifier.padding(16.dp)) { Text(msg!!) }
        }
    }
}

@Composable
fun SessionDetailScreen(sessionId: String) {
    val ctx = LocalContext.current
    val repo = remember { Repository.get(ctx) }
    val photos by repo.photosForSession(sessionId).collectAsState(initial = emptyList())
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("Session: $sessionId", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))
        LazyVerticalGrid(columns = GridCells.Adaptive(128.dp), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(photos.size) { i ->
                AsyncImage(model = "file://${photos[i].filePath}", contentDescription = null, modifier = Modifier.height(140.dp))
            }
        }
    }
}
