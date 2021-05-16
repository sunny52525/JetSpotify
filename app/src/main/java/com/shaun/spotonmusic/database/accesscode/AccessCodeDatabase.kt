package com.shaun.spotonmusic.database.accesscode

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [AccessCodeEntity::class],version = 2)
abstract class AccessCodeDatabase:RoomDatabase() {
    abstract fun accessCodeDao():AccessCodeDao
}