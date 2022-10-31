package org.app.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Create by LuyenPhong on 10/27/2022
 * phone 0358844343
 */
@Entity(tableName = "table_video")
data class Video(
    @ColumnInfo(name = "title")
    var title: String? = "",

    @ColumnInfo(name = "thumb")
    var thumb: String? = "",

    @ColumnInfo(name = "link")
    var link: String?= "",

    @ColumnInfo(name = "description")
    var description: String?= "",

    @ColumnInfo(name = "duration")
    var duration: String?= "",

    @ColumnInfo(name = "path")
    var path: String?= "",

    @ColumnInfo(name = "progress")
    var progress: Int? = 0,

    @ColumnInfo(name = "timeStartDownload")
    var timeStartDownload: String? = "",

    @ColumnInfo(name = "linkCheck")
    var linkCheck: String? = "",

    @ColumnInfo(name = "file_uri")
    var uri: String?= ""
) {
    @PrimaryKey(autoGenerate = true)
     var idVideo: Int = 0
}

data class ModelCheckLink(
    @SerializedName("success") @Expose
     var aBoolean: Boolean = false,
    @SerializedName("title")
    @Expose
     val title: String? = null,
    @SerializedName("links")
    @Expose val links: Links? = null,
    @SerializedName("thumb")
    @Expose
     val thumb: String? = null,
    @SerializedName("message")
    @Expose
     var message: String? = null
)

data class Links(
    @SerializedName("sd")
    @Expose
    var sd: String? = null,

    @SerializedName("hd")
    @Expose
    var hd: String? = null
)

