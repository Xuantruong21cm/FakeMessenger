package org.app.data.model

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails


enum class SkuState {
    SKU_STATE_UNPURCHASED, SKU_STATE_PENDING, SKU_STATE_PURCHASED, SKU_STATE_PURCHASED_AND_ACKNOWLEDGED
}

data class InAppProductModel(
    var id: String,
    var name: String,
    var description: String,
    var offerToken: String = "",
    var price: String = "",
    var purchaseTime: Long = 0,
    var billingCycle: Int = 0,
    var billingPeriod: String = "",
    var priceCurrencyCode: String = "",
    var type: String = "", //String INAPP = "inapp" (one time); SUBS = "subs" (time: weekly, monthly, yearly);
    var state: SkuState = SkuState.SKU_STATE_UNPURCHASED
)

fun InAppProductModel.fromRemoteProductDetail(product: ProductDetails) {
    if (product.subscriptionOfferDetails != null && product.subscriptionOfferDetails!!.size > 0) {
        val subscription = product.subscriptionOfferDetails!![0]
        val pricePhases = subscription.pricingPhases

        this.offerToken = subscription.offerToken
        this.type = BillingClient.ProductType.SUBS

        if (pricePhases.pricingPhaseList.size > 0) {
            val pricePhase = pricePhases.pricingPhaseList[0]

            this.price = pricePhase.formattedPrice
            this.billingCycle = pricePhase.billingCycleCount
            this.billingPeriod = pricePhase.billingPeriod
            this.priceCurrencyCode = pricePhase.priceCurrencyCode
        }
    } else if (product.oneTimePurchaseOfferDetails != null) {
        val oneTimePurchase = product.oneTimePurchaseOfferDetails!!

        this.type = BillingClient.ProductType.INAPP
        this.price = oneTimePurchase.formattedPrice
        this.priceCurrencyCode = oneTimePurchase.priceCurrencyCode
    }
}
