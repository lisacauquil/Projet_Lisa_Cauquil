package com.example.myapplication.data.model

import java.io.Serializable

data class Magasin(
    val nom: String,
    val adresse: String,
    val latitude: Double,
    val longitude: Double,
    val passion: String,
    val partenaire: Boolean = false,
    val stock: List<String> = emptyList()
) : Serializable