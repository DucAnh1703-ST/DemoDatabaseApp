package com.example.demodatabase.model

data class Student(
    val studentID: Int,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val city: String,
    val phone: String,
    val subjects: List<Subjects>
)
