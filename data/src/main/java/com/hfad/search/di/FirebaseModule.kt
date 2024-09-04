package com.hfad.search.di

import com.hfad.search.firebase.FirebaseRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule{
    @Singleton
    fun provideFirebaseRepository(): FirebaseRepository {
        return FirebaseRepository()
    }
}