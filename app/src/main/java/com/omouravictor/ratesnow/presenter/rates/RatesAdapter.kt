package com.omouravictor.ratesnow.presenter.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesnow.data.local.entity.CurrencyEntity
import com.omouravictor.ratesnow.databinding.ConversionItemListBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class RatesAdapter() : ListAdapter<CurrencyEntity, RatesAdapter.CurrencyViewHolder>(diffCallback) {

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
        fun bind(currencyEntity: CurrencyEntity) {
            val defaultLocale = Locale("pt", "BR")
            val locale = when (currencyEntity.currencyName) {
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
            currencyItem.tvToCurrency.text = currencyEntity.currencyName
            currencyItem.tvValue.text =
                NumberFormat.getCurrencyInstance(locale)
                    .format(round(1 * currencyEntity.rate * 100) / 100)
            "${dateFormatter.format(currencyEntity.date)}\n${timeFormatter.format(currencyEntity.date)}".also {
                currencyItem.tvDateTime.text = it
            }
        }
    }
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<CurrencyEntity>() {
            override fun areItemsTheSame(oldItem: CurrencyEntity, newItem: CurrencyEntity): Boolean {
                return oldItem.currencyName == newItem.currencyName
            }

            override fun areContentsTheSame(oldItem: CurrencyEntity, newItem: CurrencyEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}