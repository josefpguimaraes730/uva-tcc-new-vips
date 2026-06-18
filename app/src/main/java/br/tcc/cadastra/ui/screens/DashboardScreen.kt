package br.tcc.cadastra.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.tcc.cadastra.data.dao.MetricaFunil
import br.tcc.cadastra.data.entity.ParticipanteEntity
import br.tcc.cadastra.ui.viewmodel.ParticipanteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: ParticipanteViewModel) {
    val listaParticipantes by viewModel.participantes.collectAsState()
    val metricas by viewModel.metricasFunil.collectAsState()
    
    var mostrarDialogoCadastro by remember { mutableStateOf(false) }
    
    var abaSelecionada by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard VIP") }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = abaSelecionada == 0,
                    onClick = { abaSelecionada = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
                    label = { Text("Painel") }
                )
                NavigationBarItem(
                    selected = abaSelecionada == 1,
                    onClick = { abaSelecionada = 1 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoCadastro = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Participante")
            }
        }
    ) { paddingValues ->
        DashboardContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            participantes = listaParticipantes,
            metricas = metricas
        )

        if (mostrarDialogoCadastro) {
            FormularioCadastroDialog(
                onDismiss = { mostrarDialogoCadastro = false },
                onConfirm = { nome, estagio ->
                    viewModel.cadastrarNovoParticipante(nome, estagio)
                    mostrarDialogoCadastro = false
                }
            )
        }
    }
}

@Composable
fun DashboardContent(
    modifier: Modifier = Modifier,
    participantes: List<ParticipanteEntity>,
    metricas: List<MetricaFunil>
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
                items(participantes) { participante ->
                    ItemParticipante(participante = participante)
                }
            }
        }
    }
}
