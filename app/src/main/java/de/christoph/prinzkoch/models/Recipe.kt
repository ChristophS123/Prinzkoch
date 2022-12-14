package de.christoph.prinzkoch.models

import android.os.Parcel
import android.os.Parcelable

data class Recipe (
    val nameOfRecipe:String = "",
    val shortDescription:String = "",
    val category:String = "",
    val longDescription:String = "",
    val image:String = "",
    val creatorID:String = "",
    val creatorName:String = "",
    val creatorImage:String = "",
    var id:String = "",
    var likers:ArrayList<String> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nameOfRecipe)
        parcel.writeString(shortDescription)
        parcel.writeString(category)
        parcel.writeString(longDescription)
        parcel.writeString(image)
        parcel.writeString(creatorID)
        parcel.writeString(creatorName)
        parcel.writeString(creatorImage)
        parcel.writeString(id)
        parcel.writeStringList(likers)
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