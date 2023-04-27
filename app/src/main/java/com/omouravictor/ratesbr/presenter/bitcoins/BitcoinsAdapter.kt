package com.omouravictor.ratesbr.presenter.bitcoins

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesbr.databinding.BitcoinItemListBinding
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitcoinUiModel
import com.omouravictor.ratesbr.util.BrazilianFormats.dateFormat
import com.omouravictor.ratesbr.util.BrazilianFormats.timeFormat
import com.omouravictor.ratesbr.util.Functions.setVariationOnBind
import java.text.NumberFormat
import java.util.*

class BitcoinsAdapter(
    private val list: List<BitcoinUiModel>
) : RecyclerView.Adapter<BitcoinsAdapter.BitcoinViewHolder>() {

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
        private val binding: BitcoinItemListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bitcoin: BitcoinUiModel) {
            val language = bitcoin.language.substring(0..1)
            val country = bitcoin.language.substring(3..4)
            val locale = Locale(language, country)
            val numberFormat = NumberFormat.getCurrencyInstance(locale)

            binding.textViewBitcoinCurrencyTerm.text = bitcoin.iSO
            binding.textViewBitcoinName.text = bitcoin.name
            setVariationOnBind(
                bitcoin.variation,
                binding.textViewBitcoinVariation,
                binding.imageViewBitcoinVariation
            )
            binding.textViewBitcoinValue.text = numberFormat.format(bitcoin.last)
            binding.textViewDate.text = dateFormat.format(bitcoin.bitcoinDate)
            binding.textViewTime.text = timeFormat.format(bitcoin.bitcoinDate)
        }
    }
}