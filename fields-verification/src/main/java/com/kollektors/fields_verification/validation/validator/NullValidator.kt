package com.kollektors.fields_verification.validation.validator

import com.kollektors.fields_verification.validation.exception.EmptyFieldException

class NullValidator<T> : Validator<T> {

    override suspend fun validate(item: T?): Result<T> = runCatching {
        item ?: throw EmptyFieldException()
    }
}