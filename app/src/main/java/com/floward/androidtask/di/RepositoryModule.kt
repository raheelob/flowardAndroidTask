package com.floward.androidtask.di;

import com.floward.androidtask.data.repository.UserRepository
import com.floward.androidtask.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}
