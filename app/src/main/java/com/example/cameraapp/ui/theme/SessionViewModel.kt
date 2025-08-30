package com.example.cameraapp.ui.theme

import android.app.Application
import androidx.camera.core.ImageCapture
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameraapp.data.Repository
import com.example.cameraapp.data.Session
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SessionViewModel(app: Application): AndroidViewModel(app) {
    private val repo = Repository.get(app)
    val sessions: StateFlow<List<Session>> = repo.observeSessions().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun createSession(name: String, age: Int, onCreated: (String)->Unit) = viewModelScope.launch {
        val id = repo.createSession(name, age)
        onCreated(id)
    }
    fun search(q: String) = repo.searchSessions(q)
    fun saveImage(sessionId: String, imageCapture: ImageCapture) = viewModelScope.launch {
        val r = repo.saveCapturedImage(sessionId, imageCapture)
        _message.value = r.fold({ "Saved: ${it.name}" }, { "Error: ${it.message}" })
    }
}
