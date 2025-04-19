package com.weberhsu.presentation.ui.cards.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.weberhsu.domain.usecase.CardUseCase
import com.weberhsu.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val useCase: CardUseCase
) : BaseViewModel() {

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean>
        get() = _updateSuccess

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean>
        get() = _deleteSuccess

    fun updateCardName(id: String, cardName: String) {
        viewModelScope.launch {
            runCatching {
                useCase.updateCardName(id, cardName)
            }.onSuccess {
                _updateSuccess.value = true
            }.onFailure {
                handleErrorMsg(it)
            }
        }
    }

    fun updateCardCvv(id: String, cvv: String) {
        viewModelScope.launch {
            runCatching {
                useCase.updateCardCvv(id, cvv)
            }.onSuccess {
                _updateSuccess.value = true
            }.onFailure {
                handleErrorMsg(it)
            }
        }
    }

    fun updateUserName(id: String, userName: String) {
        viewModelScope.launch {
            runCatching {
                useCase.updateCardUserName(id, userName)
            }.onSuccess {
                _updateSuccess.value = true
            }.onFailure {
                handleErrorMsg(it)
            }
        }
    }

    fun updateCardNumber(id: String, cardNumber: String) {
        viewModelScope.launch {
            runCatching {
                useCase.updateCardNumber(id, cardNumber)
            }.onSuccess {
                _updateSuccess.value = true
            }.onFailure {
                handleErrorMsg(it)
            }
        }
    }

    fun updateCardExpiryDate(id: String, month: String, year: String) {
        viewModelScope.launch {
            runCatching {
                useCase.updateCardExpiryDate(id, month, year)
            }.onSuccess {
                _updateSuccess.value = true
            }.onFailure {
                handleErrorMsg(it)
            }
        }
    }

    fun deleteCard(id: String) {
        viewModelScope.launch {
            runCatching {
                useCase.deleteCard(id)
            }.onSuccess {
                _deleteSuccess.value = true
            }.onFailure {
                handleErrorMsg(it)
            }
        }
    }

    fun isFavorite(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            runCatching {
                useCase.setFavorite(id, isFavorite)
            }.onSuccess {
                _updateSuccess.value = true
            }.onFailure {
                handleErrorMsg(it)
            }
        }
    }
}