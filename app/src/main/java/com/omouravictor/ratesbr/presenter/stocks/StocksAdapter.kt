package com.omouravictor.ratesbr.presenter.stocks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.ratesbr.databinding.ItemListStockBinding
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import com.omouravictor.ratesbr.util.BrazilianFormatUtils.dateFormat
import com.omouravictor.ratesbr.util.BrazilianFormatUtils.numberFormat
import com.omouravictor.ratesbr.util.BrazilianFormatUtils.timeFormat
import com.omouravictor.ratesbr.util.ViewHolderUtils.setVariationOnBind

class StocksAdapter(
    private val list: List<StockUiModel>,
    private val callBack: (StockUiModel) -> Unit
) : RecyclerView.Adapter<StocksAdapter.StockViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding =
            ItemListStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding, callBack)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class StockViewHolder(
        private val binding: ItemListStockBinding,
        private val callBack: (StockUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(stock: StockUiModel) {
            binding.textViewStockLocation.text = stock.countryLocation
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
                callBack(stock)
            }
        }
    }
}