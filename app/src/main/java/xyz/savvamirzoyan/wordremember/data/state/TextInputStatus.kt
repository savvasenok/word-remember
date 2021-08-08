package xyz.savvamirzoyan.wordremember.data.state

import androidx.annotation.StringRes

sealed class TextInputStatus {
    object Success : TextInputStatus()
    class Error(@StringRes val description: Int) : TextInputStatus()
}
