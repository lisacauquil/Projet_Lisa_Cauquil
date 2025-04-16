package com.example.myapplication.data.model

data class Magasin(
    val nom: String,
    val adresse: String,
    val latitude: Double,
    val longitude: Double,
    val passion: String,
    val partenaire: Boolean,
    val stock: List<String>? = null
)
