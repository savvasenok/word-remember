package xyz.savvamirzoyan.wordremember.ui.state

import androidx.annotation.StringRes

sealed class TextInputState {
    object Success : TextInputState()
    class Error(@StringRes val description: Int) : TextInputState()
}
