package com.example.cameraapp.data

import android.content.Context
import android.os.Environment
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Repository private constructor(private val db: AppDb, private val app: Context) {
    companion object {
        @Volatile private var INSTANCE: Repository? = null
        fun get(app: Context): Repository {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(app, AppDb::class.java, "session-db").build()
                Repository(db, app).also { INSTANCE = it }
            }
        }
    }

    private fun sessionDir(sessionId: String): File {
        val base = app.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File(base, "Sessions/$sessionId").apply { if (!exists()) mkdirs() }
    }

    suspend fun createSession(name: String, age: Int): String {
        val id = UUID.randomUUID().toString()
        db.sessionDao().upsert(Session(id, name, age, System.currentTimeMillis()))
        sessionDir(id) // ensure folder exists
        return id
    }

    fun observeSessions(): Flow<List<Session>> = db.sessionDao().observeAll()
    fun searchSessions(q: String): Flow<List<Session>> = db.sessionDao().search(q)
    fun photosForSession(sessionId: String): Flow<List<Photo>> = db.photoDao().photosForSession(sessionId)

    suspend fun saveCapturedImage(
        sessionId: String,
        imageCapture: ImageCapture
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
            val file = File(sessionDir(sessionId), "IMG_${sdf.format(Date())}.jpg")
            val output = ImageCapture.OutputFileOptions.Builder(file).build()
            val res = kotlinx.coroutines.suspendCancellableCoroutine<ImageCapture.OutputFileResults> { cont ->
                imageCapture.takePicture(output, Dispatchers.IO.asExecutor(),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exception: ImageCaptureException) {
                            if (cont.isActive) cont.resumeWith(Result.failure(exception))
                        }
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            if (cont.isActive) cont.resumeWith(Result.success(outputFileResults))

                        }
                    })
            }
            db.photoDao().insert(Photo(sessionId = sessionId, filePath = file.absolutePath, takenAt = System.currentTimeMillis()))
            Result.success(file)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}
