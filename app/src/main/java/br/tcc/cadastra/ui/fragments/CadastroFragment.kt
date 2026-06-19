package br.tcc.cadastra.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import br.tcc.cadastra.ui.activities.RegistrationActivity
import br.tcc.cadastra.ui.screens.FormularioCadastro // Importando o formulário completo baseado em Scaffold

class CadastroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, 
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val activityMae = requireActivity() as RegistrationActivity
        val viewModelCompartilhada = activityMae.viewModel

        return ComposeView(requireContext()).apply {
            setContent {
                val listaCelulasPorUsuario by viewModelCompartilhada.todasCelulas.collectAsState()
                // MODIFICAÇÃO: Observa reativamente se há um participante selecionado para edição
                val participanteParaEditar by viewModelCompartilhada.participanteEmEdicao.collectAsState()

                FormularioCadastro(
                    viewModel = viewModelCompartilhada,
                    participanteEdicao = participanteParaEditar,
                    listaCelulas = listaCelulasPorUsuario,
                    onCadastroSucesso = {
                        val mensagem = if (participanteParaEditar == null) {
                            "Participante guardado localmente!"
                        } else {
                            "Cadastro atualizado com sucesso!"
                        }
                        
                        Toast.makeText(requireContext(), mensagem, Toast.LENGTH_SHORT).show()
                        
                        // Limpa o estado para que o formulário não fique "preso" no modo edição
                        viewModelCompartilhada.limparEdicao()
                        
                        // Retorna o usuário ao Dashboard para ver as alterações
                        activityMae.irParaDashboard()
                    }
                )
            }
        }
    }

    // Caso o usuário saia da aba voluntariamente, opcionalmente limpamos a edição
    override fun onPause() {
        super.onPause()
        (requireActivity() as RegistrationActivity).viewModel.limparEdicao()
    }
}