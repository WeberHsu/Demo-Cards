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
    private var originCards: List<CardEntity> = emptyList()

    fun getCards() {
        viewModelScope.launch {
            runCatching {
                useCase.getAllCard()
            }.onSuccess {
                event.value = useCase.getAllCard()
                originCards = event.value.orEmpty()
            }.onFailure {
                handleErrorMsg(it)
            }
        }
    }

    private fun getUpdatedCards(
        originalList: List<CardEntity>,
        newList: List<CardEntity>
    ): List<CardEntity> {
        return newList.mapIndexedNotNull { newIndex, card ->
            // 無論原來 sort 為何，只要與新位置 index 不一致，就要更新
            if (card.sort != newIndex) {
                card.copy(sort = newIndex)
            } else {
                null
            }
        }
    }

    fun updateSorts(data: List<CardEntity>) {
        getUpdatedCards(originCards, data).run {
            if (this.isNotEmpty()) {
                // changed
                viewModelScope.launch {
                    runCatching {
                        useCase.updateCards(this@run)
                    }.onSuccess {
                        originCards = data
                    }.onFailure {
                        handleErrorMsg(it)
                    }
                }
            }
        }
    }
}