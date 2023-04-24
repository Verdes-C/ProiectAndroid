package com.facultate.myapplication.hilt

import com.facultate.myapplication.redux.ApplicationState
import com.facultate.myapplication.redux.Store
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationStoreModule {

    @Provides
    @Singleton
    fun providesApplicationStateStore(): Store<ApplicationState>{
        return Store(ApplicationState())
    }
}