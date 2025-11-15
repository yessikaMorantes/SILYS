package com.silys.utils

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.R
import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference

class UIUtils {
    private var rootViewRef: WeakReference<View>? = null

    fun init(rootView: View) {
        rootViewRef = WeakReference(rootView)
    }
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

        fun View.showSnackBar(message: String) {
            val snackbar = Snackbar.make(this, "", Snackbar.LENGTH_SHORT)
            val snackbarLayout = snackbar.view as ViewGroup
            val textView = snackbarLayout.findViewById<TextView>(
                R.id.snackbar_text
            )
            textView.visibility = View.INVISIBLE
            snackbarLayout.setBackgroundColor(Color.TRANSPARENT)
            val custom = LayoutInflater.from(this.context)
                .inflate(com.silys.R.layout.snackbar, null)
            val txtMensaje = custom.findViewById<TextView>(com.silys.R.id.txtMensaje)
            txtMensaje.text = message
            snackbarLayout.addView(custom, 0)
            val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(
                params.leftMargin,
                params.topMargin,
                params.rightMargin,
                500
            )
            snackbar.show()
        }
    }
}