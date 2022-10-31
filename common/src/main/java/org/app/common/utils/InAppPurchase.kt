package org.app.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import com.android.billingclient.api.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.app.data.model.InAppProductModel
import org.app.data.model.SkuState
import org.app.data.model.fromRemoteProductDetail
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@SuppressLint("LogNotTimber")
class InAppPurchase private constructor(
    application: Application,
    subscriptionIds: Array<String>?,
    inAppIds: Array<String>?
) : BillingClientStateListener {
    private val defaultScope = GlobalScope
    private val billingClient: BillingClient

    private val subscriptionIds: List<String>
    private val inAppIds: List<String>

    private val productDetailsMap: MutableMap<String, MutableStateFlow<ProductDetails?>> = HashMap()
    private val productModelMap: MutableMap<String, InAppProductModel> = HashMap()

    private var onPurchaseComplete: ((Boolean) -> Unit)? = null
    private var purchasingId: String = ""

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        Log.i(TAG, "onBillingSetupFinished: $responseCode $debugMessage")
        when (responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                defaultScope.launch {
                    querySkuDetailsAsync()
                    restorePurchases()
                }
            }
        }
    }

    private fun addSkuFlows(ids: List<String>?) {
        if (null == ids) {
            Log.e(TAG, "addSkuFlows: ids list is either null or empty.")
        } else {
            for (id in ids) {
                val details = MutableStateFlow<ProductDetails?>(null)
                details.subscriptionCount.map { count ->
                    count > 0
                }
                    .distinctUntilChanged() // only react to true<->false changes
                    .onEach { isActive -> // configure an action
                        if (isActive) {
                            querySkuDetailsAsync()
                        }
                    }
                    .launchIn(defaultScope)

                productDetailsMap[id] = details
            }
        }
    }

    private suspend fun querySkuDetailsAsync() {
        if (subscriptionIds.isNotEmpty()) {
            val products = subscriptionIds.map { id ->
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(id)
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build() }

            val params = QueryProductDetailsParams
                .newBuilder()
                .setProductList(products)
                .build()
            val skuDetailsResult = withContext(Dispatchers.IO) {
                billingClient.queryProductDetails(params)
            }

            onSkuDetailsResponse(skuDetailsResult.billingResult, skuDetailsResult.productDetailsList)
        }

        if (inAppIds.isNotEmpty()) {
            val products = inAppIds.map { id ->
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(id)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build() }

            val params = QueryProductDetailsParams
                .newBuilder()
                .setProductList(products)
                .build()
            val skuDetailsResult = withContext(Dispatchers.IO) {
                billingClient.queryProductDetails(params)
            }

            onSkuDetailsResponse(skuDetailsResult.billingResult, skuDetailsResult.productDetailsList)
        }
    }

    private suspend fun restorePurchases() {
        val purchaseList = mutableListOf<Purchase>()

        // Query purchase list for subscription type
        val subParams = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
        val subPurchasesResult = billingClient.queryPurchasesAsync(subParams.build())
        if (subPurchasesResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            purchaseList.addAll(subPurchasesResult.purchasesList)
        }

        // Query purchase list for in-app one time type
        val inAppParams = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
        val inAppPurchasesResult = billingClient.queryPurchasesAsync(inAppParams.build())
        if (inAppPurchasesResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            purchaseList.addAll(inAppPurchasesResult.purchasesList)
        }

        handlePurchase(purchaseList)
    }

    private fun onSkuDetailsResponse(billingResult: BillingResult, products: List<ProductDetails>?) {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        when (responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                Log.i(TAG, "onSkuDetailsResponse: $responseCode $debugMessage")
                if (products == null || products.isEmpty()) {
                    Log.e(TAG, "onSkuDetailsResponse: Found null or empty SkuDetails. ")
                } else {
                    for (product in products) {
                        var productModel = productModelMap[product.productId]
                        if (productModel == null) {
                            productModel = InAppProductModel(product.productId, product.name, product.description)
                        }
                        productModel.fromRemoteProductDetail(product)
                        productModelMap[product.productId] = productModel
                        val detailsMutableFlow = productDetailsMap[product.productId]
                        detailsMutableFlow?.tryEmit(product) ?: Log.e(TAG, "Unknown sku: ${product.productId}")
                    }
                }
            }
        }
    }

    fun launchBillingFlow(activity: Activity,
                          id: String,
                          onComplete: (Boolean) -> Unit) {
        onPurchaseComplete = onComplete
        purchasingId = id
        val productDetails = productDetailsMap[id]?.value
        if (null != productDetails) {
            val offers = productDetails.subscriptionOfferDetails
            val offerToken = if (offers.isNullOrEmpty()) "" else offers[0].offerToken
            val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(offerToken)
                    .build()
            )

            val flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()

            billingClient.launchBillingFlow(activity, flowParams)
        } else {
            Log.e(TAG, "productDetail not found for: $id")
        }
    }

    private fun handlePurchase(purchases: List<Purchase>?) {
        if (null != purchases && purchases.isNotEmpty()) {
            for (purchase in purchases) {
                val purchaseState = purchase.purchaseState
                if (purchaseState == Purchase.PurchaseState.PURCHASED) {
                    if (!isSignatureValid(purchase)) {
                        Log.e(TAG, "Invalid signature. Check to make sure your public key is correct.")
                        continue
                    }
                    setSkuStateFromPurchase(purchase)

                    if (!purchase.isAcknowledged) {
                        defaultScope.launch {
                            for (product in purchase.products) {
                                // Acknowledge item and change its state
                                val billingResult = billingClient.acknowledgePurchase(
                                    AcknowledgePurchaseParams.newBuilder()
                                        .setPurchaseToken(purchase.purchaseToken)
                                        .build()
                                )
                                if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                                    Log.e(TAG, "Error acknowledging purchase: ${purchase.products}")
                                } else {
                                    // purchase acknowledged
                                    productModelMap[product]?.state = SkuState.SKU_STATE_PURCHASED_AND_ACKNOWLEDGED
                                    Log.i(TAG, "update state: $productModelMap")
                                }
                            }
                        }
                    }
                }  else {
                    // Not purchased
                    setSkuStateFromPurchase(purchase)
                }
            }
        } else {
            Log.i(TAG, "Empty purchase list.")
            subscriptionIds.forEach { id ->
                productModelMap[id]?.state = SkuState.SKU_STATE_UNPURCHASED
            }
        }
    }

    private fun setSkuStateFromPurchase(purchase: Purchase) {
        if (purchase.products.isEmpty()) {
            Log.e(TAG, "Empty list of skus")
            return
        }

        for (product in purchase.products) {
            var purchaseResult = false
            when (purchase.purchaseState) {
                Purchase.PurchaseState.PENDING -> productModelMap[product]?.state = SkuState.SKU_STATE_PENDING
                Purchase.PurchaseState.UNSPECIFIED_STATE -> productModelMap[product]?.state = SkuState.SKU_STATE_UNPURCHASED
                Purchase.PurchaseState.PURCHASED -> {
                    if (purchasingId == product) {
                        purchaseResult = true
                    }
                    if (purchase.isAcknowledged) {
                        productModelMap[product]?.state = SkuState.SKU_STATE_PURCHASED_AND_ACKNOWLEDGED
                    } else {
                        productModelMap[product]?.state = SkuState.SKU_STATE_PURCHASED
                    }
                    productModelMap[product]?.purchaseTime = purchase.purchaseTime
                }
                else -> Log.e(TAG, "Purchase in unknown state: " + purchase.purchaseState)
            }

            if (purchasingId == product) {
                purchasingId = ""
                onPurchaseComplete?.invoke(purchaseResult)
            }
        }

        Log.i(TAG, "setSkuStateFromPurchase update state: $productModelMap")
    }

    fun isPurchased(productId: String): Boolean {
        val state = productModelMap[productId]?.state
        return state == SkuState.SKU_STATE_PURCHASED_AND_ACKNOWLEDGED
    }

    fun isPurchaseAdsRemove(): Boolean {
        val filter = productModelMap.values.filter {
            it.state == SkuState.SKU_STATE_PURCHASED || it.state == SkuState.SKU_STATE_PURCHASED_AND_ACKNOWLEDGED
        }

        return filter.isNotEmpty()
    }

    private fun isSignatureValid(purchase: Purchase): Boolean {
        if (TextUtils.isEmpty(purchase.originalJson) || TextUtils.isEmpty(PUBLIC_KEY)
            || TextUtils.isEmpty(purchase.signature)) {
            Log.i(TAG, "Purchase verification failed: missing data.")
            return false
        }
        return try {
            val key = generatePublicKey(PUBLIC_KEY)
            verify(key, purchase.originalJson, purchase.signature)
        } catch (e: IOException) {
            Log.e(TAG, "Error generating PublicKey from encoded key: " + e.message)
            false
        }
    }

    @Throws(IOException::class)
    private fun generatePublicKey(encodedPublicKey: String): PublicKey {
        return try {
            val decodedKey = Base64.decode(encodedPublicKey, Base64.DEFAULT)
            val keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM)
            keyFactory.generatePublic(X509EncodedKeySpec(decodedKey))
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeySpecException) {
            val msg = "Invalid key specification: $e"
            Log.i(TAG, msg)
            throw IOException(msg)
        }
    }

    private fun verify(publicKey: PublicKey, signedData: String, signature: String?): Boolean {
        val signatureBytes: ByteArray = try {
            Base64.decode(signature, Base64.DEFAULT)
        } catch (e: IllegalArgumentException) {
            Log.i(TAG, "Base64 decoding failed.")
            return false
        }
        try {
            val signatureAlgorithm = Signature.getInstance(SIGNATURE_ALGORITHM)
            signatureAlgorithm.initVerify(publicKey)
            signatureAlgorithm.update(signedData.toByteArray())
            if (!signatureAlgorithm.verify(signatureBytes)) {
                Log.i(TAG, "Signature verification failed...")
                return false
            }
            return true
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            Log.e(TAG, "Invalid key specification.")
        } catch (e: SignatureException) {
            Log.e(TAG, "Signature exception.")
        }
        return false
    }

    fun getSkuTitle(id: String): Flow<String> {
        val skuDetailsFlow = productDetailsMap[id]!!
        return skuDetailsFlow.mapNotNull { skuDetails ->
            skuDetails?.title
        }
    }

    fun getProducts() : Flow<List<InAppProductModel>> = flow {
        querySkuDetailsAsync()
        restorePurchases()

        val result = productModelMap.map { it.value }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun onBillingServiceDisconnected() {
        Log.i(TAG, "Service disconnected")
        billingClient.startConnection(this)
    }

    init {
        this.subscriptionIds = if (subscriptionIds == null) {
            ArrayList()
        } else {
            listOf(*subscriptionIds)
        }

        this.inAppIds = if (inAppIds == null) {
            ArrayList()
        } else {
            listOf(*inAppIds)
        }

        //Add flow for in app purchases
        val ids = this.subscriptionIds + this.inAppIds
        addSkuFlows(ids)

        //Connecting the billing client
        billingClient = BillingClient.newBuilder(application)
            .setListener { result, purchases ->
                Log.i(TAG, "onPurchasesUpdated: ${result.responseCode} - ${purchases?.size}")
                when (result.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        if (null != purchases) {
                            handlePurchase(purchases)
                        } else Log.i(TAG, "Null Purchase List Returned from OK response!")
                    }
                    BillingClient.BillingResponseCode.USER_CANCELED -> Log.i(TAG, "onPurchasesUpdated: User canceled the purchase")
                    BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> Log.i(TAG, "onPurchasesUpdated: The user already owns this item")
                    BillingClient.BillingResponseCode.DEVELOPER_ERROR -> Log.e(TAG, "onPurchasesUpdated: Does not recognize the configuration")
                    else -> Log.i(TAG, "BillingResult [" + result.responseCode + "]: " + result.debugMessage)
                }
            }
            .enablePendingPurchases()
            .build()
        billingClient.startConnection(this)
    }

    companion object {
        private val TAG = InAppPurchase::class.java.simpleName

        const val PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwXELWf46yrUsotLeG+JP4TMnY9L1BH6I5m5D0WQhjC6pbHNZQtKyX8wID9AKYY5fgbS+FvWjUU7BrDlAYQ3QeuRdp/n+0Dv4LuTetF0vJrB9MuuZqpo0V5N6tI+xuLf46L7PphwKAMD6T660zuEtS7RSykW+vgM+LFxUQ+o915PE3f+PwN4uODkd1ATl8u5H0hU2GL/zXWNjbgiyEUjESj1qoc+IpChEzhBWxo9yfw6OWlGtItSepCrIZYLLm92zKufIOXUPDSpaugmx7FZhYO55YpT4GTPP8IJDi6ZOsWqDbaDvtSt2DCuuyQ5JigYIPu4zNNk+Cqhn1iYHLBeEVwIDAQAB"
        private const val KEY_FACTORY_ALGORITHM = "RSA"
        private const val SIGNATURE_ALGORITHM = "SHA1withRSA"

        // Define product list
        const val IN_APP_PROD_all = "dc3iqnlife"
        const val IN_APP_PROD_6m = "6monthremoveads"
        const val IN_APP_PROD_3m = "3monthremoveads"
        const val IN_APP_PROD_1m = "1monthremoveads"

        @Volatile
        private var sharedInstance: InAppPurchase? = null

        @JvmStatic
        fun initialize(application: Application) {
            if (sharedInstance == null) {
                sharedInstance = InAppPurchase(application,
                    arrayOf(IN_APP_PROD_6m,
                        IN_APP_PROD_3m,
                        IN_APP_PROD_1m),
                    arrayOf(
                        IN_APP_PROD_all
                    ))
            }
        }

        @JvmStatic
        fun getInstance(application: Application) = sharedInstance ?: synchronized(this) {
            sharedInstance ?: InAppPurchase(
                application,
                arrayOf(IN_APP_PROD_6m,
                    IN_APP_PROD_3m,
                    IN_APP_PROD_1m),
                arrayOf(
                    IN_APP_PROD_all
                )
            ).also { sharedInstance = it }
        }
    }
}
