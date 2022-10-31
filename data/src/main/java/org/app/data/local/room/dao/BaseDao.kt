package org.app.data.local.room.dao

import androidx.room.*

interface BaseDao<T> {

  @Insert
  suspend fun insert(item: T)

  @Insert
  fun insertAll(vararg items: T)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insertIgnore(item: T)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAllReplace(items: List<T>)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insertAllIgnore(vararg items: T)

  @Update
  fun update(item: T)

  @Update
  fun updateAll(items: List<T>)

  @Delete
  suspend fun delete(item: T)

  @Delete
  fun deleteAll(vararg items: T)
}