package xyz.savvamirzoyan.wordremember.extension

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

fun CoroutineScope.flowListen(
    function: suspend () -> Unit,
    lifecycleOwner: LifecycleOwner
): CoroutineScope {
    Timber.i("flowListen()")

    launch { lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) { function() } }

    return this
}