package com.omouravictor.ratesbr.presenter.bitcoins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesbr.databinding.FragmentBitcoinsBinding
import com.omouravictor.ratesbr.presenter.base.UiResultState
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitcoinUiModel

class BitcoinsFragment : Fragment() {

    private lateinit var binding: FragmentBitcoinsBinding
    private val bitcoinViewModel: BitcoinsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBitcoinsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSwipeRefreshLayout()

        bitcoinViewModel.bitcoinsResult.observe(viewLifecycleOwner) {
            when (it) {
                is UiResultState.Success -> {
                    configureRecyclerView(it.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.recyclerViewBitcoins.isVisible = true
                    binding.includeViewError.root.isVisible = false
                }
                is UiResultState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.recyclerViewBitcoins.isVisible = false
                    binding.includeViewError.root.isVisible = true
                    binding.includeViewError.textViewErrorMessage.text = it.e.message
                }
                is UiResultState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.recyclerViewBitcoins.isVisible = false
                    binding.includeViewError.root.isVisible = false
                }
            }
        }
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            bitcoinViewModel.getBitcoins()
        }
    }

    private fun configureRecyclerView(bitcoinList: List<BitcoinUiModel>) {
        binding.recyclerViewBitcoins.apply {
            adapter = BitcoinsAdapter(bitcoinList)
            layoutManager = LinearLayoutManager(context)
        }
    }

}