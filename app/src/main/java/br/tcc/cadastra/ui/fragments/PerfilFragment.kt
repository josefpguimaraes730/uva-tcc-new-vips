package br.tcc.cadastra.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import br.tcc.cadastra.data.entity.CelulaEntity
import br.tcc.cadastra.ui.activities.RegistrationActivity
import br.tcc.cadastra.ui.screens.TelaPerfilLocal
import br.tcc.cadastra.data.session.UserSessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class PerfilFragment : Fragment() {

    private lateinit var activityMae: RegistrationActivity

    private val seletorCsvLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            processarEImportarArquivo(uri)
        } else {
            Toast.makeText(requireContext(), "Nenhum arquivo selecionado.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activityMae = requireActivity() as RegistrationActivity
        val viewModelCompartilhada = activityMae.viewModel

        return ComposeView(requireContext()).apply {
            setContent {
                val participantes by viewModelCompartilhada.todosParticipantes.collectAsState()
                val idUsuarioLogado by UserSessionManager.usuarioAtivoId.collectAsState(initial = 0L)

                val filtradosPorSessaoEEstagio = participantes.filter { participante ->
                    val pertenceASessao = if (idUsuarioLogado == 0L || idUsuarioLogado == null) {
                        participante.usuarioLocalId == null
                    } else {
                        participante.usuarioLocalId == idUsuarioLogado
                    }
                    pertenceASessao && participante.estagioFunil == "ACOMPANHAMENTO"
                }

                TelaPerfilLocal(
                    modifier = Modifier.fillMaxSize(),
                    participantesEmAcompanhamento = filtradosPorSessaoEEstagio,
                    onImportarCsvClick = {
                        seletorCsvLauncher.launch("text/*")
                    },
                    onEncaminharParticipante = { participante ->
                        viewModelCompartilhada.encaminharParticipante(participante)
                        Toast.makeText(requireContext(), "${participante.nome} encaminhado com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private fun processarEImportarArquivo(uri: Uri) {
        Toast.makeText(requireContext(), "Processando arquivo...", Toast.LENGTH_SHORT).show()

        viewLifecycleOwner.lifecycleScope.launch {
            val resultado = withContext(Dispatchers.IO) {
                runCatching {
                    val novosGroups = mutableListOf<CelulaEntity>()
                    val inputStream = requireContext().contentResolver.openInputStream(uri) ?: return@runCatching 0
                    
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        val cabecalho = reader.readLine()
                        var linha = reader.readLine()
                        
                        while (linha != null) {
                            if (linha.isNotBlank()) {
                                val colunas = linha.split(",", ";")
                                if (colunas.isNotEmpty()) {
                                    val nomeGroup = colunas.getOrNull(0)?.trim()?.replace("\"", "") ?: ""
                                    
                                    if (nomeGroup.isNotBlank()) {
                                        val lider = colunas.getOrNull(1)?.trim()?.replace("\"", "") ?: ""
                                        val whatsappLider = colunas.getOrNull(2)?.trim()?.replace("\"", "") ?: ""
                                        val anfitriao = colunas.getOrNull(3)?.trim()?.replace("\"", "") ?: ""
                                        val whatsappAnfitriao = colunas.getOrNull(4)?.trim()?.replace("\"", "") ?: ""
                                        val bairro = colunas.getOrNull(5)?.trim()?.replace("\"", "") ?: ""

                                        novosGroups.add(
                                            CelulaEntity(
                                                id = 0,
                                                nome = nomeGroup,
                                                lider = lider,
                                                whatsappLider = whatsappLider,
                                                anfitriao = anfitriao,
                                                whatsappAnfitriao = whatsappAnfitriao,
                                                bairro = bairro
                                            )
                                        )
                                    }
                                }
                            }
                            linha = reader.readLine()
                        }
                    }

                    if (novosGroups.isNotEmpty()) {
                        activityMae.viewModel.celulaRepository.inserirListaCelulas(novosGroups)
                    }
                    novosGroups.size
                }
            }

            resultado.onSuccess { total ->
                if (total > 0) {
                    Toast.makeText(requireContext(), "$total uGroups importados com sucesso!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Nenhum registro válido encontrado no arquivo.", Toast.LENGTH_SHORT).show()
                }
            }.onFailure { erro ->
                Toast.makeText(requireContext(), "Erro ao processar estrutura: ${erro.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}