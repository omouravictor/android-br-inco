package com.omouravictor.br_inco.util

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import com.omouravictor.br_inco.R

object ViewHolderUtils {
    fun setVariationOnBind(variation: Double, textView: TextView, imageView: ImageView) {
        textView.text = StringUtils.getVariationText(variation)

        textView.setTextColor(
            if (variation >= 0.0)
                Color.parseColor("#19FF00")
            else
                Color.parseColor("#FFEB3B")
        )

        imageView.setImageResource(
            if (variation >= 0.0)
                R.drawable.icon_arrow_up
            else
                R.drawable.icon_arrow_down
        )
    }
}