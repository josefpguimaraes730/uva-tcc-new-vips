package br.tcc.cadastra.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.tcc.cadastra.ui.screens.DashboardContent
import br.tcc.cadastra.ui.viewmodel.ParticipanteViewModel

class DashboardFragment : Fragment() {

    private lateinit var viewModel: ParticipanteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[ParticipanteViewModel::class.java]

        return ComposeView(requireContext()).apply {
            setContent {
                val listaParticipantes by viewModel.todosParticipantes.collectAsState()
                val listaMetricas by viewModel.metricasFunil.collectAsState()

                DashboardContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    participantes = listaParticipantes,
                    metricas = listaMetricas,
                    onMudarEstagio = { participanteClicado ->
                        viewModel.encaminharParticipante(participanteClicado)
                    }
                )
            }
        }
    }
}