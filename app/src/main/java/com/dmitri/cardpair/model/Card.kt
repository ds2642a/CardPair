package com.dmitri.cardpair.model

import android.graphics.drawable.Drawable

data class Card (
    var pos : Int,
    var imageDrawable : Drawable,
    var backImageResId : Int,
    var name : String,
    var isShown : Boolean = false,
    var isSolved : Boolean = false
)