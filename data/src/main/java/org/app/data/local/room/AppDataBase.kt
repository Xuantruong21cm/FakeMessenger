package org.app.data.local.room

import androidx.room.*
import org.app.data.local.room.dao.UserDao
import org.app.data.model.entity.*
import java.util.*

@Database(entities = [User::class],
  version = 1,
  exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
  abstract fun userDao(): UserDao

  //TODO: Other DAO
}

class Converters {
  @TypeConverter
  fun fromTimestamp(value: Long?): Date? {
    return value?.let { Date(it) }
  }

  @TypeConverter
  fun dateToTimestamp(date: Date?): Long? {
    return date?.time
  }
}