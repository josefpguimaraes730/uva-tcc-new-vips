package br.tcc.cadastra.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.tcc.cadastra.ui.fragments.CadastroFragment
import br.tcc.cadastra.ui.fragments.DashboardFragment
import br.tcc.cadastra.ui.fragments.PerfilFragment // Certifique-se de que o Fragment do Perfil está criado

class LocalPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CadastroFragment()
            1 -> DashboardFragment()
            else -> PerfilFragment()
        }
    }
}
