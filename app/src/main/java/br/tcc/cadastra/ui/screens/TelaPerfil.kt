package br.tcc.cadastra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.tcc.cadastra.data.entity.ParticipanteEntity

@Composable
fun TelaPerfilLocal(
    modifier: Modifier = Modifier,
    participantesEmAcompanhamento: List<ParticipanteEntity>,
    onImportarCsvClick: () -> Unit,
    onEncaminharParticipante: (ParticipanteEntity) -> Unit
) {
    var mostrarAcompanhamentos by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Configurações Globais (V1 Offline)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)


        Button(
            onClick = onImportarCsvClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Carregar uGroups (Arquivo TXT/CSV)")
        }


        OutlinedButton(
            onClick = { mostrarAcompanhamentos = !mostrarAcompanhamentos },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (mostrarAcompanhamentos) "Ocultar Acompanhamentos" else "Ver Acompanhamentos ativos")
        }

        if (mostrarAcompanhamentos) {
            HorizontalDivider()
            Text(
                text = "Lista de Acompanhamentos (Mudar para Encaminhado)",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (participantesEmAcompanhamento.isEmpty()) {
                Text("Nenhum participante está no estágio de acompanhamento atualmente.", style = MaterialTheme.typography.labelSmall)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(participantesEmAcompanhamento) { participante ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(participante.nome, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                    Text("Telefone: ${participante.telefone}", style = MaterialTheme.typography.labelSmall)
                                }
                                Button(
                                    onClick = { onEncaminharParticipante(participante) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Text("Encaminhar", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}