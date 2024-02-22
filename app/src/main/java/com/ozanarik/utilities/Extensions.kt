package com.ozanarik.utilities

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

class Extensions {

    companion object{


        fun Fragment.showSnackbar(message: String,length: Int=Snackbar.LENGTH_LONG){
            view?.let {
                Snackbar.make(it,message,length).show()
            }
        }


        fun View.showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG){

            Snackbar.make(this,message,length).show()

        }
    }

}