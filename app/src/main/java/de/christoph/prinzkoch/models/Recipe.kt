package de.christoph.prinzkoch.models

import android.os.Parcel
import android.os.Parcelable

data class Recipe (
    val name:String = "",
    val shortDescription:String = "",
    val category:String = "",
    val longDescription:String = "",
    val image:String = "",
    val creatorID:String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(shortDescription)
        parcel.writeString(category)
        parcel.writeString(longDescription)
        parcel.writeString(image)
        parcel.writeString(creatorID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recipe> {
        override fun createFromParcel(parcel: Parcel): Recipe {
            return Recipe(parcel)
        }

        override fun newArray(size: Int): Array<Recipe?> {
            return arrayOfNulls(size)
        }
    }
}