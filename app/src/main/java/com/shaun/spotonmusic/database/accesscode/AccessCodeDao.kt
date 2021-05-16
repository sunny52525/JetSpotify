package com.shaun.spotonmusic.database.accesscode

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AccessCodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCode(accessCodeEntity: AccessCodeEntity)

    @Query("SELECT * FROM accessCode")
    fun getAccessCode():LiveData<AccessCodeEntity>
}