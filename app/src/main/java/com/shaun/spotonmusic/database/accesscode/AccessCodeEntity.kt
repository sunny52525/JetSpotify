package com.shaun.spotonmusic.database.accesscode

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accessCode")
data class AccessCodeEntity(

    var code: String,
    @PrimaryKey var id: Int
)