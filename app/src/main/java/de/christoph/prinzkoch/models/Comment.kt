package de.christoph.prinzkoch.models

import android.os.Parcel
import android.os.Parcelable

data class Comment(
    val createdBy:String = "",
    val createdByName:String = "",
    val createdByImage:String = "",
    val recipeComment:String = "",
    val message:String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(createdBy)
        parcel.writeString(createdByName)
        parcel.writeString(createdByImage)
        parcel.writeString(recipeComment)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Comment> {
        override fun createFromParcel(parcel: Parcel): Comment {
            return Comment(parcel)
        }

        override fun newArray(size: Int): Array<Comment?> {
            return arrayOfNulls(size)
        }
    }
}