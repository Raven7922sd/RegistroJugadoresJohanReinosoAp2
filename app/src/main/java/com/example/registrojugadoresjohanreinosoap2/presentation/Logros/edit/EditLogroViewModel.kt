package edu.ucne.registrojugadores.Presentation.Logros.Edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadoresjohanreinosoap2.domain.model.Logros.Logros
import com.example.registrojugadoresjohanreinosoap2.domain.repository.LogrosRepository.LogroRepository
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.logroUseCase.DeleteLogroUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.logroUseCase.GetLogroUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.logroUseCase.UpsertLogroCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditLogroViewModel @Inject constructor(
    private val getLogroUseCase: GetLogroUseCase,
    private val upsertLogroUseCase: UpsertLogroCase,
    private val deleteLogroUseCase: DeleteLogroUseCase,
    private val logroRepository: LogroRepository
) : ViewModel(){
    private val _state = MutableStateFlow(EditLogroUiState())
    val state: StateFlow<EditLogroUiState> = _state.asStateFlow()

    fun onEvent(event: EditLogroUiEvent){
        when(event){
            is EditLogroUiEvent.Load -> onLoad(event.id)
            is EditLogroUiEvent.Save -> onSave()
            is EditLogroUiEvent.Delete -> onDelete()
            is EditLogroUiEvent.NombreChanged -> onNombreChanged(event.value)
            is EditLogroUiEvent.DescripcionChanged -> onDescripcionChanged(event.value)
            is EditLogroUiEvent.esLogradoChanged -> onLogradoChanged(event.value)
        }
    }

    private fun onLoad(id: Int?){
        if(id == null || id == 0){
            _state.update { it.copy(isNew = true, LogroId = null) }
            return
        }
        viewModelScope.launch{
            val logro = getLogroUseCase(id)
            if(logro != null){
                _state.update {
                    it.copy(
                        isNew = false,
                        LogroId = logro.LogroId,
                        NombreLogro = logro.LogroNombre,
                        Descripcion = logro.Descripcion,
                        EsDesbloqueado = logro.EsDesbloqueado
                    )
                }
            }
        }
    }

    private fun onNombreChanged(nombre: String){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    NombreLogro = nombre,
                    nombreError = if(nombre.isBlank()) "El nombre es requerido" else null
                )
            }
        }
    }

    private fun onDescripcionChanged(descripcion: String){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    Descripcion = descripcion,
                    descripcionError = if(descripcion.isBlank()) "Descripcion requerida" else null
                )
            }
        }
    }

    private fun onLogradoChanged(logrado: Boolean){
        _state.update { it.copy(EsDesbloqueado = logrado) }
    }

    private fun onSave(){
        viewModelScope.launch {
            if(_state.value.NombreLogro.isBlank()){
                _state.update { it.copy(nombreError = "Nombre requerido") }
                return@launch
            }
            if(_state.value.Descripcion.isBlank()){
                _state.update { it.copy(descripcionError = "Descripcion requerida") }
                return@launch
            }
            _state.update { it.copy(isSaving = true) }

            try{
                val logro = Logros(
                    LogroId = _state.value.LogroId ?: 0,
                    LogroNombre = _state.value.NombreLogro,
                    Descripcion = _state.value.Descripcion,
                    EsDesbloqueado = _state.value.EsDesbloqueado
                )

                upsertLogroUseCase(logro)

                _state.update {
                    it.copy(
                        isSaving = false,
                        saved = true
                    )
                }

            }catch (e: Exception){
                _state.update {
                    it.copy(
                        isSaving = false,
                        nombreError = "Error al guardar: ${e.message}"
                    )
                }
            }
        }
    }
    private fun onDelete(){
        val id = _state.value.LogroId?: return

        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            try {
                deleteLogroUseCase(id)
                _state.update { it.copy(isDeleting = false, deleted = true) }
            }catch (e: Exception){
                _state.update {
                    it.copy(
                        isDeleting = false,
                        nombreError = "Error al eliminar: ${e.message}"
                    )
                }
            }
        }
    }
}