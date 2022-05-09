package com.kollektors.fields_verification.validation.exception

class ShortInputException(val currentLength: Int, val requiredLength: Int) : ValidationException()