package org.app.common

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.*
import kotlinx.coroutines.*
import org.app.common.utils.SingleLiveEvent

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

  var dataLoadingEvent: SingleLiveEvent<Int> = SingleLiveEvent()

  private var job: Job? = null

  private val _errorMsg: MutableLiveData<String?> = SingleLiveEvent()
  val errorMsg: LiveData<String?>
    get() = _errorMsg

  private val _progress: MutableLiveData<Boolean> = SingleLiveEvent()
  val progress: LiveData<Boolean>
    get() = _progress

  open fun launchDataLoad(block: suspend () -> Unit): Job {
    return viewModelScope.launch {
      try {
        _progress.value = true
        withContext(Dispatchers.IO) {
          block()
        }
      } catch (error: Throwable) {
        _errorMsg.value = error.message
      } finally {
        _progress.value = false
      }
    }
  }

  open fun launchDataLoadWithoutProgress(block: suspend () -> Unit): Job {
    return viewModelScope.launch(Dispatchers.IO) {
      try {
        block()
      } catch (error: Throwable) {
        _errorMsg.postValue(error.message)
      }
    }
  }

  private val _isErrorView = SingleLiveEvent<Boolean>()
  val isErrorView: LiveData<Boolean>
    get() = _isErrorView

  fun enableErrorView(isError: Boolean){
    _isErrorView.postValue(isError)
  }

  open fun <T> debounceLiveData(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    duration: Long = 500L,
    block: () -> LiveData<T>
  ): LiveData<T> {
    job?.cancel()
    job = Job()
    return liveData(CoroutineScope(job!! + coroutineDispatcher).coroutineContext) {
      delay(duration)
      emitSource(block.invoke())
    }
  }

  open fun debounce(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    duration: Long = 500L,
    block: suspend () -> Unit
  ): Job {
    job?.cancel()
    job = Job()
    return viewModelScope.launch(CoroutineScope(job!! + coroutineDispatcher).coroutineContext) {
      delay(duration)
      block.invoke()
    }
  }

  private val _refreshComplete = MutableLiveData(false)
  val refreshComplete: LiveData<Boolean>
    get() = _refreshComplete
  private val _loadMoreComplete = MutableLiveData(false)
  val loadMoreComplete: LiveData<Boolean>
    get() = _loadMoreComplete
  private val _isNoMore = MutableLiveData(false)
  val isNoMore: LiveData<Boolean>
    get() = _isNoMore

  fun enableNoMore(enable: Boolean) {
    _isNoMore.postValue(enable)
  }

  fun isInternetConnected(): Boolean {
    val cm = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    return cm?.activeNetworkInfo != null && cm.activeNetworkInfo?.isConnected == true
  }

  open fun onRefresh(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: (suspend () -> Unit)?
  ): Job {
    _refreshComplete.value = false
    _isNoMore.value = false
    return viewModelScope.launch(coroutineDispatcher) {
      try {
        block?.invoke()
      } catch (e: Exception) {
        e.printStackTrace()
      } finally {
        _refreshComplete.postValue(true)
      }
    }
  }

  open fun onLoadMore(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: (suspend () -> Unit)?
  ): Job {
    _loadMoreComplete.value = false
    return viewModelScope.launch(coroutineDispatcher) {
      try {
        block?.invoke()
      } catch (e: Exception) {
        e.printStackTrace()
      } finally {
        _loadMoreComplete.postValue(true)
      }
    }
  }

  fun stopRefresh() {
    _refreshComplete.postValue(true)
  }

  fun stopLoadMore() {
    _loadMoreComplete.postValue(true)
  }
}