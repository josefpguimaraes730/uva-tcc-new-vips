package br.tcc.cadastra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.tcc.cadastra.ui.screens.DashboardScreen
import br.tcc.cadastra.ui.theme.NewVipsAppTheme
import br.tcc.cadastra.ui.viewmodel.ParticipanteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as MainApplication
        val participanteRepository = app.dataModule.participanteRepository

        val viewModel: ParticipanteViewModel by viewModels {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ParticipanteViewModel(participanteRepository) as T
                }
            }
        }

        setContent {
            NewVipsAppTheme {
                DashboardScreen(viewModel = viewModel)
            }
        }
    }
}
