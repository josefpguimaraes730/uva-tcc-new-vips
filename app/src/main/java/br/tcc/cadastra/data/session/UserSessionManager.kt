package br.tcc.cadastra.data.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserSessionManager {
    
    private val _usuarioAtivoId = MutableStateFlow<String?>(null)
    val usuarioAtivoId: StateFlow<String?> = _usuarioAtivoId.asStateFlow()

    fun definirUsuarioAtivo(id: String) {
        _usuarioAtivoId.value = id
    }

    fun obterIdObrigatorio(): String {
        return _usuarioAtivoId.value ?: throw IllegalStateException("Nenhum usuário ativo na sessão local.")
    }

    fun encerrarSessao() {
        _usuarioAtivoId.value = null
    }
}