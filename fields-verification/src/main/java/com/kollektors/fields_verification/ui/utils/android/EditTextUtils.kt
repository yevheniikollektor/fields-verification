package com.kollektors.fields_verification.ui.utils.android

import android.widget.EditText

val EditText.stringValue get() = text?.toString() ?: ""