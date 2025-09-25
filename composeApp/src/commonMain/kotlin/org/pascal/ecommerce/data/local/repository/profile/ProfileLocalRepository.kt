package org.pascal.ecommerce.data.local.repository.profile

import org.pascal.ecommerce.data.local.entity.ProfileEntity

interface ProfileLocalRepository {
    suspend fun getProfileById(id: Long): ProfileEntity?
    suspend fun getAllProfiles(): List<ProfileEntity>
    suspend fun deleteProfileById(item: ProfileEntity)
    suspend fun insertProfile(item: ProfileEntity)
}