package br.tcc.cadastra.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.tcc.cadastra.data.entity.CelulaEntity
import br.tcc.cadastra.data.entity.ParticipanteEntity
import br.tcc.cadastra.data.repository.CelulaRepository
import br.tcc.cadastra.data.repository.ParticipanteRepository
import br.tcc.cadastra.data.dao.MetricaFunil
import br.tcc.cadastra.data.session.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ParticipanteViewModel(
    private val participanteRepository: ParticipanteRepository,
    val celulaRepository: CelulaRepository
) : ViewModel() {

    val todasCelulas: StateFlow<List<CelulaEntity>> = celulaRepository.todasCelulas
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())

    val todosParticipantes: StateFlow<List<ParticipanteEntity>> = participanteRepository.listarParticipantesDoUsuarioAtivo()
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())

    val metricasFunil: StateFlow<List<MetricaFunil>> = participanteRepository.obterMetricasFunilDoUsuarioAtivo()
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())

    private val _participanteEmEdicao = MutableStateFlow<ParticipanteEntity?>(null)
    val participanteEmEdicao: StateFlow<ParticipanteEntity?> = _participanteEmEdicao

    fun iniciarEdicao(participante: ParticipanteEntity) {
        _participanteEmEdicao.value = participante
    }

    fun limparEdicao() {
        _participanteEmEdicao.value = null
    }

    fun salvarParticipante(
        idLocal: Long,
        nome: String,
        telefone: String,
        temWhatsapp: Boolean,
        estagioFunil: String,
        bairro: String,
        cidade: String,
        dataNascimento: String,
        participaIgreja: Boolean,
        qualIgreja: String,
        vezesVisitadas: String,
        comoConheceu: String,
        acompanhantes: String,
        apelo: String,
        uGroupIndicado: String,
        idRemoto: Long?
    ) {
        viewModelScope.launch {
            val usuarioId = UserSessionManager.usuarioAtivoId.value ?: 0
            val usuarioIdEfetivo = if (usuarioId == 0L) null else usuarioId

            val participante = ParticipanteEntity(
                idLocal = idLocal,
                usuarioLocalId = usuarioIdEfetivo,
                nome = nome,
                telefone = telefone,
                temWhatsapp = temWhatsapp,
                estagioFunil = estagioFunil,
                bairro = bairro,
                cidade = cidade,
                dataNascimento = dataNascimento,
                participaIgreja = participaIgreja,
                qualIgreja = qualIgreja,
                vezesVisitadas = vezesVisitadas,
                comoConheceu = comoConheceu,
                acompanhantes = acompanhantes,
                apelo = apelo,
                uGroupIndicado = uGroupIndicado,
                dataCriacao = System.currentTimeMillis(),
                sincronizado = false,
                idRemoto = idRemoto
            )
            participanteRepository.salvarParticipanteCompleto(participante)
        }
    }

    fun encaminharParticipante(participante: ParticipanteEntity) {
        viewModelScope.launch {
            participanteRepository.evoluirEstagioParticipante(participante, "ENCAMINHADO")
        }
    }

    fun carregarCelulasDoCsv(linhasCsv: List<String>) {
        viewModelScope.launch {
            val listaCelulas = mutableListOf<CelulaEntity>()
            for (linha in linhasCsv) {
                if (linha.isBlank() || linha.startsWith("nome", ignoreCase = true)) continue
                val colunasTratadas = linha.split(",")
                if (colunasTratadas.size >= 5) {
                    val celula = CelulaEntity(
                        nome = colunasTratadas[0].trim(),
                        lider = colunasTratadas[1].trim(),
                        whatsappLider = colunasTratadas[2].trim(),
                        anfitriao = colunasTratadas[3].trim(),
                        whatsappAnfitriao = colunasTratadas[4].trim(),
                        bairro = if (colunasTratadas.size > 5) colunasTratadas[5].trim() else ""
                    )
                    listaCelulas.add(celula)
                }
            }
            if (listaCelulas.isNotEmpty()) celulaRepository.salvarCelulas(listaCelulas)
        }
    }
}