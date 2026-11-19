package com.drojian.mathgamemini

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

abstract class BaseActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }
//
    protected fun applySystemBarPadding(topSpace: View,bottomSpace: View){
        val root = findViewById<View>(android.R.id.content)

        ViewCompat.setOnApplyWindowInsetsListener(root){  _,insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            topSpace.layoutParams.height = bars.top
            topSpace.requestLayout()

            bottomSpace.layoutParams.height = bars.bottom
            bottomSpace.requestLayout()
            insets
        }
    }

}