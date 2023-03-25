package com.omouravictor.ratesbr.presenter.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesbr.databinding.ConversionItemListBinding
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
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

    fun updateConversionRates(amount: Double) {
        ratesList.forEach {
            it.conversionRate = it.unityRate * amount
        }
        notifyDataSetChanged()
    }

    class ConversionViewHolder(
        private val conversionItem: ConversionItemListBinding
    ) : RecyclerView.ViewHolder(conversionItem.root) {
        fun bind(rateUiModel: RateUiModel) {
            val locale = Locale("pt", "BR")
            val dateFormatter = SimpleDateFormat("dd/MM/yy", locale)
            val timeFormatter = SimpleDateFormat("HH:mm", locale)

            conversionItem.tvFromCurrency.text = rateUiModel.fromCurrency
            conversionItem.tvToCurrency.text = rateUiModel.toCurrency
            conversionItem.tvValue.text = NumberFormat.getCurrencyInstance(locale)
                .format(round(1 * rateUiModel.conversionRate * 100) / 100)
            "${dateFormatter.format(rateUiModel.rateDate)}\n${timeFormatter.format(rateUiModel.rateDate)}".also {
                conversionItem.tvDateTime.text = it
            }
        }
    }
}