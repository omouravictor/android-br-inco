package com.omouravictor.ratesnow.presenter.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesnow.databinding.ConversionItemListBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class RatesAdapter() : ListAdapter<RatesDto, RatesAdapter.CurrencyViewHolder>(diffCallback) {

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
        fun bind(ratesDto: RatesDto) {
            val defaultLocale = Locale("pt", "BR")
            val locale = when (ratesDto.toCurrency) {
                "USD" -> Locale("en", "US")
                "EUR" -> Locale("en", "EU")
                "JPY" -> Locale("ja", "JP")
                "GBP" -> Locale("en", "GB")
                "CAD" -> Locale("en", "CA")
                else -> defaultLocale
            }
            val dateFormatter = SimpleDateFormat("dd/MM/yy", defaultLocale)
            val timeFormatter = SimpleDateFormat("HH:mm", defaultLocale)

            currencyItem.tvFromCurrency.text = ratesDto.fromCurrency
            currencyItem.tvToCurrency.text = ratesDto.toCurrency
            currencyItem.tvValue.text =
                NumberFormat.getCurrencyInstance(locale)
                    .format(round(ratesDto.amount * ratesDto.rate * 100) / 100)
            "${dateFormatter.format(ratesDto.rateDate)}\n${timeFormatter.format(ratesDto.rateDate)}".also {
                currencyItem.tvDateTime.text = it
            }
        }
    }
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<RatesDto>() {
            override fun areItemsTheSame(oldItem: RatesDto, newItem: RatesDto): Boolean {
                return oldItem.fromCurrency == newItem.fromCurrency
            }

            override fun areContentsTheSame(oldItem: RatesDto, newItem: RatesDto): Boolean {
                return oldItem == newItem
            }
        }
    }
}