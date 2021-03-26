package com.assessment.nytimessampleapp.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.assessment.nytimessampleapp.data.PreferencesManager.PreferencesKeys.DAY_COUNT
import com.assessment.nytimessampleapp.utils.DaysCount
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("ny_times_app")

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e("Datastore exception is ${exception.printStackTrace()}")
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            DaysCount.valueOf(
                preferences[DAY_COUNT] ?: DaysCount.ONE_DAY.name
            )
        }

    suspend fun setDayCount(daysCount: DaysCount) {
        dataStore.edit { preferences ->
            preferences[DAY_COUNT] = daysCount.name
        }
    }

    private object PreferencesKeys {
        val DAY_COUNT = preferencesKey<String>("day_count")
    }
}