package com.omouravictor.ratesnow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesnow.R
import com.omouravictor.ratesnow.database.entity.StockEntity
import com.omouravictor.ratesnow.databinding.StockItemListBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class StockAdapter(
    private var list: List<StockEntity>,
    private val context: Context
) : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding =
            StockItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class StockViewHolder(
        private val stockItem: StockItemListBinding,
        private val context: Context,
    ) : RecyclerView.ViewHolder(stockItem.root) {
        fun bind(stock: StockEntity) {
            val locale = Locale("pt", "BR")
            val dateFormatter = SimpleDateFormat("dd/MM/yy", locale)
            val timeFormatter = SimpleDateFormat("HH:mm", locale)
            val decimalFormat = DecimalFormat("#0.00#", DecimalFormatSymbols.getInstance(locale))

            "${stock.stockTerm} - ${stock.stockLocation}".also {
                stockItem.textViewStockName.text = it
            }
            stockItem.textViewStockVariation.text = decimalFormat.format(stock.stockVariation)
            "${dateFormatter.format(stock.date)}\n${timeFormatter.format(stock.date)}".also {
                stockItem.tvDateTime.text = it
            }

            if (stock.stockVariation <= 0.0) {
                stockItem.imageViewStockVariation.setImageResource(R.drawable.ic_arrow_down)
                stockItem.textViewStockVariation.setTextColor(context.getColor(R.color.grey))
                stockItem.percent.setTextColor(context.getColor(R.color.grey))
            } else {
                stockItem.imageViewStockVariation.setImageResource(R.drawable.ic_arrow_up)
                stockItem.textViewStockVariation.setTextColor(context.getColor(R.color.fluorescentGreen))
                stockItem.percent.setTextColor(context.getColor(R.color.fluorescentGreen))
            }
        }
    }
}