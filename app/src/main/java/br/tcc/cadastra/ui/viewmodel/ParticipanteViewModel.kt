package br.tcc.cadastra.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.tcc.cadastra.data.entity.CelulaEntity
import br.tcc.cadastra.data.entity.ParticipanteEntity
import br.tcc.cadastra.data.repository.CelulaRepository
import br.tcc.cadastra.data.repository.ParticipanteRepository
import br.tcc.cadastra.data.dao.MetricaFunil
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ParticipanteViewModel(
    private val participanteRepository: ParticipanteRepository,
    private val celulaRepository: CelulaRepository
) : ViewModel() {

    val todasCelulas: StateFlow<List<CelulaEntity>> = celulaRepository.todasCelulas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todosParticipantes: StateFlow<List<ParticipanteEntity>> = participanteRepository.listarParticipantesDoUsuarioAtivo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val participantesEmAcompanhamento: StateFlow<List<ParticipanteEntity>> = todosParticipantes
        .combine(todosParticipantes) { lista, _ ->
            lista.filter { it.estagioFunil == "ACOMPANHAMENTO" }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val metricasFunil: StateFlow<List<MetricaFunil>> = participanteRepository.obterMetricasFunilDoUsuarioAtivo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun salvarParticipante(nome: String, estagioFunil: String) {
        viewModelScope.launch {
            participanteRepository.salvarParticipanteLocal(nome, estagioFunil)
        }
    }

    fun encaminharParticipante(participante: ParticipanteEntity) {
        viewModelScope.launch {
            participanteRepository.salvarParticipanteLocal(participante.nome, "ENCAMINHADO")
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

            if (listaCelulas.isNotEmpty()) {
                celulaRepository.salvarCelulas(listaCelulas)
            }
        }
    }
}
