package intro.android.smartcity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import intro.android.smartcity.R

class Menu : AppCompatActivity() {

    private lateinit var shared_preferences: SharedPreferences
    private lateinit var textWelcome: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        shared_preferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        val username = shared_preferences.getString("username", "")
        textWelcome = findViewById(R.id.welcome)
        var welcome = R.string.welcome
        textWelcome.text = "$welcome " + "$username"
    }

    fun problemas_lista(view: View){
        val intent = Intent(this@Menu, Problema_List::class.java)
        startActivity(intent)
    }

    fun problemas_mapa(view: View){
        val intent = Intent(this@Menu, MapsActivity::class.java)
        startActivity(intent)
    }

    fun problemas_reportar(view: View){
        val intent = Intent(this@Menu, Problema_Reportar::class.java)
        startActivity(intent)
    }

    fun logout(view: View){
        val shared_preferences_edit : SharedPreferences.Editor = shared_preferences.edit()
        shared_preferences_edit.clear()
        shared_preferences_edit.apply()

        val intent = Intent(this@Menu, Login::class.java)
        startActivity(intent)
        finish()
    }
}