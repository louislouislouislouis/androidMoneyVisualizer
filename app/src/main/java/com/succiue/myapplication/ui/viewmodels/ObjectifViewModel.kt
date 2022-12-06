package com.succiue.myapplication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.succiue.myapplication.data.repository.ObjectifRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ObjectifUiState(
    var amount: String
)


@HiltViewModel
class ObjectifViewModel @Inject constructor(private val repo: ObjectifRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(listOf<ObjectifUiState>())
    val uiState: StateFlow<List<ObjectifUiState>> = _uiState

    fun test() {
        viewModelScope.launch {
            Log.d("TEST", "dzazdzad")
            var test = repo.getObjectifs()
            test.collect { list ->
                val listUi = list.map {
                    ObjectifUiState(it.amount.toString())
                }
                _uiState.emit(listUi)
            }
            Log.d("TEST", "$test")
        }

    }

    fun test2() {
        viewModelScope.launch {
            Log.d("TEST", "")
            repo.put()
        }

    }
}
