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
import br.tcc.cadastra.ui.screens.FormularioCadastro

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

                FormularioCadastro(
                    viewModel = viewModelCompartilhada,
                    listaCelulas = listaCelulasPorUsuario,
                    participanteEdicao = null,
                    onCadastroSucesso = {
                        Toast.makeText(
                            requireContext(), 
                            "Cadastro local processado com sucesso!", 
                            Toast.LENGTH_SHORT
                        ).show()

                        activityMae.irParaDashboard()
                    }
                )
            }
        }
    }
}