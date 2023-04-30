package com.facultate.myapplication.model.domain

import java.math.BigDecimal
import javax.inject.Inject

import android.os.Parcel
import android.os.Parcelable

class Product(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    var price: Double,
    val title: String
) : Parcelable {
    init {
        this.price = Math.ceil(String.format("%.2f",price).toDouble()*100)/100
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(description)
        parcel.writeInt(id)
        parcel.writeString(image)
        parcel.writeDouble(price)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
