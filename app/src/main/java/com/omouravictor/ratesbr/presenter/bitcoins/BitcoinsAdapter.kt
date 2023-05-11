package com.omouravictor.ratesbr.presenter.bitcoins

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesbr.databinding.ItemListBitcoinBinding
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitcoinUiModel
import com.omouravictor.ratesbr.util.BrazilianFormatUtils.dateFormat
import com.omouravictor.ratesbr.util.BrazilianFormatUtils.timeFormat
import com.omouravictor.ratesbr.util.FormatUtils.getFormattedValueForCurrencyLocale
import com.omouravictor.ratesbr.util.ViewHolderUtils.setVariationOnBind
import java.util.*

class BitcoinsAdapter(
    private val originalList: List<BitcoinUiModel>,
    private val callBack: (BitcoinUiModel) -> Unit
) : RecyclerView.Adapter<BitcoinsAdapter.BitcoinViewHolder>() {

    private var filteredList: List<BitcoinUiModel> = originalList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BitcoinViewHolder {
        val binding =
            ItemListBitcoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BitcoinViewHolder(binding, callBack)
    }

    override fun onBindViewHolder(holder: BitcoinViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun filterList(text: String) {
        filteredList = if (text.isNotEmpty())
            originalList.filter { it.name.contains(text, true) }
        else originalList
        notifyDataSetChanged()
    }

    class BitcoinViewHolder(
        private val binding: ItemListBitcoinBinding,
        private val callBack: (BitcoinUiModel) -> Unit
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
            binding.textViewDate.text = dateFormat.format(bitcoin.bitcoinDate)
            binding.textViewTime.text = timeFormat.format(bitcoin.bitcoinDate)
            itemView.setOnClickListener {
                callBack(bitcoin)
            }
        }
    }
}