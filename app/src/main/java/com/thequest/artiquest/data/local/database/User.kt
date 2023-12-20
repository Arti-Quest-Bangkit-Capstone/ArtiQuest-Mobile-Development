package com.thequest.artiquest.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    val uid: String,
    var username: String,
    var displayName: String,
    var phoneNumber: String,
    var profilePictureUrl: String?
)
