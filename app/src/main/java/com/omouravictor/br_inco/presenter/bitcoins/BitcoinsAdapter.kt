package com.omouravictor.br_inco.presenter.bitcoins

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.br_inco.databinding.ItemListBinding
import com.omouravictor.br_inco.presenter.bitcoins.model.BitcoinUiModel
import com.omouravictor.br_inco.util.FormatUtils.BrazilianFormats.brDateFormat
import com.omouravictor.br_inco.util.FormatUtils.BrazilianFormats.brTimeFormat
import com.omouravictor.br_inco.util.FormatUtils.getFormattedValueForCurrencyLocale
import com.omouravictor.br_inco.util.ViewHolderUtils.setVariationOnBind
import java.util.Locale

class BitcoinsAdapter(
    private val callbackFunction: (BitcoinUiModel) -> Unit
) : RecyclerView.Adapter<BitcoinsAdapter.BitcoinViewHolder>() {

    private var originalList = listOf<BitcoinUiModel>()
    private var filteredList: List<BitcoinUiModel> = originalList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BitcoinViewHolder {
        val binding =
            ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BitcoinViewHolder(binding, callbackFunction)
    }

    override fun onBindViewHolder(holder: BitcoinViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int = filteredList.size

    fun filterList(text: String) {
        filteredList = if (text.isNotEmpty()) {
            originalList.filter {
                it.name.contains(text, true) ||
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

    fun setList(list: List<BitcoinUiModel>) {
        originalList = list
        filteredList = list
        notifyDataSetChanged()
    }

    class BitcoinViewHolder(
        private val binding: ItemListBinding,
        private val callbackFunction: (BitcoinUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bitcoin: BitcoinUiModel) {
            val bitcoinLocale = Locale(bitcoin.language, bitcoin.countryLanguage)

            binding.textViewInfo1.text = bitcoin.currencyTerm
            binding.textViewInfo2.text = bitcoin.name
            setVariationOnBind(
                bitcoin.variation,
                binding.textViewVariation,
                binding.imageViewVariation
            )
            binding.textViewInfo3.text = getFormattedValueForCurrencyLocale(
                bitcoin.unitaryRate,
                bitcoinLocale
            )
            binding.textViewDate.text = brDateFormat.format(bitcoin.bitcoinDate)
            binding.textViewTime.text = brTimeFormat.format(bitcoin.bitcoinDate)
            itemView.setOnClickListener {
                callbackFunction(bitcoin)
            }
        }
    }
}