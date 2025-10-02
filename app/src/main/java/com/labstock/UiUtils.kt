package com.labstock

import android.view.View
import android.view.ViewGroup
import android.widget.EditText

class UiUtils {
    companion object {
        fun animateAddView(view: View, duration: Long = 300) {
            view.alpha = 0f
            view.translationY = 50f
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(duration)
                .start()
        }

        fun animateRemoveView(
            container: ViewGroup,
            view: View,
            editTextList: MutableList<EditText>,
            editText: EditText,
            duration: Long = 300
        ) {
            view.animate()
                .alpha(0f)
                .translationY(50f)
                .setDuration(duration)
                .withEndAction {
                    container.removeView(view)
                    editTextList.remove(editText)
                    container.requestFocus()
                }
                .start()
        }
    }
}