package com.kollektors.fields_verification.ui.utils

import android.widget.Filter

object EmptyFilter: Filter() {

    override fun performFiltering(
        constraint: CharSequence?
    ): FilterResults? = null

    override fun publishResults(
        constraint: CharSequence?,
        results: FilterResults?
    ) = Unit
}