package br.tcc.cadastra.ui.screens

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
fun DashboardScreen(viewModel: ParticipanteViewModel) {
    val listaParticipantes by viewModel.todosParticipantes.collectAsState()
    val metricas by viewModel.metricasFunil.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard VIP") }
            )
        }
    ) { paddingValues ->
        DashboardContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            participantes = listaParticipantes,
            metricas = metricas,
            onMudarEstagio = { participanteClicado ->
                viewModel.encaminharParticipante(participanteClicado)
            }
        )
    }
}

@Composable
fun DashboardContent(
    modifier: Modifier = Modifier,
    participantes: List<ParticipanteEntity>,
    metricas: List<MetricaFunil>,
    onMudarEstagio: (ParticipanteEntity) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = "Métricas Atuais do Funil",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        GridMetricasFunil(metricas = metricas)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Lista de Participantes Cadastrados",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (participantes.isEmpty()) {
            Text(
                text = "Nenhum participante cadastrado para este perfil local ainda.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(
                    items = participantes,
                    key = { it.nome }
                ) { participante ->
                    ItemParticipante(
                        participante = participante,
                        onItemClick = { participanteClicado ->
                            onMudarEstagio(participanteClicado)
                        }
                    )
                }
            }
        }
    }
}