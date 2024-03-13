package ru.eugeneprojects.passwordmanager.models

data class Password(
    val id: Int,
    val passwordSiteName: String,
    val passwordSiteUrl: String,
    val password: String,
) {

}
