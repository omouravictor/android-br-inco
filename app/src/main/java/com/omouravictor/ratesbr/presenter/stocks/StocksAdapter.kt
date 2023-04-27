package com.omouravictor.ratesbr.presenter.stocks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesbr.databinding.StockItemListBinding
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import com.omouravictor.ratesbr.util.BrazilianFormats.dateFormat
import com.omouravictor.ratesbr.util.BrazilianFormats.numberFormat
import com.omouravictor.ratesbr.util.BrazilianFormats.timeFormat
import com.omouravictor.ratesbr.util.Functions.setVariationOnBind

class StocksAdapter(
    private val list: List<StockUiModel>,
    private val onClickItem: (StockUiModel) -> Unit
) : RecyclerView.Adapter<StocksAdapter.StockViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding =
            StockItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding, onClickItem)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class StockViewHolder(
        private val binding: StockItemListBinding,
        private val onClickItem: (StockUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(stock: StockUiModel) {
            binding.textViewStockLocation.text = when (stock.location.split(", ").last()) {
                "Brazil" -> "Brasil"
                "United States" -> "Estados Unidos"
                "French" -> "França"
                "Japan" -> "Japão"
                else -> "País não encontrado"
            }
            binding.textViewStockName.text = stock.name
            setVariationOnBind(
                stock.variation,
                binding.textViewStockVariation,
                binding.imageViewStockVariation
            )
            binding.textViewStockPoints.text = numberFormat.format(stock.points)
            binding.textViewDate.text = dateFormat.format(stock.stockDate)
            binding.textViewTime.text = timeFormat.format(stock.stockDate)
            itemView.setOnClickListener {
                onClickItem(stock)
            }
        }
    }
}