package br.tcc.cadastra.ui.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.viewModels
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

        val rootLayout = FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        viewPager = ViewPager2(this).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            adapter = LocalPagerAdapter(this@RegistrationActivity)
            
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        rootLayout.addView(viewPager)
        setContentView(rootLayout)
    }

    fun irParaDashboard() {
        viewPager.currentItem = 1
    }

    fun irParaCadastro() {
        viewPager.currentItem = 0
    }
}