package com.kollektors.fields_verification.validation

import kotlinx.coroutines.flow.Flow

interface FieldHolder<T> {

    val state: Flow<Result<T?>>
}