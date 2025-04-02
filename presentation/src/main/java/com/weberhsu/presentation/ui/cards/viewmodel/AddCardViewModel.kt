package com.weberhsu.presentation.ui.cards.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.weberhsu.domain.entity.CardEntity
import com.weberhsu.domain.usecase.CardUseCase
import com.weberhsu.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardViewModel @Inject constructor(
    private val useCase: CardUseCase
) : BaseViewModel() {

    private val _addCardSuccess = MutableLiveData<Boolean>()
    val addCardSuccess: LiveData<Boolean>
        get() = _addCardSuccess

    fun addCard(
        userName: String,
        cardName: String,
        cardNumber: String,
        expiryMonth: String,
        expiryYear: String,
        cvv: String
    ) {
        viewModelScope.launch {
            runCatching {
                useCase.addCard(
                    CardEntity(
                        userName = userName,
                        cardName = cardName,
                        cardNumber = cardNumber,
                        expiryMonth = expiryMonth,
                        expiryYear = expiryYear,
                        cvv = cvv,
                    )
                )
            }.onSuccess {
                _addCardSuccess.value = true
            }.onFailure {
                handleErrorMsg(it)
            }
        }
    }
}