package com.kollektors.fields_verification.ui.utils

import android.text.TextWatcher
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.google.android.material.textfield.TextInputLayout
import com.kollektors.fields_verification.R
import com.kollektors.fields_verification.validation.FieldHolder
import com.kollektors.fields_verification.validation.FieldHolderImpl
import com.kollektors.fields_verification.validation.exception.EmptyFieldException
import com.kollektors.fields_verification.validation.exception.ShortInputException
import com.kollektors.fields_verification.validation.validator.Validator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

fun List<FieldHolderImpl<*>>.isSuccess(): Flow<Boolean> {
    return combine(map { it.isSuccess }) { results -> results.all { it } }
}

fun <T : Any> holderOf(vararg validator: Validator<T?>): FieldHolderImpl<T> {
    return FieldHolderImpl(validator.asList())
}

val <T> FieldHolder<T>.isSuccess
    get() = state.map { it.isSuccess }

val <T> FieldHolder<T>.error
    get() = state.map { it.exceptionOrNull() }

fun FieldHolderImpl<String>.watch(editText: EditText): TextWatcher {
    return editText.doAfterTextChanged {
        value = it?.toString()
    }
}


inline fun FieldHolderImpl<String>.watch(
    textInputLayout: TextInputLayout,
    owner: LifecycleOwner,
    crossinline block: (Throwable, TextInputLayout) -> CharSequence = ::onErrorHandled
): InputWatcher {
    val job = error.asObserverJob(owner) {
        textInputLayout.error = it?.let { block(it, textInputLayout) }
    }
    val watcher = textInputLayout.editText?.let { watch(it) }
    return InputWatcher(textInputLayout, job, watcher)
}

fun onErrorHandled(
    error: Throwable,
    layout: TextInputLayout
): CharSequence {
    val context = layout.context
    return when (error) {
        is EmptyFieldException -> context.getString(R.string.error_empty_field, layout.hint ?: "")
        is ShortInputException -> context.getString(
            R.string.error_length_too_short,
            error.requiredLength
        )
        else -> context.getString(R.string.error_iregula_field)
    }
}

class WatchersHandler<VB : ViewBinding>(
    private val block: WatchersHandler<VB>.(VB) -> Unit
) : DefaultLifecycleObserver {

    private val watchers: MutableList<InputWatcher> = mutableListOf()

    var viewBinding: VB? = null

//    fun FieldHolderImpl<String>.register(textInputLayout: TextInputLayout, owner: LifecycleOwner) {
//        watchers.add(watch(textInputLayout, owner))
//    }

    override fun onCreate(owner: LifecycleOwner) {
        viewBinding?.let { this.block(it) }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        watchers.onEach { it.unsubscribe() }.clear()
        viewBinding = null
    }
}

class InputWatcher(
    private val textInputLayout: TextInputLayout,
    private val errorEmissionJob: Job,
    private val textWatcher: TextWatcher?
) {

    fun unsubscribeFromErrors() {
        errorEmissionJob.cancel()
    }

    fun unsubscribe() {
        unsubscribeFromErrors()
        textInputLayout.editText?.removeTextChangedListener(textWatcher)
    }
}

