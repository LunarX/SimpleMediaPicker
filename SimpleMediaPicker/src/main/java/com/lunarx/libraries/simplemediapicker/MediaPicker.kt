package com.lunarx.libraries.simplemediapicker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class MediaPicker(private val activity: ComponentActivity, onMediaSelected: (uris: MutableList<Uri>) -> Unit) {

    private val selectFilesResultLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            extractUris(result).takeIf(MutableList<Uri>::isNotEmpty)?.let { onMediaSelected(it) }
        }

    private fun extractUris(activityResult: ActivityResult): MutableList<Uri> {
        val uris = mutableListOf<Uri>()

        activityResult.whenResultIsOk { data ->
            val clipData = data?.clipData
            val uri = data?.data

            try {
                if (clipData != null) {
                    val count = clipData.itemCount
                    for (i in 0 until count) {
                        uris += clipData.getItemAt(i).uri
                    }
                } else if (uri != null) {
                    uris += uri
                }
            } catch (exception: Exception) {
                Toast.makeText(activity, "Error device storage", Toast.LENGTH_LONG).show()
            }
        }

        return uris
    }

    private fun ActivityResult.whenResultIsOk(callback: (data: Intent?) -> Unit) {
        if (resultCode == Activity.RESULT_OK) callback.invoke(data)
    }

    fun chooseMedia(fileType: FileType = FileType.ALL) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = fileType.type
            flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, fileType.canSelectMoreThanOne)
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        val chooserIntent = Intent.createChooser(intent, "Media picker")
        selectFilesResultLauncher.launch(chooserIntent)
    }

    enum class FileType(val type: String, val canSelectMoreThanOne: Boolean) {
        IMAGE("image/*", false),
        VIDEO("video/*", false),
        IMAGES("image/*", true),
        VIDEOS("video/*", true),
        ALL("*/*", true),
    }
}