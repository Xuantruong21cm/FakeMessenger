package org.app.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.app.data.local.AppPreferences
import org.app.data.local.room.AppDataBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context) = AppPreferences(context)

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context,
        AppDataBase::class.java,
        "appdb")
        .build()

    @Singleton
    @Provides
    fun provideUserDao(db: AppDataBase) = db.userDao()
}