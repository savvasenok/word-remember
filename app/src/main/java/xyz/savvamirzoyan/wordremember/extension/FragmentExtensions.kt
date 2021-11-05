package xyz.savvamirzoyan.wordremember.extension

import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.snackbar(
    @StringRes textRes: Int,
    @StringRes actionText: Int,
    length: Int = Snackbar.LENGTH_LONG,
    action: ((View) -> Unit)? = null
) {
    Snackbar.make(requireView(), textRes, length).apply {
        action?.let { setAction(actionText, it) }
        show()
    }
}

fun Fragment.snackbar(
    text: String,
    actionText: String? = null,
    length: Int = Snackbar.LENGTH_LONG,
    action: ((View) -> Unit)? = null
) {
    Snackbar.make(requireView(), text, length).apply {
        action?.let { setAction(actionText, it) }
        show()
    }
}