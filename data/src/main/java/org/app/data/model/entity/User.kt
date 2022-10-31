package org.app.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class User(
  @ColumnInfo(name = "image")
  val imagePath: String,

  @ColumnInfo(name = "username")
  val username: String,

  @ColumnInfo(name = "fullname")
  val fullname: String,

  @ColumnInfo(name = "email")
  val email: String,

  @ColumnInfo(name = "pwd")
  val password: String,

  @ColumnInfo(name = "refresh_token")
  val refreshToken: String,

  @ColumnInfo(name = "access_token")
  val token: String,

  @ColumnInfo(name = "token_expire")
  val tokenExpirationDate: String,

  @ColumnInfo(name = "user_id")
  val userId: String,

  @ColumnInfo(name = "current_active")
  val isCurrentActive: Boolean
) {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  var id: Int? = null

}