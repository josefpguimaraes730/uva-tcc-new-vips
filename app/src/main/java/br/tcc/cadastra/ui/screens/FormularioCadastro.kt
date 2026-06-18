package br.tcc.cadastra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.tcc.cadastra.data.entity.CelulaEntity
import br.tcc.cadastra.data.entity.ParticipanteEntity
import br.tcc.cadastra.ui.viewmodel.ParticipanteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCadastro(
    modifier: Modifier = Modifier,
    viewModel: ParticipanteViewModel,
    participanteEdicao: ParticipanteEntity? = null,
    listaCelulas: List<CelulaEntity> = emptyList(),
    onCadastroSucesso: () -> Unit
) {
    val scrollState = rememberScrollState()


    var nomeCompleto by remember { mutableStateOf(participanteEdicao?.nome ?: "") }
    var telefone by remember { mutableStateOf(participanteEdicao?.telefone ?: "") }
    var temWhatsapp by remember { mutableStateOf(participanteEdicao?.sincronizado ?: true) } 
    var bairro by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var participaIgreja by remember { mutableStateOf(false) }
    var qualIgreja by remember { mutableStateOf("") }
    var vezesVisitadas by remember { mutableStateOf("1") }


    val opcoesComoConheceu = listOf("Convite", "Rede Social / Internet", "Passando na Rua", "Outros")
    var dropdownExpandido by remember { mutableStateOf(false) }
    var comoConheceuSelecionado by remember { mutableStateOf(opcoesComoConheceu[0]) }


    val opcoesAcompanhantes = listOf("Sozinho", "Cônjuge", "Filhos", "Amigos", "Outros")
    var acompanhantesSelecionados by remember { mutableStateOf(setOf<String>()) }


    val opcoesApelo = listOf("Não", "Salvação", "Reconciliação", "Batismo")
    var dropdownApeloExpandido by remember { mutableStateOf(false) }
    var apeloSelecionado by remember { mutableStateOf(opcoesApelo[0]) }

    var dropdownuGroupExpandido by remember { mutableStateOf(false) }
    var uGroupSelecionado by remember { mutableStateOf("Nenhum") }



    val estagioCalculado = remember(participanteEdicao, apeloSelecionado, uGroupSelecionado) {
        if (participanteEdicao != null || apeloSelecionado != "Não" || uGroupSelecionado != "Nenhum") {
            "ACOMPANHAMENTO"
        } else {
            "TRIAGEM"
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (participanteEdicao == null) "Novo Cadastro" else "Completar Cadastro",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        if (participanteEdicao != null) {
            SuggestionChip(
                onClick = {},
                label = { Text("Estágio Atual: $estagioCalculado") }
            )
        }


        OutlinedTextField(
            value = nomeCompleto,
            onValueChange = { nomeCompleto = it },
            label = { Text("Nome Completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = participanteEdicao == null // Bloqueia o nome se for apenas para completar cadastro
        )

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = telefone,
                onValueChange = { telefone = it },
                label = { Text("Telefone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = temWhatsapp, onCheckedChange = { temWhatsapp = it })
                Text("WhatsApp", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = bairro,
                onValueChange = { bairro = it },
                label = { Text("Bairro") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = cidade,
                onValueChange = { cidade = it },
                label = { Text("Cidade") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        OutlinedTextField(
            value = dataNascimento,
            onValueChange = { dataNascimento = it },
            label = { Text("Data de Nascimento (DD/MM/AAAA)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        HorizontalDivider()


        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Já participa de alguma igreja?", modifier = Modifier.weight(1f))
            Switch(checked = participaIgreja, onCheckedChange = { participaIgreja = it })
        }

        if (participaIgreja) {
            OutlinedTextField(
                value = qualIgreja,
                onValueChange = { qualIgreja = it },
                label = { Text("Qual igreja?") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = vezesVisitadas,
            onValueChange = { vezesVisitadas = it },
            label = { Text("Vezes visitadas (Número)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )


        Text("Conexão e Espiritual", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)


        ExposedDropdownMenuBox(
            expanded = dropdownExpandido,
            onExpandedChange = { dropdownExpandido = !dropdownExpandido }
        ) {
            OutlinedTextField(
                value = comoConheceuSelecionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Como conheceu a igreja?") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpandido) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = dropdownExpandido, onDismissRequest = { dropdownExpandido = false }) {
                opcoesComoConheceu.forEach { opcao ->
                    DropdownMenuItem(text = { Text(opcao) }, onClick = { comoConheceuSelecionado = opcao; dropdownExpandido = false })
                }
            }
        }


        ExposedDropdownMenuBox(
            expanded = dropdownApeloExpandido,
            onExpandedChange = { dropdownApeloExpandido = !dropdownApeloExpandido }
        ) {
            OutlinedTextField(
                value = apeloSelecionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Atendeu apelo? Qual?") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownApeloExpandido) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = dropdownApeloExpandido, onDismissRequest = { dropdownApeloExpandido = false }) {
                opcoesApelo.forEach { item ->
                    DropdownMenuItem(text = { Text(item) }, onClick = { apeloSelecionado = item; dropdownApeloExpandido = false })
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = dropdownuGroupExpandido,
            onExpandedChange = { dropdownuGroupExpandido = !dropdownuGroupExpandido }
        ) {
            OutlinedTextField(
                value = uGroupSelecionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("uGroup Indicado (Célula)") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownuGroupExpandido) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = dropdownuGroupExpandido, onDismissRequest = { dropdownuGroupExpandido = false }) {
                DropdownMenuItem(text = { Text("Nenhum") }, onClick = { uGroupSelecionado = "Nenhum"; dropdownuGroupExpandido = false })
                listaCelulas.forEach { celula ->
                    DropdownMenuItem(text = { Text(celula.nome) }, onClick = { uGroupSelecionado = celula.nome; dropdownuGroupExpandido = false })
                }
            }
        }

        HorizontalDivider()

        Text("Acompanhantes", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                opcoesAcompanhantes.take(3).forEach { opcao ->
                    FilterChip(
                        selected = acompanhantesSelecionados.contains(opcao),
                        onClick = { acompanhantesSelecionados = if (acompanhantesSelecionados.contains(opcao)) acompanhantesSelecionados - opcao else acompanhantesSelecionados + opcao },
                        label = { Text(opcao) }
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                opcoesAcompanhantes.drop(3).forEach { opcao ->
                    FilterChip(
                        selected = acompanhantesSelecionados.contains(opcao),
                        onClick = { acompanhantesSelecionados = if (acompanhantesSelecionados.contains(opcao)) acompanhantesSelecionados - opcao else acompanhantesSelecionados + opcao },
                        label = { Text(opcao) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.salvarParticipante(
                    nome = nomeCompleto,
                    estagioFunil = estagioCalculado
                )
                onCadastroSucesso()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = nomeCompleto.isNotBlank() && telefone.isNotBlank()
        ) {
            Text(if (participanteEdicao == null) "Cadastrar Local" else "Confirmar Alterações")
        }
    }
}