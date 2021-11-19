package xyz.savvamirzoyan.wordremember.ui.state

import android.view.View
import androidx.annotation.StringRes

data class DataInputState(
    val isEnabled: Boolean,
    @StringRes val error: Int?,
    @StringRes val helperText: Int,
    val visibility: Int = View.VISIBLE
)
