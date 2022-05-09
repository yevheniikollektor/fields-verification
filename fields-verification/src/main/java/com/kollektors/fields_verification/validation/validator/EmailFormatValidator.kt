package com.kollektors.fields_verification.validation.validator

import android.util.Patterns

class EmailFormatValidator : Validator<String?> {

    companion object {
        private val EMAIL_REGEX = Patterns.EMAIL_ADDRESS
    }

    override suspend fun validate(item: String?): Result<String> = runCatching {
        item?.takeIf { EMAIL_REGEX.matcher(it).matches() } ?: throw NumberFormatException()
    }
}