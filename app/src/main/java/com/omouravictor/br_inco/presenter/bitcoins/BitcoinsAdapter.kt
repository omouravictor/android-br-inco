package com.omouravictor.br_inco.presenter.bitcoins

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.br_inco.databinding.ItemListBitcoinBinding
import com.omouravictor.br_inco.presenter.bitcoins.model.BitcoinUiModel
import com.omouravictor.br_inco.util.FormatUtils.BrazilianFormats.brDateFormat
import com.omouravictor.br_inco.util.FormatUtils.BrazilianFormats.brTimeFormat
import com.omouravictor.br_inco.util.FormatUtils.getFormattedValueForCurrencyLocale
import com.omouravictor.br_inco.util.ViewHolderUtils.setVariationOnBind
import java.util.Locale

class BitcoinsAdapter(
    private val originalList: List<BitcoinUiModel>,
    private val callbackFunction: (BitcoinUiModel) -> Unit
) : RecyclerView.Adapter<BitcoinsAdapter.BitcoinViewHolder>() {

    private var filteredList: List<BitcoinUiModel> = originalList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BitcoinViewHolder {
        val binding =
            ItemListBitcoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BitcoinViewHolder(binding, callbackFunction)
    }

    override fun onBindViewHolder(holder: BitcoinViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

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

    class BitcoinViewHolder(
        private val binding: ItemListBitcoinBinding,
        private val callbackFunction: (BitcoinUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bitcoin: BitcoinUiModel) {
            val bitcoinLocale = Locale(bitcoin.language, bitcoin.countryLanguage)

            binding.textViewBitcoinCurrencyTerm.text = bitcoin.currencyTerm
            binding.textViewBitcoinName.text = bitcoin.name
            setVariationOnBind(
                bitcoin.variation,
                binding.textViewBitcoinVariation,
                binding.imageViewBitcoinVariation
            )
            binding.textViewBitcoinValue.text = getFormattedValueForCurrencyLocale(
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