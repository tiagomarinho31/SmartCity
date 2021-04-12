package intro.android.smartcity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import intro.android.smartcity.adapters.ProblemaAdapter
import intro.android.smartcity.api.EndPoints
import intro.android.smartcity.api.Problema
import intro.android.smartcity.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class Problema_List : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problema_list)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getProblemas()
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerview_problema)

        call.enqueue(object : Callback<List<Problema>>{
            override fun onResponse(call: Call<List<Problema>>, response: Response<List<Problema>>){
                if (response.isSuccessful){
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@Problema_List)
                        adapter = ProblemaAdapter(response.body()!!)
                    }
                }
            }
            override fun onFailure(call: Call<List<Problema>>, t: Throwable){
                Toast.makeText(this@Problema_List,"${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getSingle(view: View){
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getProblemasById(1)

        call.enqueue(object : retrofit2.Callback<Problema>{
            override fun onResponse(call: Call<Problema>, response: Response<Problema>){
                if (response.isSuccessful){
                    val c: Problema = response.body()!!
                    Toast.makeText(this@Problema_List,c.tipo, Toast.LENGTH_SHORT).show()
                    }
                }
            override fun onFailure(call: Call<Problema>, t: Throwable){
                Toast.makeText(this@Problema_List,"${t.message}", Toast.LENGTH_SHORT).show()
            }
            })
    }


}