package com.example.firebasedemo

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class Helper {

    companion object {
        fun isConnected(ctx: Context): Boolean {
            val connectivityManager =
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = connectivityManager.activeNetworkInfo
            return if (ni != null && ni.isAvailable && ni.isConnected) {
                true
            } else {
                false
            }
        }

        fun showSnackBar(view: View?, text: CharSequence?) {
            try {
                if (view != null) {
                    val snackbar = Snackbar.make(view, text!!, Snackbar.LENGTH_INDEFINITE)
                    val sbView = snackbar.view
                    val textView = sbView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
                    textView.setTextColor(Color.parseColor("#FFFFFF"))
                    textView.maxLines = 5
                    val handler = Handler()
                    handler.postDelayed({ snackbar.dismiss() }, 4000)
                    snackbar.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



}