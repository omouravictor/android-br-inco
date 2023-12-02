package com.omouravictor.br_inco.presenter.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.br_inco.databinding.ItemListBinding
import com.omouravictor.br_inco.presenter.rates.model.RateUiModel
import com.omouravictor.br_inco.util.FormatUtils.BrazilianFormats.brCurrencyFormat
import com.omouravictor.br_inco.util.FormatUtils.BrazilianFormats.brDateFormat
import com.omouravictor.br_inco.util.FormatUtils.BrazilianFormats.brTimeFormat
import com.omouravictor.br_inco.util.NumberUtils.getRoundedDouble
import com.omouravictor.br_inco.util.ViewHolderUtils.setVariationOnBind

class RatesAdapter(
    private val callbackFunction: (RateUiModel) -> Unit
) : RecyclerView.Adapter<RatesAdapter.RatesViewHolder>() {

    private var originalList = listOf<RateUiModel>()
    private var filteredList: List<RateUiModel> = originalList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val binding =
            ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatesViewHolder(binding, callbackFunction)
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int = filteredList.size

    fun filterList(text: String) {
        filteredList = if (text.isNotEmpty()) {
            originalList.filter {
                it.currencyName.contains(text, true) ||
                        it.currencyTerm.contains(text, true)
            }
        } else {
            originalList
        }

        notifyDataSetChanged()
    }

    fun clearFilter() {
        filteredList = originalList
        notifyDataSetChanged()
    }

    fun setList(list: List<RateUiModel>) {
        originalList = list
        filteredList = list
        notifyDataSetChanged()
    }

    class RatesViewHolder(
        private val binding: ItemListBinding,
        private val callbackFunction: (RateUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(rate: RateUiModel) {
            binding.textViewInfo1.text = rate.currencyTerm
            binding.textViewInfo2.text = rate.currencyName
            setVariationOnBind(
                rate.variation,
                binding.textViewVariation,
                binding.imageViewVariation
            )
            binding.textViewInfo3.text = brCurrencyFormat.format(
                getRoundedDouble(rate.unitaryRate)
            )
            binding.textViewDate.text = brDateFormat.format(rate.rateDate)
            binding.textViewTime.text = brTimeFormat.format(rate.rateDate)
            itemView.setOnClickListener {
                callbackFunction(rate)
            }
        }
    }
}