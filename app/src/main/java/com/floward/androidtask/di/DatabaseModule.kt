package com.floward.androidtask.di

import android.content.Context
import com.floward.androidtask.data.local.UserDao
import com.floward.androidtask.data.local.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): UserDatabase {
        return UserDatabase.getInstance(context)
    }

    @Provides
    fun providePlantDao(appDatabase: UserDatabase): UserDao {
        return appDatabase.UserDao()
    }

}
