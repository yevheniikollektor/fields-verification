package com.kollektors.fields_verification.validation

import com.kollektors.fields_verification.validation.exception.ValidationException
import com.kollektors.fields_verification.validation.validator.Validator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class FieldHolderImpl<T : Any> constructor(
    private val validators: List<Validator<T?>>
) : FieldHolder<T> {

    private val _value: MutableSharedFlow<T?> = MutableSharedFlow(replay = 1)

    var value: T?
        set(value) {
            _value.tryEmit(value)
        }
        get() = _value.replayCache.firstOrNull()

    override val state: Flow<Result<T?>> = _value.distinctUntilChanged().map { curr ->

        for (validator in validators) {
            val result = validator.validate(curr)
            if (result.isFailure) return@map result
        }
        return@map Result.success(curr)
    }

    @Throws(ValidationException::class)
    suspend fun validateItSelf(): Result<T?> {
        val curr = value
        for (validator in validators) {
            val result = validator.validate(curr)
            if (result.isFailure) return result
        }
        return Result.success(curr)
    }
}