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

    suspend fun salvarParticipanteCompleto(participante: ParticipanteEntity) = withContext(Dispatchers.IO) {
        participanteDao.inserirOuAtualizar(participante)
    }

    suspend fun evoluirEstagioParticipante(participante: ParticipanteEntity, novoEstagio: String) = withContext(Dispatchers.IO) {
        val participanteAtualizado = participante.copy(
            estagioFunil = novoEstagio,
            sincronizado = false
        )
        participanteDao.inserirOuAtualizar(participanteAtualizado)
    }

    fun listarParticipantesDoUsuarioAtivo(): Flow<List<ParticipanteEntity>> {
        return UserSessionManager.usuarioAtivoId.flatMapLatest { idUsuarioLogado ->
            val idBusca = idUsuarioLogado ?: 0L
            participanteDao.listarParticipantesPorUsuario(idBusca)
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
}