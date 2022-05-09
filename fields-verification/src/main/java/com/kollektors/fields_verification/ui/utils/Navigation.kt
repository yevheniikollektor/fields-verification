package com.kollektors.fields_verification.ui.utils

import android.app.Activity
import android.os.Bundle
import androidx.core.os.bundleOf

sealed class DestinationState

data class BackState(
    val code: Int = Activity.RESULT_CANCELED,
    val arguments: Map<String, Any?> = mapOf()
) : DestinationState()

data class RequestPermissionsState(
    val permissions: List<String>
) : DestinationState()

abstract class NextDestination : DestinationState()

fun BackState.toBundle(): Bundle {
    return bundleOf(*arguments.map { it.key to it.value }.toTypedArray())
}
