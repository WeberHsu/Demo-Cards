package com.weberhsu.presentation.ui.cards.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.weberhsu.domain.entity.CardEntity
import com.weberhsu.presentation.R
import com.weberhsu.presentation.base.BaseFragment
import com.weberhsu.presentation.databinding.FragmentCardsBinding
import com.weberhsu.presentation.extensions.clickWithTrigger
import com.weberhsu.presentation.extensions.gone
import com.weberhsu.presentation.extensions.visible
import com.weberhsu.presentation.ui.cards.adapter.CardAdapter
import com.weberhsu.presentation.ui.cards.viewmodel.CardsViewModel
import com.weberhsu.presentation.ui.cards.widget.StackedCardItemDecoration
import com.weberhsu.presentation.util.ScreenUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CardsFragment : BaseFragment<FragmentCardsBinding>() {
    companion object {
        private const val CARD_OVERLAP_OFFSET = 160f
    }

    private lateinit var viewModel: CardsViewModel
    private var cardAdapter: CardAdapter? = null
    private var isInitView = false
    private var cardList = listOf<CardEntity>()

    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCardsBinding
        get() = FragmentCardsBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[CardsViewModel::class.java]

        viewModel.getCards()

        viewModel.event.observe(viewLifecycleOwner) {
            binding.txtCardCount.text = it.size.toString()
            cardList = it
            if (it.isEmpty()) {
                showNoCardView()
            } else {
                showCardsView()
            }
            cardAdapter?.run {
                refreshData(cardList.toMutableList())
                // Scroll to end
                val lastPosition = itemCount - 1
                if (lastPosition >= 0) {
                    binding.layoutCards.recyclerview.scrollToPosition(lastPosition)
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMsg.collect {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showNoCardView() {
        isInitView = true
        binding.layoutNoCards.root.visible()
        binding.layoutCards.root.gone()
        binding.layoutNoCards.btn.clickWithTrigger {
            goToAddCardPage()
        }
    }

    private fun showCardsView() {
        isInitView = true
        binding.layoutNoCards.root.gone()
        binding.layoutCards.root.visible()
        cardAdapter = CardAdapter()
        binding.layoutCards.recyclerview.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cardAdapter?.apply {
                cardItemClickListener = object : CardAdapter.CardsItemCallback {
                    override fun onClickItem(card: CardEntity) {
                        parentFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                            .replace(
                                R.id.fragmentContainer,
                                CardDetailFragment.newInstance(card)
                            )
                            .addToBackStack(null)
                            .commit()
                    }

                }
            }
            addItemDecoration(
                StackedCardItemDecoration(
                    ScreenUtils.dp2px(
                        requireContext(),
                        CARD_OVERLAP_OFFSET
                    )
                )
            ) // Set cards overlap
            setupItemTouchHelper(this)
        }

        binding.layoutCards.btnAdd.clickWithTrigger {
            goToAddCardPage()
        }
    }

    private fun goToAddCardPage() {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            .replace(R.id.fragmentContainer, AddCardFragment())
            .addToBackStack(null).commit()
    }

    private fun setupItemTouchHelper(view: RecyclerView) {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                cardAdapter?.swapItems(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        })
        itemTouchHelper.attachToRecyclerView(view)
    }
}