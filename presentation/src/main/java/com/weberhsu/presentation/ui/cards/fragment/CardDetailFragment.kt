package com.weberhsu.presentation.ui.cards.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.weberhsu.domain.entity.CardEntity
import com.weberhsu.presentation.R
import com.weberhsu.presentation.base.BaseFragment
import com.weberhsu.presentation.constant.KEY_FRAGMENT_TRANSITION_SUCCESS
import com.weberhsu.presentation.databinding.FragmentCardDetailBinding
import com.weberhsu.presentation.extensions.clickWithTrigger
import com.weberhsu.presentation.ui.cards.viewmodel.CardDetailViewModel
import com.weberhsu.presentation.ui.cards.Cards
import com.weberhsu.presentation.ui.cards.dialog.DeleteCardDialogFragment
import com.weberhsu.presentation.ui.cards.dialog.EditCardCvvDialogFragment
import com.weberhsu.presentation.ui.cards.dialog.EditCardFieldDialogFragment
import com.weberhsu.presentation.ui.cards.dialog.EditCardNumberDialogFragment
import com.weberhsu.presentation.ui.cards.widget.CardExpiryDatePickerTextView
import com.weberhsu.presentation.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CardDetailFragment : BaseFragment<FragmentCardDetailBinding>() {

    companion object {
        const val KEY_CARD_DATA = "KEY_CARD_DATA"

        fun newInstance(card: CardEntity): CardDetailFragment {
            return CardDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_CARD_DATA, card)
                }
            }
        }

    }

    private lateinit var viewModel: CardDetailViewModel

    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCardDetailBinding
        get() = FragmentCardDetailBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[CardDetailViewModel::class.java]
        binding.layoutTitle.imgBack.clickWithTrigger {
            goBackPage()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBackPage()
                }
            }
        )

        arguments?.getParcelable<CardEntity>(KEY_CARD_DATA)?.let { card ->
            binding.txtCardName.run {
                text = card.cardName
                setOnLongClickListener {
                    EditCardFieldDialogFragment.newInstance(
                        card,
                        getString(R.string.card_name_required),
                        card.cardName
                    ) { result ->
                        viewModel.updateCardName(card.id.orEmpty(), result)
                        card.cardName = result
                        binding.txtCardName.text = result
                    }.show(parentFragmentManager, "")
                    true
                }
            }
            binding.txtCardNumber.run {
                text = Cards.DEFAULT.formatNumber(card.cardNumber)
                setOnLongClickListener {
                    EditCardNumberDialogFragment.newInstance(card) { result ->
                        viewModel.updateCardNumber(card.id.orEmpty(), result)
                        card.cardNumber = result
                        binding.txtCardNumber.text = Cards.DEFAULT.formatNumber(result)
                    }.show(parentFragmentManager, "")
                    true
                }
            }
            binding.txtExpiryDate.run {
                expiryYear = card.expiryYear
                expiryMonth = card.expiryMonth
                setCardExpiryDate(card.expiryYear, card.expiryMonth)
                callback = object : CardExpiryDatePickerTextView.CardExpiryDateCallback {
                    override fun onExpiryDateSelected(
                        view: CardExpiryDatePickerTextView,
                        year: String,
                        month: String,
                        dateValid: Boolean
                    ) {
                        card.expiryYear = year
                        card.expiryMonth = month
                        viewModel.updateCardExpiryDate(card.id.orEmpty(), month, year)
                    }
                }
                setOnLongClickListener {
                    showExpiryDatePicker()
                    true
                }
            }
            binding.txtCvv.run {
                text = card.cvv
                setOnLongClickListener {
                    EditCardCvvDialogFragment.newInstance(card) { result ->
                        viewModel.updateCardCvv(card.id.orEmpty(), result)
                        card.cvv = result
                        binding.txtCvv.text = result
                    }.show(parentFragmentManager, "")
                    true
                }
            }
            binding.txtNameOnCard.run {
                text = card.userName
                setOnLongClickListener {
                    EditCardFieldDialogFragment.newInstance(
                        card,
                        getString(R.string.name_on_card_required),
                        card.userName
                    ) { result ->
                        viewModel.updateUserName(card.id.orEmpty(), result)
                        card.userName = result
                        binding.txtNameOnCard.text = result
                    }.show(parentFragmentManager, "")
                    true
                }
            }

            binding.btnDelete.clickWithTrigger {
                DeleteCardDialogFragment.newInstance {
                    viewModel.deleteCard(card.id.orEmpty())
                }.show(parentFragmentManager, "")
            }

            binding.imgCardNumberCopy.clickWithTrigger {
                Utils.copyToClipboardWithCheck(requireContext(), binding.txtCardNumber.text)
            }

            binding.imgExpDateCopy.clickWithTrigger {
                Utils.copyToClipboardWithCheck(requireContext(), binding.txtExpiryDate.text)
            }

            binding.imgCvvCopy.clickWithTrigger {
                Utils.copyToClipboardWithCheck(requireContext(), binding.txtCvv.text)
            }

            binding.imgNameOnCardCopy.clickWithTrigger {
                Utils.copyToClipboardWithCheck(requireContext(), binding.txtNameOnCard.text)
            }
        }

        viewModel.deleteSuccess.observe(this) {
            Toast.makeText(requireContext(), "Delete Card Successfully", Toast.LENGTH_SHORT).show()
            goBackPage()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMsg.collect {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goBackPage() {
        setFragmentResult(KEY_FRAGMENT_TRANSITION_SUCCESS, Bundle().apply {
            putString("result", "Card Added Successfully")
        })
        parentFragmentManager.popBackStack()
    }
}