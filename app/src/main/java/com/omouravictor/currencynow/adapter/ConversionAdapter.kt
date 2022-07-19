package com.omouravictor.currencynow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.currencynow.data.models.Conversion
import com.omouravictor.currencynow.databinding.ConversionItemListBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ConversionAdapter(
    private var list: List<Conversion>,
    private val defaultLocale: Locale = Locale("pt", "BR"),
    private val dateFormatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yy", defaultLocale),
    private val timeFormatter: SimpleDateFormat = SimpleDateFormat("HH:mm", defaultLocale)
) : RecyclerView.Adapter<ConversionAdapter.CurrencyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            ConversionItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding, defaultLocale, dateFormatter, timeFormatter)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CurrencyViewHolder(
        private val currencyItem: ConversionItemListBinding,
        private val defaultLocale: Locale,
        private val dateFormatter: SimpleDateFormat,
        private val timeFormatter: SimpleDateFormat
    ) : RecyclerView.ViewHolder(currencyItem.root) {
        fun bind(conversion: Conversion) {
            val locale = when (conversion.toCurrency) {
                "USD" -> Locale("en", "US")
                "EUR" -> Locale("en", "EU")
                "JPY" -> Locale("ja", "JP")
                "GBP" -> Locale("en", "GB")
                "CAD" -> Locale("en", "CA")
                else -> defaultLocale
            }

            currencyItem.tvFromCurrency.text = conversion.fromCurrency
            currencyItem.tvToCurrency.text = conversion.toCurrency
            currencyItem.tvDate.text = dateFormatter.format(conversion.rateDate)
            currencyItem.tvTime.text = timeFormatter.format(conversion.rateDate)
            currencyItem.tvValue.text = NumberFormat.getCurrencyInstance(locale).format(conversion.getValue())
        }
    }

    fun setZeroRates() {
        for (conv: Conversion in list) conv.rate = 0.0
        notifyDataSetChanged()
    }

    fun setList(newList: List<Conversion>) {
        list = newList
        notifyDataSetChanged()
    }

    fun removeAll() {
        list = emptyList()
        notifyDataSetChanged()
    }
}