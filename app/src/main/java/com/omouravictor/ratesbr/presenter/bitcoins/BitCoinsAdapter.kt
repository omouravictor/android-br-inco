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
            val language = bitcoin.language.substring(0..1)
            val country = bitcoin.language.substring(3..4)
            val locale = Locale(language, country)
            val dateFormatter = SimpleDateFormat("dd/MM/yy", locale)
            val timeFormatter = SimpleDateFormat("HH:mm", locale)
            val decimalFormat =
                DecimalFormat("#0.000", DecimalFormatSymbols.getInstance(Locale("pt", "BR")))
            val numberFormat = NumberFormat.getCurrencyInstance(locale)

            "${bitcoin.name} / ${bitcoin.iSO}".also {
                bitCoinItem.textViewBitcoinName.text = it
            }

            bitCoinItem.textViewBitcoinValue.text = numberFormat.format(bitcoin.last)

            if (bitcoin.variation >= 0) {
                "+${decimalFormat.format(bitcoin.variation)}%".also {
                    bitCoinItem.textViewBitcoinVariation.text = it
                }
                bitCoinItem.textViewBitcoinVariation.setTextColor(Color.GREEN)
            } else {
                "${decimalFormat.format(bitcoin.variation)}%".also {
                    bitCoinItem.textViewBitcoinVariation.text = it
                }
            }

            "${dateFormatter.format(bitcoin.date)}\n${timeFormatter.format(bitcoin.date)}".also {
                bitCoinItem.textViewDateTime.text = it
            }

        }
    }
}