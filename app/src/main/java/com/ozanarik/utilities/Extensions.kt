package com.ozanarik.utilities

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Extensions {

    companion object{


        fun Fragment.showSnackbar(message: String,length: Int=Snackbar.LENGTH_LONG){
            view?.let {
                Snackbar.make(it,message,length).show()
            }
        }






    }

}