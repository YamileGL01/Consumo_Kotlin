package com.example.pruebas.Models

data class User (
    val gender: String,
    val name: Name,
    val picture: Picture,
)
data class Name (
    val title: String,
    val first: String,
    val last: String
)
data class Picture (
    val large: String,
    val medium: String,
    val thumbnail: String
)

data class ResultUser(
    val results: List<User>
)