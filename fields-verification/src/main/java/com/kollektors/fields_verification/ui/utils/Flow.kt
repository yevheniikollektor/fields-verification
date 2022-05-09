
package com.kollektors.fields_verification.ui.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Suppress("NAME_SHADOWING")
inline fun <T> Flow<T>.asObserverJob(
    lifecycleOwner: LifecycleOwner,
    rootJob: Job? = null,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend (T) -> Unit
): Job {
    val scope = lifecycleOwner.lifecycleScope
    val block: suspend (CoroutineScope.() -> Unit) = {
        lifecycleOwner.repeatOnLifecycle(state) {
            collect(block)
        }
    }
    return when (rootJob) {
        null -> scope.launch(block = block)
        else -> scope.launch(rootJob, block = block)
    }
}