package br.tcc.cadastra.ui.screens

import androidx.compose.foundation.clickable // CORRIGIDO: Adicionado o import do Modificador de Clique
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    val estagiosObrigatorios = listOf("TRIAGEM", "ACOMPANHAMENTO", "ENCAMINHADO")

    Row(
        modifier = modifier.fillMaxWidth(), 
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        estagiosObrigatorios.forEach { estagio ->
            val quantidade = metricas.find { it.estagio == estagio }?.quantidade ?: 0
            
            CardMetrica(
                modifier = Modifier.weight(1f), 
                titulo = estagio, 
                valor = quantidade.toString()
            )
        }
    }
}

@Composable
private fun CardMetrica(modifier: Modifier = Modifier, titulo: String, valor: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp), 
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = titulo, 
                style = MaterialTheme.typography.labelSmall, 
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = valor, 
                style = MaterialTheme.typography.titleLarge, 
                fontWeight = FontWeight.Bold, 
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun ItemParticipante(
    modifier: Modifier = Modifier,
    participante: ParticipanteEntity,
    onItemClick: (ParticipanteEntity) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onItemClick(participante) }, // CORRIGIDO: Agora resolve com o import correto do foundation
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