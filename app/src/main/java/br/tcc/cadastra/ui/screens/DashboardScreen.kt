package br.tcc.cadastra.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.tcc.cadastra.data.dao.MetricaFunil
import br.tcc.cadastra.data.entity.ParticipanteEntity
import br.tcc.cadastra.ui.viewmodel.ParticipanteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: ParticipanteViewModel,
    onItemClick: (ParticipanteEntity) -> Unit // Adicionado para permitir navegação se usado via Screen
) {
    val listaParticipantes by viewModel.todosParticipantes.collectAsState()
    val metricas by viewModel.metricasFunil.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard VIP") }
            )
        }
    ) { paddingValores ->
        DashboardContent(
            modifier = Modifier
                .padding(paddingValores)
                .fillMaxSize(),
            participantes = listaParticipantes,
            metricas = metricas,
            onMudarEstagio = { participante -> viewModel.encaminharParticipante(participante) },
            onItemClick = onItemClick // Repassa corretamente para o arquivo unificado de componentes
        )
    }
}