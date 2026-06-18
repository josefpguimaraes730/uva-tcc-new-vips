package br.tcc.cadastra

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import br.tcc.cadastra.ui.activities.RegistrationActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        

        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
        

        finish()
    }
}