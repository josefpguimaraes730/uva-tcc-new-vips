package br.tcc.cadastra.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun FormularioCadastroDialog(
    onDismiss: () -> Unit,
    onConfirm: (nome: String, estagio: String) -> Unit
) {
    var nomeInput by remember { mutableStateOf("") }
    var estagioSelecionado by remember { mutableStateOf("TRIAGEM") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Novo Participante",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = nomeInput,
                    onValueChange = { nomeInput = it },
                    label = { Text("Nome Completo") },
                    placeholder = { Text("Digite o nome do participante") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Estágio Inicial no Funil",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = estagioSelecionado == "TRIAGEM",
                                onClick = { estagioSelecionado = "TRIAGEM" },
                                role = Role.RadioButton
                            )
                    ) {
                        RadioButton(
                            selected = estagioSelecionado == "TRIAGEM",
                            onClick = { estagioSelecionado = "TRIAGEM" }
                        )
                        Text(
                            text = "Triagem",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = estagioSelecionado == "ATENDIMENTO",
                                onClick = { estagioSelecionado = "ATENDIMENTO" },
                                role = Role.RadioButton
                            )
                    ) {
                        RadioButton(
                            selected = estagioSelecionado == "ATENDIMENTO",
                            onClick = { estagioSelecionado = "ATENDIMENTO" }
                        )
                        Text(
                            text = "Atendimento",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nomeInput.isNotBlank()) {
                        onConfirm(nomeInput.trim(), estagioSelecionado)
                    }
                },
                enabled = nomeInput.isNotBlank()
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}