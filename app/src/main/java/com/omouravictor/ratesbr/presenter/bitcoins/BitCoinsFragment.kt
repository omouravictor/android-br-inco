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
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitCoinUiModel
import dagger.hilt.android.AndroidEntryPoint

class BitCoinsFragment : Fragment() {

    private lateinit var bitCoinBinding: FragmentBitcoinsBinding
    private val bitCoinViewModel: BitCoinsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bitCoinBinding = FragmentBitcoinsBinding.inflate(layoutInflater, container, false)
        return bitCoinBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSwipeRefreshLayout()

        bitCoinViewModel.bitCoins.observe(viewLifecycleOwner) {
            when (it) {
                is UiResultState.Success -> {
                    configureRecyclerView(it.data)
                    bitCoinBinding.swipeRefreshLayout.isRefreshing = false
                    bitCoinBinding.progressBar.isVisible = false
                    bitCoinBinding.rvBitCoins.isVisible = true
                    bitCoinBinding.includeViewError.root.isVisible = false
                }
                is UiResultState.Error -> {
                    bitCoinBinding.swipeRefreshLayout.isRefreshing = false
                    bitCoinBinding.progressBar.isVisible = false
                    bitCoinBinding.rvBitCoins.isVisible = false
                    bitCoinBinding.includeViewError.root.isVisible = true
                    bitCoinBinding.includeViewError.textViewErrorMessage.text = it.e.message
                }
                is UiResultState.Loading -> {
                    bitCoinBinding.swipeRefreshLayout.isRefreshing = false
                    bitCoinBinding.progressBar.isVisible = true
                    bitCoinBinding.rvBitCoins.isVisible = false
                    bitCoinBinding.includeViewError.root.isVisible = false
                }
            }
        }
    }

    private fun initSwipeRefreshLayout() {
        bitCoinBinding.swipeRefreshLayout.setOnRefreshListener {
            bitCoinViewModel.getBitCoins()
        }
    }

    private fun configureRecyclerView(bitCoinList: List<BitCoinUiModel>) {
        bitCoinBinding.rvBitCoins.apply {
            adapter = BitCoinsAdapter(bitCoinList)
            layoutManager = LinearLayoutManager(context)
        }
    }

}