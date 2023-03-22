package com.omouravictor.ratesnow.presenter.bitcoins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesnow.databinding.FragmentBitcoinsBinding
import com.omouravictor.ratesnow.presenter.base.UiResultState
import com.omouravictor.ratesnow.presenter.bitcoins.model.BitCoinUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BitCoinsFragment : Fragment() {

    private lateinit var bitCoinBinding: FragmentBitcoinsBinding
    private val bitCoinViewModel: BitCoinsViewModel by activityViewModels()
    private lateinit var bitCoinAdapter: BitCoinsAdapter

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
                    initAdapter(it.data)
                    bitCoinBinding.swipeRefreshLayout.isRefreshing = false
                    bitCoinBinding.progressBar.isVisible = false
                    bitCoinBinding.rvBitCoins.isVisible = true
                }
                is UiResultState.Error -> {
                    bitCoinBinding.swipeRefreshLayout.isRefreshing = false
                    bitCoinBinding.progressBar.isVisible = false
                    bitCoinBinding.rvBitCoins.isVisible = false
                    Toast.makeText(context, it.e.message, Toast.LENGTH_SHORT).show()
                }
                is UiResultState.Loading -> {
                    bitCoinBinding.swipeRefreshLayout.isRefreshing = false
                    bitCoinBinding.progressBar.isVisible = true
                    bitCoinBinding.rvBitCoins.isVisible = false
                }
            }
        }
    }

    private fun initSwipeRefreshLayout() {
        bitCoinBinding.swipeRefreshLayout.setOnRefreshListener {
            bitCoinViewModel.getBitCoins()
        }
    }

    private fun initAdapter(bitCoinList: List<BitCoinUiModel>) {
        bitCoinAdapter = BitCoinsAdapter(bitCoinList)

        bitCoinBinding.rvBitCoins.apply {
            adapter = bitCoinAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}