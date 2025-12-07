package com.silys.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
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
            spinnerList: MutableList<Spinner>,
            spinner: Spinner,
            duration: Long = 300
        ) {
            view.animate()
                .alpha(0f)
                .translationY(50f)
                .setDuration(duration)
                .withEndAction {
                    container.removeView(view)
                    spinnerList.remove(spinner)
                    container.requestFocus()
                }
                .start()
        }

        fun View.showSnackBar(message: String) {
            if(message.isBlank()){
                return
            }
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
        private var overlayView: View? = null
        fun View.showLoading() {

            val activity = context as? Activity ?: return

            if (overlayView == null) {
                val root = activity.findViewById<ViewGroup>(android.R.id.content)

                val view = LayoutInflater.from(context)
                    .inflate(com.silys.R.layout.loading_overlay, root, false)
                val lottie = view.findViewById<LottieAnimationView>(com.silys.R.id.lottieViewLoading)
                lottie.playAnimation()

                view.alpha = 0f
                root.addView(view)

                overlayView = view
            }

            overlayView?.apply {
                isVisible = true
                animate().alpha(1f).setDuration(180).start()
            }

        }

        fun View.hideLoading() {
            overlayView?.animate()
                ?.alpha(0f)
                ?.setDuration(150)
                ?.withEndAction {
                    overlayView?.isVisible = false
                }
                ?.start()
        }

    }
}