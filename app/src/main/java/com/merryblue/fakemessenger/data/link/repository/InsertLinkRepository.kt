package com.merryblue.fakemessenger.data.link.repository

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Pair
import androidx.lifecycle.MutableLiveData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.gson.Gson
import com.merryblue.fakemessenger.R
import org.app.common.utils.checkNetwork
import org.app.common.utils.checkSaveFileMediaStore
import org.app.common.utils.getPath
import org.app.data.model.entity.ModelCheckLink
import org.app.data.model.entity.Video
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.logging.Logger
import javax.inject.Inject

interface InsertLinkRepository {
    val checkInternet: Boolean
    fun checkLinkFB(link: String?)

    // get progress and data file
    val percentProgress: MutableLiveData<Int>
    val scanFileVideo: MutableLiveData<Pair<String, Uri>>
    val videoLiveData: MutableLiveData<Pair<Video, ModelCheckLink>>
}

class InsertLinkRepositoryImpl @Inject constructor(
    private val context: Context
) : InsertLinkRepository {

    override val percentProgress by lazy { MutableLiveData<Int>() }
    override val scanFileVideo by lazy { MutableLiveData<Pair<String, Uri>>() }
    override val videoLiveData by lazy { MutableLiveData<Pair<Video, ModelCheckLink>>() }

    override val checkInternet: Boolean
        get() = (checkNetwork(
            context,
            context.getString(R.string.txt_yc_connection_internet)
        ) == -1)

    override fun checkLinkFB(link: String?) {
        AndroidNetworking.post("https://download-video.merryblue.llc/api/v1/download/link")
            .addBodyParameter("url", link)
            .setTag("test")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    val modelCheckLink = Gson().fromJson(
                        response.toString(),
                        ModelCheckLink::class.java
                    )
                    modelCheckLink?.let {
                        val video = Video()
                        video.title = it.title
                        it.links?.let { links ->
                            video.link = links.hd
                        }
                        video.thumb = it.thumb
                        video.linkCheck = link

                        videoLiveData.postValue(Pair(video, it))
                        downloadRequest(context, video.link ?: "")
                    } ?: run {
                        videoLiveData.postValue(Pair(null, null))
                    }
                }

                override fun onError(anError: ANError) {
                    val modelCheckLink = Gson().fromJson(
                        anError.errorBody,
                        ModelCheckLink::class.java
                    )
                    videoLiveData.postValue(Pair(null, modelCheckLink))
                }
            })
    }

    fun downloadRequest(context: Context, url: String) {
        // update
        val displayName = "FbDownloader_" + System.currentTimeMillis() + ".mp4"
        var relativeLocation = ""
        val dataSave = checkSaveFileMediaStore(context, displayName)
        dataSave?.let { data ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                data.contentValues?.let { contentValue ->
                    data.uri?.let { uriFile ->
                        contentValue.put("is_pending", false)
                        context.contentResolver.update(uriFile, contentValue, null, null)
                        data.path = getPath(context, uriFile)
                        relativeLocation = data.path ?: ""
                    }
                }
            } else {
                relativeLocation = data.path ?: ""
            }
            val format = SimpleDateFormat("HH:mm dd/MM/yyyy")
            val startDownload = format.format(System.currentTimeMillis())
//            videoModel?.timeStartDownload = startDownload
            AndroidNetworking.download(url, relativeLocation, "")
                .setTag("downloadTest")
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener { bytesDownloaded, totalBytes ->
                    val progress = (bytesDownloaded * 100 / totalBytes).toInt()
                    percentProgress.postValue(progress)
                    // do anything with progress
                    Logger.getLogger("Loading").warning("Loading..$progress")
                }
                .startDownload(object : DownloadListener {
                    override fun onDownloadComplete() {
                        scanFileVideo.postValue(Pair(data.path, data.uri))
                        Logger.getLogger("Loading").warning("Done..")
                    }

                    override fun onError(error: ANError?) {
                        scanFileVideo.postValue(null)
                    }
                })
        }
    }
}

