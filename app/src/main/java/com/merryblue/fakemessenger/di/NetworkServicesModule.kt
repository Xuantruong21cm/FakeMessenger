package com.merryblue.fakemessenger.di

import com.merryblue.fakemessenger.data.general.remote.ManagerServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkServicesModule {

    @Provides
    @Singleton
    fun provideManagerServices(retrofit: Retrofit): ManagerServices =
        retrofit.create<ManagerServices>(ManagerServices::class.java)
}