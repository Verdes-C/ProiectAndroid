package com.facultate.myapplication.model.domain

data class UserData(
    var name:String,
    var email:String,
    var userID:String,
    var phone: String,
    var adress: String,
    var cartItems: HashMap<String,Int>,
    var wishlistedProducts: ArrayList<String>,
    var interests: ArrayList<String>,
    var joiningReason:String
){

    fun toHashMap():HashMap<String,Any>{
        return hashMapOf(
            "name" to name,
            "email" to email,
            "userID" to userID,
            "phone" to phone,
            "adress" to adress,
            "cartItems" to cartItems,
            "wishlistedProducts" to wishlistedProducts,
            "interests" to interests,
            "joiningReason" to joiningReason
        )
    }

}
