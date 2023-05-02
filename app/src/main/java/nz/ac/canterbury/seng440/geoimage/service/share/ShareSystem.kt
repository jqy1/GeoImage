package nz.ac.canterbury.seng440.geoimage.service.share

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import nz.ac.canterbury.seng440.geoimage.R
import java.io.File


class ShareSystem(authority_name: String, context: Context):BaseIntent(authority_name,context) {
    override val packageName: String? = null

    override fun execute(function: String?, arguments: Map<String, String>):String {
        return when (function) {
            "shareText" -> {
                shareText(arguments["text"], arguments["type"]!!, arguments["prompt"])
            }
            "shareFile" -> {
                shareFile(arguments["file_url"], arguments["type"]!!, arguments["prompt"])
            }
            "shareImage" -> {
                shareImage(arguments["image_url"], arguments["type"]!!, arguments["prompt"])
            }
            else -> {
                "Error"
            }
        }
    }

    private fun shareText(text:String?, mime:String, prompt: String?,) :String{
        var result = ""
        if (text.isNullOrEmpty()) {
            val exceptionMessage = "Non-empty text expected"
            val exception = IllegalArgumentException(exceptionMessage)
            result = "Non-empty text expected ${exception.message}"
            throw exception
        }

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = mime
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)

        val chooserIntent = Intent.createChooser(shareIntent, prompt)
        runActivityForResult(chooserIntent, R.string.app_ui_share_text)
        return result
    }

    private fun shareFile(file:String?, mime:String, prompt: String?, ) :String{
        var result = ""
        if (file.isNullOrEmpty()) {
            val exceptionMessage = "Non-empty local file path expected"
            val exception = IllegalArgumentException(exceptionMessage)
            result = "IllegalArgumentException ${exception.message}"
            throw exception
        }

        val contentFile = File(context.cacheDir, file)
        val contentUri = FileProvider.getUriForFile(context, authority_name, contentFile)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = mime
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)

        val chooserIntent = Intent.createChooser(shareIntent, prompt)
        runActivityForResult(chooserIntent, R.string.app_ui_share_file)
        return result
    }

    private fun shareImage(image:String?, mime:String, prompt: String?,):String {
        var result = ""
        if (image.isNullOrEmpty()) {
            val exceptionMessage = "Non-empty local image path expected"
            val exception = IllegalArgumentException(exceptionMessage)
            result = "IllegalArgumentException ${exception.message}"
            throw exception
        }

        val imageFile = File(context.cacheDir, image)
        val contentUri = FileProvider.getUriForFile(context, authority_name, imageFile)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = mime
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)

        val chooserIntent = Intent.createChooser(shareIntent, prompt)
        runActivityForResult(chooserIntent, R.string.app_ui_share_image)

        return result
    }
}