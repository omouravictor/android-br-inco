package com.omouravictor.br_inco.presenter.stocks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omouravictor.br_inco.databinding.ItemListBinding
import com.omouravictor.br_inco.presenter.stocks.model.StockUiModel
import com.omouravictor.br_inco.util.FormatUtils.BrazilianFormats.brDateFormat
import com.omouravictor.br_inco.util.FormatUtils.BrazilianFormats.brNumberFormat
import com.omouravictor.br_inco.util.FormatUtils.BrazilianFormats.brTimeFormat
import com.omouravictor.br_inco.util.ViewHolderUtils.setVariationOnBind

class StocksAdapter(
    private val originalList: List<StockUiModel>,
    private val callbackFunction: (StockUiModel) -> Unit
) : RecyclerView.Adapter<StocksAdapter.StockViewHolder>() {

    private var filteredList: List<StockUiModel> = originalList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding =
            ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding, callbackFunction)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

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

    class StockViewHolder(
        private val binding: ItemListBinding,
        private val callbackFunction: (StockUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(stock: StockUiModel) {
            binding.textViewInfo1.text = stock.countryLocation
            binding.textViewInfo2.text = stock.name
            setVariationOnBind(
                stock.variation,
                binding.textViewVariation,
                binding.imageViewVariation
            )
            binding.textViewInfo3.text = brNumberFormat.format(stock.points)
            binding.textViewDate.text = brDateFormat.format(stock.stockDate)
            binding.textViewTime.text = brTimeFormat.format(stock.stockDate)
            itemView.setOnClickListener {
                callbackFunction(stock)
            }
        }
    }
}