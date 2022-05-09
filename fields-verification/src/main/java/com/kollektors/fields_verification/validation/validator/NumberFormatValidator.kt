package com.kollektors.fields_verification.validation.validator

class NumberFormatValidator : Validator<String?> {

    companion object {
        private val DIGIT_REGEX = "\\d+".toRegex()
    }

    override suspend fun validate(item: String?): Result<String> = runCatching {
        item?.takeIf { DIGIT_REGEX.matches(it) } ?: throw NumberFormatException()
    }
}