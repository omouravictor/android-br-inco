package com.omouravictor.ratesbr.presenter.stocks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesbr.databinding.StockItemListBinding
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import com.omouravictor.ratesbr.util.BrazilianFormats
import com.omouravictor.ratesbr.util.Functions.setVariationOnBind

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
        private val binding: StockItemListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(stock: StockUiModel) {
            binding.textViewStockLocation.text = stock.location

            binding.textViewStockName.text = stock.name

            setVariationOnBind(
                stock.variation,
                binding.textViewStockVariation,
                binding.imageViewStockVariation
            )

            binding.textViewStockPoints.text = BrazilianFormats.numberFormat.format(stock.points)

            binding.textViewDate.text = BrazilianFormats.dateFormat.format(stock.stockDate)

            binding.textViewTime.text = BrazilianFormats.timeFormat.format(stock.stockDate)
        }
    }
}