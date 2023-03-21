package com.omouravictor.ratesnow.presenter.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesnow.databinding.ConversionItemListBinding
import com.omouravictor.ratesnow.presenter.rates.model.RateUiModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class RatesAdapter : ListAdapter<RateUiModel, RatesAdapter.CurrencyViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            ConversionItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class CurrencyViewHolder(
        private val currencyItem: ConversionItemListBinding
    ) : RecyclerView.ViewHolder(currencyItem.root) {
        fun bind(rateEntity: RateUiModel) {
            val defaultLocale = Locale("pt", "BR")
            val locale = when (rateEntity.toCurrency) {
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

            currencyItem.tvFromCurrency.text = rateEntity.fromCurrency
            currencyItem.tvToCurrency.text = rateEntity.toCurrency
            currencyItem.tvValue.text = NumberFormat.getCurrencyInstance(locale)
                .format(round(1 * rateEntity.rate * 100) / 100)
            "${dateFormatter.format(rateEntity.rateDate)}\n${timeFormatter.format(rateEntity.rateDate)}".also {
                currencyItem.tvDateTime.text = it
            }
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<RateUiModel>() {
            override fun areItemsTheSame(oldItem: RateUiModel, newItem: RateUiModel): Boolean {
                return oldItem.fromCurrency == newItem.fromCurrency && oldItem.toCurrency == newItem.toCurrency
            }

            override fun areContentsTheSame(oldItem: RateUiModel, newItem: RateUiModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}