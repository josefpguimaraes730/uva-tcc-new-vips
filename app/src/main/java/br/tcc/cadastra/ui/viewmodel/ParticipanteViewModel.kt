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

/**
 * ViewModel responsável por orquestrar o estado da tela de Dashboard e listagem.
 * Atua como ponte intermediária entre a interface (Compose) e a camada de dados (Repository).
 */
class ParticipanteViewModel(
    private val participanteRepository: ParticipanteRepository
) : ViewModel() {

    /**
     * Estado reativo contendo a lista de participantes vinculados APENAS ao usuário ativo.
     * Converte o Flow frio do Room em um StateFlow quente ligado ao ciclo de vida da UI.
     */
    val participantes: StateFlow<List<ParticipanteEntity>> =
        participanteRepository.listarParticipantesDoUsuarioAtivo()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    /**
     * Estado reativo contendo as agregações (métricas do funil) calculadas direto no SQLite.
     * Alimenta os cartões de contagem e gráficos do Dashboard em tempo real.
     */
    val metricasFunil: StateFlow<List<MetricaFunil>> =
        participanteRepository.obterMetricasFunilDoUsuarioAtivo()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    /**
     * Fluxo de Execução Assíncrona: Insere um novo participante no banco de dados local.
     * A execução roda dentro do escopo da ViewModel, garantindo que a operação seja concluída
     * mesmo se o utilizador girar o ecrã durante o processo.
     */
    fun cadastrarNovoParticipante(nome: String, estagioFunil: String) {
        viewModelScope.launch {
            participanteRepository.salvarParticipanteLocal(nome, estagioFunil)
        }
    }

    /**
     * Gatilho manual para teste ou rotina do algoritmo de otimização de memória.
     * Limpa os dados sincronizados locais mantendo apenas o teto de estabilidade (50 registros).
     */
    fun executarLimpezaMemoriaSincronizada() {
        viewModelScope.launch {
            participanteRepository.rodarExpurgoDeDadosSincronizados()
        }
    }
}