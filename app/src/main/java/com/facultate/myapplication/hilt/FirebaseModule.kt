package com.facultate.myapplication.hilt

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {


    @Provides
    @Singleton
    fun providesAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    @UsersDB
    fun providesUsersDB(): CollectionReference {
        return FirebaseFirestore.getInstance()
            .collection("Users")
    }


    @Provides
    @Singleton
    @DealsDB
    fun providesDealsDB(): CollectionReference {
        return FirebaseFirestore.getInstance()
            .collection("Deals")
    }

    @Provides
    @Singleton
    fun providesImageStorageRef(): StorageReference {
        return Firebase.storage.reference
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersDB

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersDBWithQueryStringString

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DealsDB
