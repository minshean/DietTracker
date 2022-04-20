package com.diettracker.models

data class ProfileModel(
    val name: String,
    val dateOfBirth: String,
    val gender: String,
    val weight: Double,
    val height: Double,
    val weightChanges: Map<String, Double> = emptyMap()
){
    constructor(): this("","", "", 0.0, 0.0)
}