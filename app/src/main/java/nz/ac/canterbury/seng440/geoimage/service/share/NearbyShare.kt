package nz.ac.canterbury.seng440.geoimage.service.share

import android.bluetooth.BluetoothAdapter
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.collection.SimpleArrayMap
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import nz.ac.canterbury.seng440.geoimage.BuildConfig
import nz.ac.canterbury.seng440.geoimage.ui.component.MyNotification
import nz.ac.canterbury.seng440.geoimage.ui.component.makeToast
import nz.ac.canterbury.seng440.geoimage.utils.TripViewModel
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*


class NearbyShare(viewModel: TripViewModel, authority_name: String, context: Context):BaseIntent(authority_name,context) {
    override val packageName: String? = null
    final val SELECT_IMAGE = 100
    final val ENDPOINT_EXTRA : String = BuildConfig.APPLICATION_ID.plus("endpoint")


    override fun initializeIntent(){

    }

    override fun execute(function: String?, arguments: Map<String, String>):String {
        return ""
    }

    fun SendPhotos(endPoints:List<String>,photoList:List<nz.ac.canterbury.seng440.geoimage.model.File>){

        for ( f in photoList){
            val file = File(f.path)
            val payload = Payload.fromFile(file)
            if (endPoints?.size!! > 0) {

                // Construct a simple message mapping the ID of the file payload to the desired filename.

                // Construct a simple message mapping the ID of the file payload to the desired filename.
                val filenameMessage: String = payload.id.toString() + ":" + file.name

                // Send the filename message as a bytes payload.

                // Send the filename message as a bytes payload.
                val filenameBytesPayload =
                    Payload.fromBytes(filenameMessage.toByteArray(StandardCharsets.UTF_8))
                Nearby.getConnectionsClient(context).sendPayload(endPoints, filenameBytesPayload)

                // Finally, send the file payload.
                Nearby.getConnectionsClient(context).sendPayload(endPoints!!, payload)
            }
        }

    }

    /**
     *  Core function that need to start when program start.
     * */
    fun startAdvertising(){
        val advertisingOptions : AdvertisingOptions = AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_STAR)
            .build();
        if(BluetoothAdapter.getDefaultAdapter() == null){
            Toast.makeText(context, "Bluetooth is required", Toast.LENGTH_SHORT).show()

        }else {
            if(BluetoothAdapter.getDefaultAdapter().isEnabled){
                Nearby.getConnectionsClient(context).stopAdvertising()
                val endpoint : String = android.os.Build.MANUFACTURER + "-" + UUID.randomUUID().toString()
                Nearby.getConnectionsClient(context)
                    .startAdvertising(endpoint, BuildConfig.APPLICATION_ID, mConnectionLifeCycleCallback , advertisingOptions)
                    .addOnCompleteListener { a ->
                    }.addOnSuccessListener { a ->
                        Toast.makeText(context, "Started Advertising", Toast.LENGTH_SHORT).show()
                        Log.d("Nearby ","Started Advertising")
                    }.addOnFailureListener { a ->
                        if(a is ApiException){
                            a.statusMessage?.let { Log.d("ApiException ", it) }
                            Log.d("ApiException ","".plus(a.statusCode))
                        }
                        a.printStackTrace()
                        Log.d("Nearby ","Failed Advertising")

                    }
            }else{
                Toast.makeText(context, "Turn on bluetooth and wifi", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun stopAdvertising(){
        Nearby.getConnectionsClient(context).stopAdvertising()
    }

    /*
    * Start Discorvery of Nearby function,
    * */
    fun startDiscovery(){
        val discoveryOptions : DiscoveryOptions = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_STAR).build()
        if(BluetoothAdapter.getDefaultAdapter() == null){
            Toast.makeText(context, "Bluetooth is required", Toast.LENGTH_SHORT).show()

        }else {
            if(BluetoothAdapter.getDefaultAdapter().isEnabled){
                Nearby.getConnectionsClient(context).stopDiscovery()
                Nearby.getConnectionsClient(context)
                    .startDiscovery(BuildConfig.APPLICATION_ID,mDiscoveryCallback,discoveryOptions)
                    .addOnCompleteListener { a ->
                    }.addOnSuccessListener { a ->
                        Log.d("Nearby ","Started Discovering")
                        Toast.makeText(context, "Started Discovering", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { a ->
                        a.printStackTrace()
                        Log.d("Nearby ","Failed Discovering")

                    }
            }else{
                Toast.makeText(context, "Turn on bluetooth and wifi", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun stopDiscover(){
        Nearby.getConnectionsClient(context).stopDiscovery()
    }
    /**
     *  Discovery call back
     * */
    private val mDiscoveryCallback : EndpointDiscoveryCallback = object : EndpointDiscoveryCallback(){
        override fun onEndpointFound(p0: String, p1: DiscoveredEndpointInfo) {
            Log.d("onEndpointFound", "End point -> ${p0}")
            Log.d("onEndpointFound", "End point Name -> ${p1.endpointName}")
            Log.d("onEndpointFound", "Service Id -> ${p1.serviceId}")
            requestionConnection(p0)
        }

        override fun onEndpointLost(p0: String) {
            Log.d("onEndpointLost", "End point -> ${p0}")
        }
    }

    /**
     * Connection accepted call back function
     * */
    private val mConnectionLifeCycleCallback : ConnectionLifecycleCallback = object : ConnectionLifecycleCallback(){
        override fun onConnectionResult(p0: String, p1: ConnectionResolution) {
            Log.d("onConnectionResult", "End point -> ${p0}")
            Log.d("onConnectionResult", "End point status -> ${p1.status}")
        }

        override fun onDisconnected(p0: String) {
            Log.d("onDisconnected", "End point -> ${p0}")

        }

        override fun onConnectionInitiated(p0: String, p1: ConnectionInfo) {
            Log.d("onConnectionInitiated", "End point -> ${p0}")
            establishConnection(p0,p1)
            viewModel.onAddEndPoint(p0)
        }

    }

    private fun requestionConnection(endPointId: String){
        val deviceName = android.os.Build.MANUFACTURER
        Nearby.getConnectionsClient(context).requestConnection(deviceName,endPointId,mConnectionLifeCycleCallback)
    }

    /** Copies a stream from one location to another.  */
    private fun copyStream(inputStream: InputStream, outStream: OutputStream) {
        try {
            val buffer = ByteArray(1024)
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                outStream.write(buffer, 0, read)
            }
            outStream.flush()
        } finally {
            inputStream.close()
            inputStream.close()
        }
    }

    private val mPayloadCallback : PayloadCallback = object  :  PayloadCallback(){
        val incomingFilePayloads = SimpleArrayMap<Long, Payload>()
        val completedFilePayloads = SimpleArrayMap<Long, Payload>()
        val filePayloadFilenames = SimpleArrayMap<Long, String>()
        val totalfile = 0

        private fun copyUriToUri(from: Uri, to: Uri) {
            context.contentResolver.openInputStream(from).use { input ->
                context.contentResolver.openOutputStream(to).use { output ->
                    input?.let {
                        if (output != null) {
                            it.copyTo(output)
                        }
                    }
                }
            }
        }

        private fun processFilePayload(payloadId: Long) {
            // BYTES and FILE could be received in any order, so we call when either the BYTES or the FILE
            // payload is completely received. The file payload is considered complete only when both have
            // been received.
            val filePayload = completedFilePayloads[payloadId]
            val filename = filePayloadFilenames[payloadId]
            if (filePayload != null && filename != null) {
                completedFilePayloads.remove(payloadId)
                filePayloadFilenames.remove(payloadId)

                // Get the received file (which will be in the Downloads folder)
                // Because of https://developer.android.com/preview/privacy/scoped-storage, we are not
                // allowed to access filepaths from another process directly. Instead, we must open the
                // uri using our ContentResolver.
                val uri = filePayload.asFile()!!.asUri()
                try {
                    // Copy the file to a new location.
                    val inStream = context.contentResolver.openInputStream(uri!!)
                    copyStream(inStream!!, FileOutputStream(File(context.cacheDir, filename)))
                    Log.d("processFilePayload", "End point -> ${filename}")
                } catch (e: IOException) {
                    // Log the error.
                } finally {
                    // Delete the original file.
                    context.contentResolver.delete(uri!!, null, null)
                }
            }
        }

        // Checks if a volume containing external storage is available
        // for read and write.
        fun isExternalStorageWritable(): Boolean {
            return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        }

        // Checks if a volume containing external storage is available to at least read.
        fun isExternalStorageReadable(): Boolean {
            return Environment.getExternalStorageState() in
                    setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
        }

        // add removed tag back to fix b/183037922
        private fun processFilePayload2(payloadId: Long) {
            // BYTES and FILE could be received in any order, so we call when either the BYTES or the FILE
            // payload is completely received. The file payload is considered complete only when both have
            // been received.
            val filePayload = completedFilePayloads[payloadId]
            val filename = filePayloadFilenames[payloadId]
            if (filePayload != null && filename != null) {
                completedFilePayloads.remove(payloadId)
                filePayloadFilenames.remove(payloadId)

                // Get the received file (which will be in the Downloads folder)
                if (VERSION.SDK_INT >= VERSION_CODES.Q) {
                    // Because of https://developer.android.com/preview/privacy/scoped-storage, we are not
                    // allowed to access filepaths from another process directly. Instead, we must open the
                    // uri using our ContentResolver.
                    val uri = filePayload.asFile()!!.asUri()
                    try {

                        val resolver = context.contentResolver
                        val contentValues = ContentValues()
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,filename)
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        contentValues.put(
                            MediaStore.MediaColumns.RELATIVE_PATH,
                            Environment.DIRECTORY_PICTURES + File.separator.toString()
                        )
                        // Copy the file to a new location.
                        val inputStream = context.contentResolver.openInputStream(uri!!)
                        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)?.also{
                            context.contentResolver.openOutputStream(it).use {outputStream->
                                if (inputStream != null) {
                                    if (outputStream != null) {
                                        inputStream.copyTo(outputStream)
                                    }
                                }
                            }
                        }
                        sendNotification("GeoImage Nearby Sharing","File $filename transfering succeed!")
                        // Copy the file to a new location.
                        //val inputStream = context.contentResolver.openInputStream(uri!!)
                        //copyStream(inputStream!!, FileOutputStream(File(context.cacheDir, filename)))
                    } catch (e: IOException) {
                        // Log the error.
                    } finally {
                        // Delete the original file.
                        context.contentResolver.delete(uri!!, null, null)
                    }
                } else {
                    val payloadFile = filePayload.asFile()!!.asJavaFile()
                    // Rename the file.
                    payloadFile!!.renameTo(File(payloadFile.parentFile, filename))

                }
            }
        }

        private fun sendNotification(title:String, msg:String){
            val notish = MyNotification(context,  title, msg)
            notish.TestNotification()
        }

        /**
         * Extracts the payloadId and filename from the message and stores it in the
         * filePayloadFilenames map. The format is payloadId:filename.
         */
        private fun addPayloadFilename(payloadFilenameMessage: String): Long {
            val parts = payloadFilenameMessage.split(":").toTypedArray()
            val payloadId = parts[0].toLong()
            val filename = parts[1]
            filePayloadFilenames.put(payloadId, filename)
            return payloadId
        }

        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            if (payload.type == Payload.Type.BYTES) {
                val payloadFilenameMessage = String(payload.asBytes()!!, StandardCharsets.UTF_8)
                val payloadId = addPayloadFilename(payloadFilenameMessage)
                processFilePayload(payloadId)
            } else if (payload.type == Payload.Type.FILE) {
                // Add this to our tracking map, so that we can retrieve the payload later.
                incomingFilePayloads.put(payload.id, payload)
            }
        }

        fun onPayloadReceived1(p0: String, p1: Payload) {

            if (p1.getType() === Payload.Type.BYTES) {
                val payloadFilenameMessage = p1.asBytes()
                    ?.let { String(it, StandardCharsets.UTF_8) }
                val payloadId = payloadFilenameMessage?.let { addPayloadFilename(it) }
                if (payloadId != null) {
                    processFilePayload(payloadId)
                }
            } else if (p1.getType() === Payload.Type.FILE) {
                // Add this to our tracking map, so that we can retrieve the payload later.
                incomingFilePayloads.put(p1.getId(), p1)
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            Log.d("onPayloadTransferUpdate", "End point -> ${endpointId}")
            Log.d("onPayloadTransferUpdate", "End point Total bytes Update -> ${update.totalBytes}")
            if (update.status === PayloadTransferUpdate.Status.SUCCESS) {
                val payloadId: Long = update.getPayloadId()
                val payload = incomingFilePayloads.remove(payloadId)
                completedFilePayloads.put(payloadId, payload)

                if (payload!=null && payload.type == Payload.Type.FILE) {
                    processFilePayload2(payloadId)
                }
            }
        }

    }

    private fun establishConnection(endPointId: String, p1: ConnectionInfo) {
        Nearby.getConnectionsClient(context).acceptConnection(endPointId,mPayloadCallback)
    }

}