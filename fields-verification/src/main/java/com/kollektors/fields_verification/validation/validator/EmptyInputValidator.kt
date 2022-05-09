package com.kollektors.fields_verification.validation.validator

import com.kollektors.fields_verification.validation.exception.EmptyFieldException

class EmptyInputValidator : Validator<String?> {

    override suspend fun validate(item: String?): Result<String> = runCatching {
        item.takeUnless { it.isNullOrBlank() } ?: throw EmptyFieldException()
    }
}