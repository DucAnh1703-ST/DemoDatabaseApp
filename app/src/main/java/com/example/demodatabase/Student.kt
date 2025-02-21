package com.example.demodatabase

import android.os.Parcel
import android.os.Parcelable

data class Student(
    val studentID: Int,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val city: String,
    val phone: String,
    val subjects: List<Subjects>
) : Parcelable {

    // Constructor to create the object from a Parcel
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(Subjects) ?: emptyList()
    )

    // Write data to the Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(studentID)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(dateOfBirth)
        parcel.writeString(city)
        parcel.writeString(phone)
        parcel.writeTypedList(subjects)
    }

    // Describe the content (used for flags, normally 0)
    override fun describeContents(): Int {
        return 0
    }

    // Companion object to create a Parcelable.Creator for Student
    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }
}
