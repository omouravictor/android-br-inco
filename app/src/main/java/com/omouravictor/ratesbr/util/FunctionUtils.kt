package com.omouravictor.ratesbr.util

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import com.omouravictor.ratesbr.R

object Functions {
    fun setVariationOnBind(variation: Double, textView: TextView, imageView: ImageView) {
        val formattedVariation = BrazilianFormats.numberFormat.format(variation)
        val variationText = if (variation >= 0.0) "+${formattedVariation}%" else "${formattedVariation}%"
        val textColor = if (variation >= 0.0) Color.GREEN else textView.currentTextColor
        val icon = if (variation >= 0.0) R.drawable.arrow_up_icon else R.drawable.arrow_down_icon

        textView.text = variationText
        textView.setTextColor(textColor)
        imageView.setImageResource(icon)
    }
}