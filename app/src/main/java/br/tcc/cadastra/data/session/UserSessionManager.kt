package br.tcc.cadastra.data.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserSessionManager {
    
    private val _usuarioAtivoId = MutableStateFlow<Long?>(null)
    val usuarioAtivoId: StateFlow<Long?> = _usuarioAtivoId.asStateFlow()

    fun definirUsuarioAtivo(id: Long) {
        _usuarioAtivoId.value = id
    }

    fun obterId(): Long? {
        return _usuarioAtivoId.value
    }

    fun encerrarSessao() {
        _usuarioAtivoId.value = null
    }
}