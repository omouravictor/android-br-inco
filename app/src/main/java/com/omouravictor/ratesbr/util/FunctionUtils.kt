package com.omouravictor.ratesbr.util

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import com.omouravictor.ratesbr.R

object Functions {
    fun setVariationOnBind(variation: Double, textView: TextView, imageView: ImageView) {
        textView.text = getVariationText(variation)

        textView.setTextColor(
            if (variation >= 0.0) Color.parseColor("#19FF00") else textView.currentTextColor
        )

        imageView.setImageResource(
            if (variation >= 0.0) R.drawable.arrow_up_icon else R.drawable.arrow_down_icon
        )
    }

    fun getVariationText(variation: Double): String {
        val formattedVariation = BrazilianFormats.numberFormat.format(variation)
        return if (variation >= 0.0) "+${formattedVariation}%" else "${formattedVariation}%"
    }
}