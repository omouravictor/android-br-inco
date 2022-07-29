package com.omouravictor.ratesnow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesnow.databinding.ConversionItemListBinding
import com.omouravictor.ratesnow.model.Conversion
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class RateAdapter(
    private var list: List<Conversion>
) : RecyclerView.Adapter<RateAdapter.CurrencyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            ConversionItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CurrencyViewHolder(
        private val currencyItem: ConversionItemListBinding
    ) : RecyclerView.ViewHolder(currencyItem.root) {
        fun bind(conversion: Conversion) {
            val defaultLocale = Locale("pt", "BR")
            val locale = when (conversion.toCurrency) {
                "USD" -> Locale("en", "US")
                "EUR" -> Locale("en", "EU")
                "JPY" -> Locale("ja", "JP")
                "GBP" -> Locale("en", "GB")
                "CAD" -> Locale("en", "CA")
                else -> defaultLocale
            }
            val dateFormatter = SimpleDateFormat("dd/MM/yy", defaultLocale)
            val timeFormatter = SimpleDateFormat("HH:mm", defaultLocale)

            currencyItem.tvFromCurrency.text = conversion.fromCurrency
            currencyItem.tvToCurrency.text = conversion.toCurrency
            currencyItem.tvDate.text = dateFormatter.format(conversion.rateDate)
            currencyItem.tvTime.text = timeFormatter.format(conversion.rateDate)
            currencyItem.tvValue.text =
                NumberFormat.getCurrencyInstance(locale).format(conversion.getValue())
        }
    }
}