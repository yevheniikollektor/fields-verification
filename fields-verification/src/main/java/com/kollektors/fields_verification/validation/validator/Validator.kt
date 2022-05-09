package com.kollektors.fields_verification.validation.validator

fun interface Validator<T> {

    suspend fun validate(item: T?): Result<T>
}