package com.merryblue.fakemessenger.di

import android.content.Context
import com.merryblue.fakemessenger.data.account.repository.HomeRepository
import com.merryblue.fakemessenger.data.account.repository.HomeRepositoryImpl
import com.merryblue.fakemessenger.data.browser.repository.BrowserRepository
import com.merryblue.fakemessenger.data.browser.repository.BrowserRepositoryImpl
import com.merryblue.fakemessenger.data.general.remote.ManagerRemoteDataSource
import com.merryblue.fakemessenger.data.general.repository.ManagerRepository
import com.merryblue.fakemessenger.data.general.repository.ManagerRepositoryImpl
import com.merryblue.fakemessenger.data.link.repository.InsertLinkRepository
import com.merryblue.fakemessenger.data.link.repository.InsertLinkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.app.data.local.AppPreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideBrowserRepository(
        @ApplicationContext context: Context
    ): BrowserRepository = BrowserRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideManagerRepository(
        @ApplicationContext context: Context,
        remoteDataSource: ManagerRemoteDataSource,
    ): ManagerRepository = ManagerRepositoryImpl(context, remoteDataSource)

    @Singleton
    @Provides
    fun provideHomeRepository(
        appPreferences: AppPreferences
    ): HomeRepository = HomeRepositoryImpl(appPreferences)

    @Provides
    fun provideInsertLinkRepository(
        @ApplicationContext context: Context
    ): InsertLinkRepository = InsertLinkRepositoryImpl(context)

}