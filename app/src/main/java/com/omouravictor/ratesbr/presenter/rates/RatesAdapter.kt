package com.omouravictor.ratesbr.presenter.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesbr.databinding.ItemListRateBinding
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import com.omouravictor.ratesbr.util.FormatUtils.BrazilianFormats.brCurrencyFormat
import com.omouravictor.ratesbr.util.FormatUtils.BrazilianFormats.brDateFormat
import com.omouravictor.ratesbr.util.FormatUtils.BrazilianFormats.brTimeFormat
import com.omouravictor.ratesbr.util.NumberUtils.getRoundedDouble
import com.omouravictor.ratesbr.util.ViewHolderUtils.setVariationOnBind

class RatesAdapter(
    private val originalList: List<RateUiModel>,
    private val callBack: (RateUiModel) -> Unit
) : RecyclerView.Adapter<RatesAdapter.RatesViewHolder>() {

    private var filteredList: List<RateUiModel> = originalList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val binding =
            ItemListRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatesViewHolder(binding, callBack)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    fun filterList(text: String) {
        filteredList = if (text.isNotEmpty())
            originalList.filter { it.currencyName.contains(text, true) }
        else originalList
        notifyDataSetChanged()
    }

    class RatesViewHolder(
        private val binding: ItemListRateBinding,
        private val callBack: (RateUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(rate: RateUiModel) {
            binding.textViewRateCurrencyTerm.text = rate.currencyTerm
            binding.textViewRateCurrencyName.text = rate.currencyName
            setVariationOnBind(
                rate.variation,
                binding.textViewRateVariation,
                binding.imageViewRateVariation
            )
            binding.textViewRateValue.text = brCurrencyFormat.format(
                getRoundedDouble(rate.unitaryRate)
            )
            binding.textViewDate.text = brDateFormat.format(rate.rateDate)
            binding.textViewTime.text = brTimeFormat.format(rate.rateDate)
            itemView.setOnClickListener {
                callBack(rate)
            }
        }
    }
}