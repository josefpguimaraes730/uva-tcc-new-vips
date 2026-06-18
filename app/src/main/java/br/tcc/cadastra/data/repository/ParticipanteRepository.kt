package br.tcc.cadastra.data.repository

import br.tcc.cadastra.data.dao.MetricaFunil
import br.tcc.cadastra.data.dao.ParticipanteDao
import br.tcc.cadastra.data.entity.ParticipanteEntity
import br.tcc.cadastra.data.session.UserSessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class ParticipanteRepository(private val participanteDao: ParticipanteDao) {
    suspend fun salvarParticipanteLocal(nome: String, estagioFunil: String) = withContext(Dispatchers.IO) {
        val usuarioId = UserSessionManager.obterIdObrigatorio()
        
        val novoParticipante = ParticipanteEntity(
            usuarioLocalId = usuarioId,
            nome = nome,
            estagioFunil = estagioFunil,
            dataCriacao = System.currentTimeMillis(),
            sincronizado = false,
            idRemoto = null
        )
        
        participanteDao.inserirOuAtualizar(novoParticipante)
    }

    fun listarParticipantesDoUsuarioAtivo(): Flow<List<ParticipanteEntity>> {
        return UserSessionManager.usuarioAtivoId.flatMapLatest { idLogado ->
            if (idLogado != null) {
                participanteDao.listarParticipantesPorUsuario(idLogado)
            } else {
                flowOf(emptyList())
            }
        }
    }

    fun obterMetricasFunilDoUsuarioAtivo(): Flow<List<MetricaFunil>> {
        return UserSessionManager.usuarioAtivoId.flatMapLatest { idLogado ->
            if (idLogado != null) {
                participanteDao.obterMetricasFunilLocal(idLogado)
            } else {
                flowOf(emptyList())
            }
        }
    }

    suspend fun rodarExpurgoDeDadosSincronizados() = withContext(Dispatchers.IO) {
        val usuarioId = UserSessionManager.obterIdObrigatorio()
        participanteDao.executarExpurgoDadosSincronizados(usuarioId)
    }
}
