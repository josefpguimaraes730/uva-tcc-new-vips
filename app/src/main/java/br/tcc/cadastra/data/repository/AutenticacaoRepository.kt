package br.tcc.cadastra.data.repository

import br.tcc.cadastra.data.dao.SessaoUsuarioDao
import br.tcc.cadastra.data.entity.SessaoUsuarioEntity
import br.tcc.cadastra.data.session.UserSessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AutenticacaoRepository(private val sessaoUsuarioDao: SessaoUsuarioDao) {

    suspend fun cadastrarOperadorLocal(usuario: SessaoUsuarioEntity) = withContext(Dispatchers.IO) {
        sessaoUsuarioDao.salvarUsuarioLocal(usuario)
    }

    suspend fun alternarUsuarioAtivo(usuarioId: String): Boolean = withContext(Dispatchers.IO) {
        val usuario = sessaoUsuarioDao.buscarUsuarioPorId(usuarioId)
        if (usuario != null) {
            val usuarioAtualizado = usuario.copy(dataUltimoAcesso = System.currentTimeMillis())
            sessaoUsuarioDao.salvarUsuarioLocal(usuarioAtualizado)
            
            UserSessionManager.definirUsuarioAtivo(usuarioId)
            true
        } else {
            false
        }
    }

    fun listarPerfisDisponiveisNoAparelho() = sessaoUsuarioDao.listarTodosOsPerfisLocais()
}
