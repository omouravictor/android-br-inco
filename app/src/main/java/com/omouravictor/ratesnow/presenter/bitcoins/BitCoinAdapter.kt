package com.omouravictor.ratesnow.presenter.bitcoins

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesnow.R
import com.omouravictor.ratesnow.data.local.entity.BitCoinEntity
import com.omouravictor.ratesnow.databinding.BitcoinItemListBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class BitCoinAdapter(
    private val list: List<BitCoinEntity>,
    private val context: Context
) : RecyclerView.Adapter<BitCoinAdapter.BitcoinViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BitcoinViewHolder {
        val binding =
            BitcoinItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BitcoinViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: BitcoinViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class BitcoinViewHolder(
        private val bitCoinItem: BitcoinItemListBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(bitCoinItem.root) {
        fun bind(bitcoin: BitCoinEntity) {
            val locale = Locale(
                bitcoin.bitcoinLanguage.substring(0..1),
                bitcoin.bitcoinLanguage.substring(3..4)
            )
            val dateFormatter = SimpleDateFormat("dd/MM/yy", locale)
            val timeFormatter = SimpleDateFormat("HH:mm", locale)
            val decimalFormat = DecimalFormat("#0.00#", DecimalFormatSymbols.getInstance(locale))
            val numberFormat = NumberFormat.getCurrencyInstance(locale)

            "${bitcoin.bitcoinName} - ${bitcoin.bitcoinISO}".also {
                bitCoinItem.textViewBitcoinName.text = it
            }
            bitCoinItem.textViewBitcoinVariation.text =
                decimalFormat.format(bitcoin.bitcoinVariation) + " %"
            bitCoinItem.textViewBitcoinValue.text = numberFormat.format(bitcoin.bitcoinLast)

            "${dateFormatter.format(bitcoin.date)}\n${timeFormatter.format(bitcoin.date)}".also {
                bitCoinItem.tvDateTime.text = it
            }

            if (bitcoin.bitcoinVariation <= 0.0) {
                bitCoinItem.textViewBitcoinVariation.setTextColor(
                    ContextCompat
                        .getColor(context, R.color.lightRed)
                )
            } else {
                bitCoinItem.textViewBitcoinVariation.text =
                    "+" + bitCoinItem.textViewBitcoinVariation.text
                bitCoinItem.textViewBitcoinVariation.setTextColor(
                    ContextCompat
                        .getColor(context, R.color.fluorescentGreen)
                )
            }

        }
    }
}