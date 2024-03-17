package ru.eugeneprojects.passwordmanager.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "table_password")
data class Password(
    @PrimaryKey(autoGenerate = true)
    val passwordId: Int,
    val passwordSiteName: String,
    val passwordSiteUrl: String,
    val password: String,
): Parcelable {

}
