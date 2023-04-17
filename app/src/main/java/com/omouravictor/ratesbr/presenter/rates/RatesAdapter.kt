package com.omouravictor.ratesbr.presenter.rates

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesbr.databinding.RateItemListBinding
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class RatesAdapter(
    private val ratesList: List<RateUiModel>
) : RecyclerView.Adapter<RatesAdapter.ConversionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversionViewHolder {
        val binding =
            RateItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConversionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return ratesList.size
    }

    override fun onBindViewHolder(holder: ConversionViewHolder, position: Int) {
        holder.bind(ratesList[position])
    }

    class ConversionViewHolder(
        private val conversionItem: RateItemListBinding
    ) : RecyclerView.ViewHolder(conversionItem.root) {
        fun bind(rateUiModel: RateUiModel) {
            val locale = Locale("pt", "BR")
            val dateFormatter = SimpleDateFormat("dd/MM/yy", locale)
            val timeFormatter = SimpleDateFormat("HH:mm", locale)
            val decimalFormat = DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(locale))
            val numberFormat = NumberFormat.getCurrencyInstance(locale)

            conversionItem.tvFromCurrency.text = rateUiModel.currency

            "${decimalFormat.format(rateUiModel.variation)}%".also {
                conversionItem.tvVariation.text = it
            }

            if (rateUiModel.variation >= 0) {
                "+${decimalFormat.format(rateUiModel.variation)}%".also {
                    conversionItem.tvVariation.text = it
                }
                conversionItem.tvVariation.setTextColor(Color.GREEN)
            }

            conversionItem.tvValue.text =
                numberFormat.format(round(1 * rateUiModel.unityRate * 100) / 100)

            "${dateFormatter.format(rateUiModel.rateDate)}\n${timeFormatter.format(rateUiModel.rateDate)}".also {
                conversionItem.tvDateTime.text = it
            }
        }
    }
}