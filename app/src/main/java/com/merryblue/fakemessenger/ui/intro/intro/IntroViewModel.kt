package com.merryblue.fakemessenger.ui.intro.intro

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import com.merryblue.fakemessenger.data.account.repository.HomeRepository
import com.merryblue.fakemessenger.enum.IntroPage
import dagger.hilt.android.qualifiers.ApplicationContext
import org.app.common.BaseViewModel
import org.app.common.utils.SingleLiveEvent
import org.app.data.model.IntroModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(private val homeRepository: HomeRepository,
                                         @ApplicationContext private val context: Context,
                                         application: Application
) : BaseViewModel(application) {

  val currentPage = MutableLiveData<Int>()
  private val pageIndex = SingleLiveEvent<Int>()
  val pageModel = SingleLiveEvent<IntroModel>()
  val openHomeEvent = SingleLiveEvent<Void>()

  fun setFirstTime(isFirstTime: Boolean) {
    homeRepository.isFirstLaunch = isFirstTime
  }

  fun setCurrentPage(page: Int) {
    currentPage.postValue(page)
  }

  fun setPageIndex(index: Int) {
    pageIndex.postValue(index)
    pageModel.postValue(IntroPage.allPage(context)[index])
  }

  fun onNext() {
    val currentPageValue = currentPage.value ?: 0
    if (currentPageValue < (IntroPage.values().size - 1)) {
      currentPage.postValue(currentPageValue + 1)
    } else {
      openHomeEvent.call()
    }
  }

  fun onPrevious() {
    val currentPageValue = currentPage.value ?: 0
    if (currentPageValue == 0) {
      openHomeEvent.call()
    } else {
      currentPage.postValue(currentPageValue - 1)
    }
  }
}