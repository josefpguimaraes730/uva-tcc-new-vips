package br.tcc.cadastra.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.tcc.cadastra.data.dao.MetricaFunil
import br.tcc.cadastra.data.entity.ParticipanteEntity

@Composable
fun GridMetricasFunil(modifier: Modifier = Modifier, metricas: List<MetricaFunil>) {
    // Especifica explicitamente que é uma lista de Strings para ajudar o compilador
    val estagiosObrigatorios: List<String> = listOf("TRIAGEM", "ACOMPANHAMENTO", "ENCAMINHADO")

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        estagiosObrigatorios.forEach { estagio ->
            // Busca o objeto correspondente dentro da lista de métricas
            val metricaCorrespondente = metricas.find { it.estagio == estagio }
            val quantidade = metricaCorrespondente?.quantidade ?: 0

            CardMetrica(
                modifier = Modifier.weight(1f),
                titulo = estagio,
                valor = quantidade.toString()
            )
        }
    }
}

@Composable
fun CardMetrica(modifier: Modifier = Modifier, titulo: String, valor: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = titulo, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = valor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
fun DashboardContent(
    modifier: Modifier = Modifier,
    participantes: List<ParticipanteEntity>,
    metricas: List<MetricaFunil>,
    onMudarEstagio: (ParticipanteEntity) -> Unit,
    onItemClick: (ParticipanteEntity) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Painel Geral",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        GridMetricasFunil(metricas = metricas)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Participantes Recentes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        if (participantes.isEmpty()) {
            Text(
                text = "Nenhum participante cadastrado nesta sessão local.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(participantes) { participante ->
                    CardParticipante(
                        participante = participante,
                        onItemClick = onItemClick // Resolvido: Repassando o evento
                    )
                }
            }
        }
    }
}

@Composable
fun CardParticipante(
    participante: ParticipanteEntity,
    onItemClick: (ParticipanteEntity) -> Unit // Resolvido: Adicionado parâmetro que faltava
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onItemClick(participante) }, 
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = participante.nome,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Estágio: ${participante.estagioFunil}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            if (!participante.sincronizado) {
                Text(
                    text = "Local",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}