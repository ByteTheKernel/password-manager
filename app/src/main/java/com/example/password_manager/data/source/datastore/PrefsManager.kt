package com.example.password_manager.data.source.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("user_prefs")

object PrefKeys {
    val MASTER_SET = booleanPreferencesKey("master_set")
    val TEST_ENCRYPTED = stringPreferencesKey("test_encrypted")
    val TEST_IV = stringPreferencesKey("test_iv")
}

class PrefsManager(private val context: Context) {
    suspend fun isMasterSet(): Boolean =
        context.dataStore.data.first()[PrefKeys.MASTER_SET] == true

    suspend fun setMasterSet(value: Boolean) {
        context.dataStore.edit { it[PrefKeys.MASTER_SET] = value }
    }

    suspend fun getTestEncrypted(): String? =
        context.dataStore.data.first()[PrefKeys.TEST_ENCRYPTED]

    suspend fun setTestEncrypted(value: String) {
        context.dataStore.edit { it[PrefKeys.TEST_ENCRYPTED] = value }
    }

    suspend fun getTestIv(): String? =
        context.dataStore.data.first()[PrefKeys.TEST_IV]

    suspend fun setTestIv(value: String) {
        context.dataStore.edit { it[PrefKeys.TEST_IV] = value }
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
