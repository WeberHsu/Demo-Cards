package com.weberhsu.presentation.ui.cards.viewmodel

import androidx.lifecycle.viewModelScope
import com.weberhsu.domain.entity.CardEntity
import com.weberhsu.domain.usecase.CardUseCase
import com.weberhsu.presentation.widget.SingleLiveEvent
import com.weberhsu.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val useCase: CardUseCase
): BaseViewModel() {

    val event = SingleLiveEvent<List<CardEntity>>()

    fun getCards() {
        viewModelScope.launch {
            runCatching {
                useCase.getAllCard()
            }.onSuccess {
                event.value = useCase.getAllCard()
            }.onFailure {
                handleErrorMsg(it)
            }
        }
    }
}