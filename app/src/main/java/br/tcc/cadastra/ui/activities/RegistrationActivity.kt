package br.tcc.cadastra.ui.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import br.tcc.cadastra.MainApplication
import br.tcc.cadastra.ui.adapters.LocalPagerAdapter
import br.tcc.cadastra.ui.viewmodel.ParticipanteViewModel

class RegistrationActivity : FragmentActivity() {

    private lateinit var viewPager: ViewPager2

    val viewModel: ParticipanteViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = application as MainApplication
                return ParticipanteViewModel(
                    app.dataModule.participanteRepository,
                    app.dataModule.celulaRepository
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootLayout = LinearLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            orientation = LinearLayout.VERTICAL
        }

        viewPager = ViewPager2(this).apply {
            id = View.generateViewId()
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            adapter = LocalPagerAdapter(this@RegistrationActivity)
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        val bottomBarComposeView = ComposeView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setContent {
                var abaSelecionada by remember { mutableIntStateOf(0) }

                // Listener reativo bidirecional para atualizar o ícone aceso ao arrastar o dedo
                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        abaSelecionada = position
                    }
                })

                NavigationBar {
                    NavigationBarItem(
                        selected = abaSelecionada == 0,
                        onClick = {
                            abaSelecionada = 0
                            irParaCadastro()
                        },
                        icon = { Icon(Icons.Filled.AddCircle, contentDescription = "Cadastro") },
                        label = { Text("Cadastro") }
                    )

                    NavigationBarItem(
                        selected = abaSelecionada == 1,
                        onClick = {
                            abaSelecionada = 1
                            irParaDashboard()
                        },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Dashboard") },
                        label = { Text("Dashboard") }
                    )

                    // INCLUSÃO DA MUDANÇA: Aba de configurações e perfil local
                    NavigationBarItem(
                        selected = abaSelecionada == 2,
                        onClick = {
                            abaSelecionada = 2
                            irParaPerfil()
                        },
                        icon = { Icon(Icons.Filled.Settings, contentDescription = "Perfil") },
                        label = { Text("Perfil") }
                    )
                }
            }
        }

        rootLayout.addView(viewPager)
        rootLayout.addView(bottomBarComposeView)

        setContentView(rootLayout)
    }

    fun irParaCadastro() {
        viewPager.currentItem = 0
    }

    fun irParaDashboard() {
        viewPager.currentItem = 1
    }

    fun irParaPerfil() {
        viewPager.currentItem = 2
    }
}