package com.omouravictor.ratesbr.presenter.bitcoins

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesbr.databinding.BitcoinItemListBinding
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitCoinUiModel
import com.omouravictor.ratesbr.util.BrazilianFormats
import java.text.NumberFormat
import java.util.*

class BitCoinsAdapter(
    private val list: List<BitCoinUiModel>
) : RecyclerView.Adapter<BitCoinsAdapter.BitcoinViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BitcoinViewHolder {
        val binding =
            BitcoinItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BitcoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BitcoinViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class BitcoinViewHolder(
        private val bitcoinItemBinding: BitcoinItemListBinding
    ) : RecyclerView.ViewHolder(bitcoinItemBinding.root) {

        fun bind(bitcoin: BitCoinUiModel) {
            val language = bitcoin.language.substring(0..1)
            val country = bitcoin.language.substring(3..4)
            val locale = Locale(language, country)
            val numberFormat = NumberFormat.getCurrencyInstance(locale)

            "${bitcoin.name} / ${bitcoin.iSO}".also {
                bitcoinItemBinding.textViewBitcoinName.text = it
            }

            bitcoinItemBinding.textViewBitcoinValue.text = numberFormat.format(bitcoin.last)

            bindVariation(bitcoin.variation)

            bitcoinItemBinding.textViewDate.text = BrazilianFormats.dateFormat.format(bitcoin.date)

            bitcoinItemBinding.textViewTime.text = BrazilianFormats.timeFormat.format(bitcoin.date)
        }

        private fun bindVariation(variation: Double) {
            if (variation >= 0) {
                "+${BrazilianFormats.decimalFormat3Places.format(variation)}%".also {
                    bitcoinItemBinding.textViewBitcoinVariation.text = it
                }
                bitcoinItemBinding.textViewBitcoinVariation.setTextColor(Color.GREEN)
            } else {
                "${BrazilianFormats.decimalFormat3Places.format(variation)}%".also {
                    bitcoinItemBinding.textViewBitcoinVariation.text = it
                }
            }
        }
    }
}