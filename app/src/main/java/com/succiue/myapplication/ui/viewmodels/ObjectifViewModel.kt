package com.succiue.myapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.data.model.Objectif
import com.succiue.myapplication.data.repository.DefaultObjectifRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*


data class ObjectifUiState(
    var amount: String
)


@AssistedFactory
interface ObjectifViewModelFactory {
    fun create(user: KichtaUserModel): ObjectifViewModel
}

class ObjectifViewModel @AssistedInject constructor(
    @Assisted var user: KichtaUserModel,
) : ViewModel() {
    private val repo = DefaultObjectifRepository(user)
    private val _uiState = MutableStateFlow(listOf<ObjectifUiState>())
    val uiState: StateFlow<List<ObjectifUiState>> = _uiState

    fun test() {
        viewModelScope.launch {
            var test = repo.getObjectifs()
            test.collect { list ->
                val listUi = list.map {
                    ObjectifUiState(it.amount.toString())
                }
                _uiState.emit(listUi)
            }
        }

    }

    fun test2() {
        viewModelScope.launch {
            repo.put(
                listOf(
                    Objectif(
                        "1",
                        user.idKichta,
                        Date(),
                        Date(),
                        listOf("Test"),
                        1.0
                    )
                )
            )
        }

    }
}
