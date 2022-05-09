package com.kollektors.fields_verification.validation.validator

import com.kollektors.fields_verification.validation.exception.ShortInputException

class MinLengthValidator(private val length: Int): Validator<String?> {

    override suspend fun validate(item: String?): Result<String> = runCatching {
        item?.takeIf { it.length >= length } ?: throw ShortInputException(item?.length ?: 0, length)
    }
}