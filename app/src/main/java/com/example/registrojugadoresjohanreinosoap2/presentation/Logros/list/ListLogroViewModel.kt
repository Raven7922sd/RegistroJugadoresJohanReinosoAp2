package edu.ucne.registrojugadores.Presentation.Logros.List

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.logroUseCase.DeleteLogroUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.logroUseCase.ObserveLogroUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListLogroViewModel @Inject constructor(
    private val observeLogroUseCase: ObserveLogroUseCase,
    private val deleteLogroUseCase: DeleteLogroUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ListLogroUiState(isLoading = true))
    val state: StateFlow<ListLogroUiState> = _state.asStateFlow()

    init {
        onEvent(ListLogroUiEvent.Load)
    }

    fun onEvent(event: ListLogroUiEvent){
        when(event){
            ListLogroUiEvent.Load -> observeLogro()
            is ListLogroUiEvent.Delete -> onDelete(event.id)
            ListLogroUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is ListLogroUiEvent.Edit ->  _state.update { it.copy(navigateToEditId = event.id) }
            is ListLogroUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
        }
    }

    private fun observeLogro(){
        viewModelScope.launch {
            observeLogroUseCase().collectLatest { logros ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        logros = logros,
                        message = null
                    )
                }
            }
        }
    }

    private fun onDelete(id: Int){
        viewModelScope.launch {
            try {
                deleteLogroUseCase(id)
                onEvent(ListLogroUiEvent.ShowMessage("Logro eliminado"))
            }catch (e: Exception){
                onEvent(ListLogroUiEvent.ShowMessage("Error al eliminar: ${e.message}"))
            }
        }
    }

    fun onNavigationHandled(){
        _state.update {
            it.copy(
                navigateToCreate = false,
                navigateToEditId = null,
                message = null
            )
        }
    }
}