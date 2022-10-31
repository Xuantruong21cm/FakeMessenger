package org.app.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import org.app.data.model.entity.User

@Dao
interface UserDao : BaseDao<User> {
    @Query("SELECT * FROM user_info")
    suspend fun getAll(): List<User>
}