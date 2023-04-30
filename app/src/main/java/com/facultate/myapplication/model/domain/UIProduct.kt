package com.facultate.myapplication.model.domain

import android.os.Parcel
import android.os.Parcelable

open class UIProduct(
    open var product: Product,
    open var isFavorite: Boolean,
    open var isDeal: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable<Product>(Product::class.java.classLoader)!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(product, flags)
        parcel.writeByte(if (isFavorite) 1 else 0)
        parcel.writeByte(if (isDeal) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UIProduct> {
        override fun createFromParcel(parcel: Parcel): UIProduct {
            return UIProduct(parcel)
        }

        override fun newArray(size: Int): Array<UIProduct?> {
            return arrayOfNulls(size)
        }
    }
}
