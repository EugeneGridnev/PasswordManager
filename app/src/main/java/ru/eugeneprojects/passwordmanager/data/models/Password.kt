package ru.eugeneprojects.passwordmanager.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_password")
data class Password(
    @PrimaryKey(autoGenerate = true)
    val passwordId: Int,
    val passwordSiteName: String,
    val passwordSiteUrl: String,
    val password: String,
) {

}
