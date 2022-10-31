package com.merryblue.fakemessenger.ui.purchase

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.merryblue.fakemessenger.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.app.common.BaseViewModel
import org.app.common.utils.InAppPurchase
import org.app.common.utils.SingleLiveEvent
import org.app.data.model.InAppProductModel
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(application: Application) : BaseViewModel(application)  {

    var tickIcon = R.drawable.ic_purchase_tick
    var products = SingleLiveEvent<List<InAppProductModel>>()

    init {
        InAppPurchase.getInstance(application).getProducts()
            .onEach {
                Log.i("Debug", "get all product: $it")
                products.postValue(it)
            }
            .launchIn(GlobalScope)

        // Timeout 5s
        Handler(Looper.getMainLooper())
            .postDelayed({ products.postValue(listOf()) },
                5000
            )
    }
}