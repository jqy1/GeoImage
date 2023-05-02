package nz.ac.canterbury.seng440.geoimage.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class StorePreference(private val context: Context) {

    //to make sure there is only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Setting")
        val GPS_METHOD_KEY = stringPreferencesKey("gps_method")
        val MAP_TYPE_KEY = stringPreferencesKey("map_type")
        val LANGUAGE_SETTING_KEY = stringPreferencesKey("language_setting")
    }

    //to get the GPS Method
    val getGPSMethod: Flow<String?> = context.dataStore.data
        .map{preferences ->
            preferences[GPS_METHOD_KEY] ?: ""
        }

    //to save the GPS Method
    suspend fun saveGPSMethod(gps_method: String){
        context.dataStore.edit { preferences ->
            preferences[GPS_METHOD_KEY] = gps_method
        }
    }

    //to get the map Type
    val getMapType: Flow<String?> = context.dataStore.data
        .map{preferences ->
            preferences[MAP_TYPE_KEY] ?: ""
        }

    //to save the map Type
    suspend fun saveMapType(mapType: String){
        context.dataStore.edit { preferences ->
            preferences[MAP_TYPE_KEY] = mapType
        }
    }

    //to get the language Setting
    val getLanguageSetting: Flow<String?> = context.dataStore.data
        .map{preferences ->
            preferences[LANGUAGE_SETTING_KEY] ?: ""
        }

    //to save the language Setting
    suspend fun saveLanguageSetting(languageSetting: String){
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_SETTING_KEY] = languageSetting
        }
    }

}