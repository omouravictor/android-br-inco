package com.omouravictor.ratesbr.presenter.stocks

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesbr.R
import com.omouravictor.ratesbr.databinding.StockItemListBinding
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import com.omouravictor.ratesbr.util.BrazilianFormats

class StocksAdapter(
    private val list: List<StockUiModel>
) : RecyclerView.Adapter<StocksAdapter.StockViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding =
            StockItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class StockViewHolder(
        private val stockItem: StockItemListBinding
    ) : RecyclerView.ViewHolder(stockItem.root) {

        fun bind(stock: StockUiModel) {
            "${stock.name} / ${stock.location}".also {
                stockItem.textViewStockName.text = it
            }

            bindVariation(stock.variation)

            stockItem.textViewDate.text = BrazilianFormats.dateFormat.format(stock.date)

            stockItem.textViewTime.text = BrazilianFormats.timeFormat.format(stock.date)
        }

        private fun bindVariation(variation: Double) {
            if (variation >= 0.0) {
                "+${BrazilianFormats.decimalFormat2Places.format(variation)}%".also {
                    stockItem.textViewStockVariation.text = it
                }
                stockItem.imageViewStockVariation.setImageResource(R.drawable.arrow_up_icon)
                stockItem.textViewStockVariation.setTextColor(Color.GREEN)
            } else {
                "${BrazilianFormats.decimalFormat2Places.format(variation)}%".also {
                    stockItem.textViewStockVariation.text = it
                }
                stockItem.imageViewStockVariation.setImageResource(R.drawable.arrow_down_icon)
            }
        }
    }
}