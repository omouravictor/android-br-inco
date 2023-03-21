package com.omouravictor.ratesnow.presenter.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesnow.databinding.ConversionItemListBinding
import com.omouravictor.ratesnow.presenter.rates.model.RateUiModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class RatesAdapter(
    private var ratesList: List<RateUiModel>
) : RecyclerView.Adapter<RatesAdapter.ConversionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversionViewHolder {
        val binding =
            ConversionItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConversionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return ratesList.size
    }

    override fun onBindViewHolder(holder: ConversionViewHolder, position: Int) {
        holder.bind(ratesList[position])
    }

    class ConversionViewHolder(
        private val conversionItem: ConversionItemListBinding
    ) : RecyclerView.ViewHolder(conversionItem.root) {
        fun bind(rateUiModel: RateUiModel) {
            val defaultLocale = Locale("pt", "BR")
            val locale = when (rateUiModel.toCurrency) {
                "USD" -> Locale("en", "US")
                "EUR" -> Locale("en", "EU")
                "JPY" -> Locale("ja", "JP")
                "GBP" -> Locale("en", "GB")
                "CAD" -> Locale("en", "CA")
                "AUD" -> Locale("en", "AU")
                else -> defaultLocale
            }
            val dateFormatter = SimpleDateFormat("dd/MM/yy", defaultLocale)
            val timeFormatter = SimpleDateFormat("HH:mm", defaultLocale)

            conversionItem.tvFromCurrency.text = rateUiModel.fromCurrency
            conversionItem.tvToCurrency.text = rateUiModel.toCurrency
            conversionItem.tvValue.text = NumberFormat.getCurrencyInstance(locale)
                .format(round(1 * rateUiModel.rate * 100) / 100)
            "${dateFormatter.format(rateUiModel.rateDate)}\n${timeFormatter.format(rateUiModel.rateDate)}".also {
                conversionItem.tvDateTime.text = it
            }
        }
    }
}