package intro.android.smartcity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import intro.android.smartcity.NotasPessoais.NotasPessoais

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun notasPessoais(view: View) {
        val intent = Intent(this, NotasPessoais::class.java)
        startActivity(intent)
    }

    fun login_activity(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}
