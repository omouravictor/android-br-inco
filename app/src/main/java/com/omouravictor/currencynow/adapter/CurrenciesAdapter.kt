package com.omouravictor.currencynow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.currencynow.data.models.Conversion
import com.omouravictor.currencynow.databinding.CurrencyItemListBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class CurrenciesAdapter(
    private var list: List<Conversion>,
    private val brLocale: Locale,
) : RecyclerView.Adapter<CurrenciesAdapter.CurrencyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            CurrencyItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding, brLocale)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CurrencyViewHolder(
        private val currencyItem: CurrencyItemListBinding,
        private val brLocale: Locale
    ) : RecyclerView.ViewHolder(currencyItem.root) {
        fun bind(conversion: Conversion) {

            val date = Date()
            val locale = when (conversion.toCurrency) {
                "USD" -> Locale("en", "US")
                "EUR" -> Locale("en", "EU")
                "JPY" -> Locale("ja", "JP")
                "GBP" -> Locale("en", "GB")
                "CAD" -> Locale("en", "CA")
                else -> brLocale
            }

            currencyItem.tvFromCurrency.text = conversion.fromCurrency
            currencyItem.tvToCurrency.text = conversion.toCurrency
            currencyItem.tvDate.text = SimpleDateFormat("dd/MM/yy", brLocale).format(date)
            currencyItem.tvTime.text = SimpleDateFormat.getTimeInstance(3, brLocale).format(date)
            currencyItem.tvValue.text = NumberFormat.getCurrencyInstance(locale).format(conversion.value)
        }
    }

    fun setList(newList: List<Conversion>) {
        list = newList
        notifyDataSetChanged()
    }
}