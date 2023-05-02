package nz.ac.canterbury.seng440.geoimage.utils;

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlin.Suppress;
import nz.ac.canterbury.seng440.geoimage.data.files.impl.FileDAO
import nz.ac.canterbury.seng440.geoimage.model.*

class PhotoRepository(private val fileDAO:FileDAO,) {


    val numFile : Flow<Int> = fileDAO.getCount()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun loadPhotoByTripId(tripId: Long): Map<Route, List<File>> {
        //Log.d("D/loadPhotoByTripId",tripId?.toString())
        return fileDAO.loadPhotosByTripId(tripId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun loadLastPhotoByTripId(tripId: Long): File {
        //Log.d("D/loadPhotoByTripId",tripId?.toString())
        return fileDAO.loadLastPhotoByTripId(tripId)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertFile(file:File) {
        //Log.d("D/insertFile",file?.toString())
        fileDAO.insert(file)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateFile(file: File) {
        //Log.d("D/updateFile",file?.toString())
        fileDAO.update(file)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteFile(file: File) {
        //Log.d("D/deleteFile",file?.toString())
        fileDAO.delete(file)
    }



}