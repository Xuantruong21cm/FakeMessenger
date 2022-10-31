package org.app.common.extensions

import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus

fun Any.toJsonString(): String = Gson().toJson(this)

fun <A : Any> String.toJsonModel(modelClass: Class<A>): A = Gson().fromJson(this, modelClass)

fun Any.registerEventBusBy(needToSubscribe: Boolean = true) {
    if (!EventBus.getDefault().isRegistered(this) && needToSubscribe) {
        EventBus.getDefault().register(this)
    }
}

fun Any.unRegisterEventBus() {
    if (EventBus.getDefault().isRegistered(this)) {
        EventBus.getDefault().unregister(this)
    }
}