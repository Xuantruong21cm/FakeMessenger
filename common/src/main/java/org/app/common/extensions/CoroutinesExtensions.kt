package org.app.common.extensions

import kotlinx.coroutines.*

suspend fun <T> runMain(run: suspend () -> T) = withContext(Dispatchers.Main) { run() }

suspend fun <T> runIO(run: suspend CoroutineScope.() -> T) =
    coroutineScope { withContext(Dispatchers.IO) { run() } }

fun CoroutineScope.launchIO(block: suspend CoroutineScope.() -> Unit) =
    launch(Dispatchers.IO, block = block)

fun CoroutineScope.launchMain(block: suspend CoroutineScope.() -> Unit) =
    launch(Dispatchers.Main, block = block)