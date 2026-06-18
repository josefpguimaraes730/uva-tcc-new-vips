package br.tcc.cadastra.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.tcc.cadastra.data.dao.MetricaFunil
import br.tcc.cadastra.data.entity.ParticipanteEntity
import br.tcc.cadastra.data.repository.ParticipanteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ParticipanteViewModel(
    private val participanteRepository: ParticipanteRepository
) : ViewModel() {

    val participantes: StateFlow<List<ParticipanteEntity>> =
        participanteRepository.listarParticipantesDoUsuarioAtivo()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val metricasFunil: StateFlow<List<MetricaFunil>> =
        participanteRepository.obterMetricasFunilDoUsuarioAtivo()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun cadastrarNovoParticipante(nome: String, estagioFunil: String) {
        viewModelScope.launch {
            participanteRepository.salvarParticipanteLocal(nome, estagioFunil)
        }
    }

    fun executarLimpezaMemoriaSincronizada() {
        viewModelScope.launch {
            participanteRepository.rodarExpurgoDeDadosSincronizados()
        }
    }
}