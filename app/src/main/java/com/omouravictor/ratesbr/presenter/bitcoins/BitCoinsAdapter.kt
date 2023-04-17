package com.omouravictor.ratesbr.presenter.bitcoins

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesbr.databinding.BitcoinItemListBinding
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitCoinUiModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
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
        private val bitCoinItem: BitcoinItemListBinding
    ) : RecyclerView.ViewHolder(bitCoinItem.root) {
        fun bind(bitcoin: BitCoinUiModel) {
            val language = bitcoin.bitcoinLanguage.substring(0..1)
            val country = bitcoin.bitcoinLanguage.substring(3..4)
            val locale = Locale(language, country)
            val dateFormatter = SimpleDateFormat("dd/MM/yy", locale)
            val timeFormatter = SimpleDateFormat("HH:mm", locale)
            val decimalFormat =
                DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(Locale("pt", "BR")))
            val numberFormat = NumberFormat.getCurrencyInstance(locale)

            "${bitcoin.bitcoinName} / ${bitcoin.bitcoinISO}".also {
                bitCoinItem.textViewBitcoinName.text = it
            }

            bitCoinItem.textViewBitcoinValue.text = numberFormat.format(bitcoin.bitcoinLast)

            "${decimalFormat.format(bitcoin.bitcoinVariation)}%".also {
                bitCoinItem.textViewBitcoinVariation.text = it
            }

            if (bitcoin.bitcoinVariation >= 0) {
                "+${decimalFormat.format(bitcoin.bitcoinVariation)}%".also {
                    bitCoinItem.textViewBitcoinVariation.text = it
                }
                bitCoinItem.textViewBitcoinVariation.setTextColor(Color.GREEN)
            }

            "${dateFormatter.format(bitcoin.date)}\n${timeFormatter.format(bitcoin.date)}".also {
                bitCoinItem.tvDateTime.text = it
            }

        }
    }
}