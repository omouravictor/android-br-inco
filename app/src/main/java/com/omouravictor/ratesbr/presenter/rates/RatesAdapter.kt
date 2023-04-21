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
    private val ratesList: List<RateUiModel>,
    private val onClickItem: (RateUiModel) -> Unit
) : RecyclerView.Adapter<RatesAdapter.RatesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val binding =
            RateItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatesViewHolder(binding, onClickItem)
    }

    override fun getItemCount(): Int {
        return ratesList.size
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.bind(ratesList[position])
    }

    class RatesViewHolder(
        private val rateItem: RateItemListBinding,
        private val onClickItem: (RateUiModel) -> Unit
    ) : RecyclerView.ViewHolder(rateItem.root) {
        fun bind(rateUiModel: RateUiModel) {
            val locale = Locale("pt", "BR")
            val dateFormatter = SimpleDateFormat("dd/MM/yy", locale)
            val timeFormatter = SimpleDateFormat("HH:mm", locale)
            val decimalFormat = DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(locale))
            val numberFormat = NumberFormat.getCurrencyInstance(locale)

            rateItem.textViewRateCurrencyTerm.text = rateUiModel.currencyTerm

            if (rateUiModel.variation >= 0) {
                "+${decimalFormat.format(rateUiModel.variation)}%".also {
                    rateItem.textViewRateVariation.text = it
                }
                rateItem.textViewRateVariation.setTextColor(Color.GREEN)
            }else {
                "${decimalFormat.format(rateUiModel.variation)}%".also {
                    rateItem.textViewRateVariation.text = it
                }
            }

            rateItem.textViewRateValue.text =
                numberFormat.format(round(1 * rateUiModel.unitaryRate * 100) / 100)

            "${dateFormatter.format(rateUiModel.rateDate)}\n${timeFormatter.format(rateUiModel.rateDate)}".also {
                rateItem.textViewDateTime.text = it
            }

            itemView.setOnClickListener {
                onClickItem(rateUiModel)
            }
        }
    }
}