package com.example.demodatabase

import android.os.Parcel
import android.os.Parcelable

data class Subjects(
    val subjectID: Int,
    val studentID: Int,
    val name: String,
    val score: Int
) : Parcelable {

    // Constructor to create the object from a Parcel
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt()
    )

    // Write data to the Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(subjectID)
        parcel.writeInt(studentID)
        parcel.writeString(name)
        parcel.writeInt(score)
    }

    // Describe the content (used for flags, normally 0)
    override fun describeContents(): Int {
        return 0
    }

    // Companion object to create a Parcelable.Creator for Subjects
    companion object CREATOR : Parcelable.Creator<Subjects> {
        override fun createFromParcel(parcel: Parcel): Subjects {
            return Subjects(parcel)
        }

        override fun newArray(size: Int): Array<Subjects?> {
            return arrayOfNulls(size)
        }
    }
}

