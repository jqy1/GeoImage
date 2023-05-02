package nz.ac.canterbury.seng440.geoimage.service.share

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


abstract class BaseIntent(val authority_name: String, val context: Context) {

    private val authority_suffix = ".nz.ac.canterbury.seng440.geoimage"
    private val authorityName = context.packageName + authority_suffix

    abstract val packageName:String?

    fun isInstalled(): Boolean {
        if(packageName == null) return true
        val packageManager = context.packageManager
        return try {
            packageManager.getApplicationInfo(packageName!!, 0).enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    open fun initializeIntent(){}

    protected fun runActivity(intent: Intent, activity: Activity? = null) {
        ContextCompat.startActivity(context, intent, null)
    }

    protected fun runActivityForResult(
        intent: Intent,
        requestCode: Int,
        activity: Activity? = null
    ) {
        ContextCompat.startActivity(context, intent,  null)
    }

    abstract fun execute(function: String?, arguments: Map<String, String>,):String
}