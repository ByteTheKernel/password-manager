package com.example.password_manager.data.repository

import android.util.Base64
import com.example.password_manager.data.source.datastore.PrefsManager
import com.example.password_manager.data.source.keystore.KeyStoreUtils
import com.example.password_manager.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val prefs: PrefsManager
) : AuthRepository {

    override suspend fun isMasterSet(): Boolean = prefs.isMasterSet()

    override suspend fun setMaster(password: String) {
        KeyStoreUtils.createSecretKeyIfNeeded()

        val checkString = "test_value:$password".toByteArray()
        val (encrypted, iv) = KeyStoreUtils.encrypt(checkString)
        prefs.setTestEncrypted(Base64.encodeToString(encrypted, Base64.NO_WRAP))
        prefs.setTestIv(Base64.encodeToString(iv, Base64.NO_WRAP))
        prefs.setMasterSet(true)
    }

    override suspend fun authenticate(password: String): Boolean {
        val encrypted = prefs.getTestEncrypted()
        val iv = prefs.getTestIv()
        if (encrypted == null || iv == null) return false
        val decrypted = KeyStoreUtils.decrypt(
            Base64.decode(encrypted, Base64.NO_WRAP),
            Base64.decode(iv, Base64.NO_WRAP)
        )
        val str = String(decrypted)
        return str == "test_value:$password"
    }
}
