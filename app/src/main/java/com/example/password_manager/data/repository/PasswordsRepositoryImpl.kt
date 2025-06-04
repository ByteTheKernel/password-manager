package com.example.password_manager.data.repository

import android.util.Base64
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.password_manager.data.source.local.PasswordDao
import com.example.password_manager.data.model.PasswordEntity
import com.example.password_manager.data.model.PasswordPreviewEntity
import com.example.password_manager.data.source.keystore.KeyStoreUtils
import com.example.password_manager.data.source.local.toDomain
import com.example.password_manager.domain.model.Password
import com.example.password_manager.domain.model.PasswordPreview
import com.example.password_manager.domain.repository.PasswordsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PasswordsRepositoryImpl(
    private val dao: PasswordDao
) : PasswordsRepository {

    override fun getPasswordPreviewsPaging(query: String): Flow<PagingData<PasswordPreview>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { dao.pagingSource(query) }
        ).flow
         .map { pagingData -> pagingData.map { it.toDomain() } }
    }

    override fun getAllPasswordPreviews(): Flow<List<PasswordPreview>> =
        dao.getAllPreviews().map { list -> list.map { it.toDomain() } }

    override fun searchPasswordPreviews(query: String): Flow<List<PasswordPreview>> =
        dao.searchPreviewsByTitle(query).map { list -> list.map { it.toDomain() } }

    override suspend fun getPasswordById(id: Long): Password? {
        val entity = dao.getById(id) ?: return null
        val (ivString, encryptedString) = entity.encryptedPassword.split(":")
        val decrypted = KeyStoreUtils.decrypt(
            Base64.decode(encryptedString, Base64.NO_WRAP),
            Base64.decode(ivString, Base64.NO_WRAP)
        )
        return entity.toDomain(String(decrypted))
    }

    override suspend fun addPassword(password: Password) = withContext(Dispatchers.IO) {
        val (encrypted, iv) = KeyStoreUtils.encrypt(password.password.toByteArray())
        val encryptedPassword = "${Base64.encodeToString(iv, Base64.NO_WRAP)}:${Base64.encodeToString(encrypted, Base64.NO_WRAP)}"
        dao.insertPassword(
            PasswordEntity(
                id = password.id,
                title = password.title,
                login = password.login,
                encryptedPassword = encryptedPassword,
                url = password.url,
                description = password.description
            )
        )
    }

    override suspend fun deletePassword(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteById(id)
    }
}
