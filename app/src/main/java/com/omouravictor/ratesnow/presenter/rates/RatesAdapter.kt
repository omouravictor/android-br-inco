package com.omouravictor.ratesnow.presenter.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesnow.data.local.entity.RateEntity
import com.omouravictor.ratesnow.databinding.ConversionItemListBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class RatesAdapter : ListAdapter<RateEntity, RatesAdapter.CurrencyViewHolder>(diffCallback) {

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
        fun bind(rateEntity: RateEntity) {
            val defaultLocale = Locale("pt", "BR")
            val locale = when (rateEntity.currencyName) {
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

            currencyItem.tvFromCurrency.text = "BRL"
            currencyItem.tvToCurrency.text = rateEntity.currencyName
            currencyItem.tvValue.text =
                NumberFormat.getCurrencyInstance(locale)
                    .format(round(1 * rateEntity.rate * 100) / 100)
            "${dateFormatter.format(rateEntity.date)}\n${timeFormatter.format(rateEntity.date)}".also {
                currencyItem.tvDateTime.text = it
            }
        }
    }
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<RateEntity>() {
            override fun areItemsTheSame(oldItem: RateEntity, newItem: RateEntity): Boolean {
                return oldItem.currencyName == newItem.currencyName
            }

            override fun areContentsTheSame(oldItem: RateEntity, newItem: RateEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}