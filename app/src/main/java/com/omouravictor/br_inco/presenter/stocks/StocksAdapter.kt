package com.omouravictor.br_inco.presenter.stocks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.br_inco.databinding.ItemListBinding
import com.omouravictor.br_inco.presenter.stocks.model.StockUiModel
import com.omouravictor.br_inco.util.FormatUtils
import com.omouravictor.br_inco.util.ViewHolderUtils

class StocksAdapter(
    private val callbackFunction: (StockUiModel) -> Unit
) : RecyclerView.Adapter<StocksAdapter.StockViewHolder>() {

    private var originalList = listOf<StockUiModel>()
    private var filteredList = originalList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding =
            ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding, callbackFunction)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int = filteredList.size

    fun filterList(text: String) {
        filteredList = if (text.isNotEmpty()) {
            originalList.filter {
                it.name.contains(text, true) ||
                        it.countryLocation.contains(text, true)
            }
        } else {
            originalList
        }

        notifyDataSetChanged()
    }

    fun clearFilter() {
        filteredList = originalList
        notifyDataSetChanged()
    }

    fun setList(list: List<StockUiModel>) {
        originalList = list
        filteredList = list
        notifyDataSetChanged()
    }

    class StockViewHolder(
        private val binding: ItemListBinding,
        private val callbackFunction: (StockUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(stock: StockUiModel) {
            binding.textViewInfo1.text = stock.countryLocation
            binding.textViewInfo2.text = stock.name
            ViewHolderUtils.setVariationOnBind(
                stock.variation,
                binding.textViewVariation,
                binding.imageViewVariation
            )
            binding.textViewInfo3.text =
                FormatUtils.BrazilianFormats.brDecimalFormat.format(stock.points)
            binding.textViewDate.text =
                FormatUtils.BrazilianFormats.brDateFormat.format(stock.stockDate)
            binding.textViewTime.text =
                FormatUtils.BrazilianFormats.brTimeFormat.format(stock.stockDate)
            itemView.setOnClickListener {
                callbackFunction(stock)
            }
        }
    }
}