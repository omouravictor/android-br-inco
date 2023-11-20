package com.omouravictor.brinco.presenter.stocks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.brinco.databinding.ItemListStockBinding
import com.omouravictor.brinco.presenter.stocks.model.StockUiModel
import com.omouravictor.brinco.util.FormatUtils.BrazilianFormats.brDateFormat
import com.omouravictor.brinco.util.FormatUtils.BrazilianFormats.brNumberFormat
import com.omouravictor.brinco.util.FormatUtils.BrazilianFormats.brTimeFormat
import com.omouravictor.brinco.util.ViewHolderUtils.setVariationOnBind

class StocksAdapter(
    private val originalList: List<StockUiModel>,
    private val callbackFunction: (StockUiModel) -> Unit
) : RecyclerView.Adapter<StocksAdapter.StockViewHolder>() {

    private var filteredList: List<StockUiModel> = originalList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding =
            ItemListStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding, callbackFunction)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun filterList(text: String) {
        filteredList = if (text.isNotEmpty())
            originalList.filter { it.name.contains(text, true) }
        else originalList
        notifyDataSetChanged()
    }

    class StockViewHolder(
        private val binding: ItemListStockBinding,
        private val callbackFunction: (StockUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(stock: StockUiModel) {
            binding.textViewStockLocation.text = stock.countryLocation
            binding.textViewStockName.text = stock.name
            setVariationOnBind(
                stock.variation,
                binding.textViewStockVariation,
                binding.imageViewStockVariation
            )
            binding.textViewStockPoints.text = brNumberFormat.format(stock.points)
            binding.textViewDate.text = brDateFormat.format(stock.stockDate)
            binding.textViewTime.text = brTimeFormat.format(stock.stockDate)
            itemView.setOnClickListener {
                callbackFunction(stock)
            }
        }
    }
}