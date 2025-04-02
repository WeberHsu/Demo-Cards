package com.weberhsu.presentation.ui.cards.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.weberhsu.presentation.R
import com.weberhsu.presentation.base.BaseFragment
import com.weberhsu.presentation.constant.KEY_FRAGMENT_TRANSITION_SUCCESS
import com.weberhsu.presentation.databinding.FragmentAddCardBinding
import com.weberhsu.presentation.extensions.clickWithTrigger
import com.weberhsu.presentation.extensions.gone
import com.weberhsu.presentation.extensions.visible
import com.weberhsu.presentation.ui.cards.viewmodel.AddCardViewModel
import com.weberhsu.presentation.ui.cards.widget.CardCvvInput
import com.weberhsu.presentation.ui.cards.widget.CardExpiryDatePickerTextView
import com.weberhsu.presentation.widget.KitInputText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCardFragment : BaseFragment<FragmentAddCardBinding>() {
    private lateinit var viewModel: AddCardViewModel

    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddCardBinding
        get() = FragmentAddCardBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[AddCardViewModel::class.java]
        binding.layoutTitle.txtTitle.text = getString(R.string.new_card)

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

        binding.inputCardName.onAfterTextChanged = {
            checkFieldValid(binding.inputCardName)
        }
        binding.inputNameOnCard.onAfterTextChanged = {
            checkFieldValid(binding.inputNameOnCard)
        }
        binding.inputCvv.onAfterTextChanged = {
            checkCvvFieldValid(binding.inputCvv)
        }
        binding.inputCardNumber.onAfterTextChanged = {
            checkFieldValid(binding.inputCardNumber)
        }

        binding.txtExpiryDate.run {
            expTextColor = ContextCompat.getColor(context, R.color.black)
            callback = object : CardExpiryDatePickerTextView.CardExpiryDateCallback {
                override fun onExpiryDateSelected(
                    view: CardExpiryDatePickerTextView,
                    year: String,
                    month: String,
                    dateValid: Boolean
                ) {
                    setCardExpiryDate(year, month)
                    handleExpiryDateValid()
                }
            }
            clickWithTrigger {
                showExpiryDatePicker()
            }
        }

        binding.btnAdd.clickWithTrigger {
            if (isFieldValid()) {
                // Add card
                viewModel.addCard(
                    binding.inputNameOnCard.getInputText(),
                    binding.inputCardName.getInputText(),
                    CardUtils.removeSpacesAndHyphens(binding.inputCardNumber.getInputText()) ?: "",
                    binding.txtExpiryDate.expiryMonth,
                    binding.txtExpiryDate.expiryYear,
                    binding.inputCvv.getInputText()
                )
            }
        }

        viewModel.addCardSuccess.observe(this) {
            goBackPage()
            Toast.makeText(
                requireContext(),
                getString(R.string.add_card_success),
                Toast.LENGTH_SHORT
            ).show()
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


    private fun isFieldValid(): Boolean {
        var isValid = true
        if (!checkFieldValid(binding.inputCardName)) {
            isValid = false
        }
        if (!checkFieldValid(binding.inputNameOnCard)) {
            isValid = false
        }
        if (!checkFieldValid(binding.inputCardNumber)) {
            isValid = false
        }
        if (!checkFieldValid(binding.inputCvv)) {
            isValid = false
        }
        if (!handleExpiryDateValid()) {
            isValid = false
        }
        return isValid
    }

    private fun handleExpiryDateValid(): Boolean {
        if (binding.txtExpiryDate.expiryYear.isBlank() || binding.txtExpiryDate.expiryMonth.isBlank()) {
            binding.tvExpiryDateErrorTip.visible()
            binding.txtExpiryDate.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_kit_input_text_error
                )
            )
            return false
        } else {
            binding.tvExpiryDateErrorTip.gone()
            binding.txtExpiryDate.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_kit_input_text
                )
            )
            return true
        }
    }

    private fun checkFieldValid(input: KitInputText): Boolean {
        if (input.getInputText().isEmpty()) {
            input.setShowErrorTip(true)
            input.showErrorStatus(getString(R.string.field_required))
            return false
        } else {
            input.setShowErrorTip(false)
            return true
        }
    }

    private fun checkCvvFieldValid(input: CardCvvInput): Boolean {
        if (input.getInputText().isEmpty() || !input.isValid) {
            input.setShowErrorTip(true)
            if (!input.isValid && input.getInputText().isNotEmpty()) {
                input.showErrorStatus("The field is invalid")
            } else {
                input.showErrorStatus(getString(R.string.field_required))
            }
            return false
        } else {
            input.setShowErrorTip(false)
            return true
        }
    }
}