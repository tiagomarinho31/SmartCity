package intro.android.smartcity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import intro.android.smartcity.NotasPessoais.AddNotas
import intro.android.smartcity.NotasPessoais.EditNotas
import intro.android.smartcity.api.*
import intro.android.smartcity.entities.Notas
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var  problemas: List<Problema>
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val newProblemaEditarActivityRequestCode = 1
    private lateinit var shared_preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        shared_preferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getProblemas()
        var position: LatLng

        call.enqueue(object : Callback<List<Problema>> {
            override fun onResponse(call: Call<List<Problema>>, response: Response<List<Problema>>){
                if (response.isSuccessful){
                    problemas = response.body()!!
                    for (problema in problemas){
                        position = LatLng(problema.latitude.toDouble(), problema.longitude.toDouble())
                        mMap.addMarker(MarkerOptions().position(position).title(problema.id.toString()).snippet(problema.tipo + "-" + problema.descricao))
                    }
                }
            }
            override fun onFailure(call: Call<List<Problema>>, t: Throwable){
                Toast.makeText(this@MapsActivity,"${t.message}", Toast.LENGTH_LONG).show()
            }
        })


    }

    override fun onInfoWindowClick(p0: Marker?) {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call :Call <List<Problema>> = request.getProblemasById(p0!!.title)
        val utilizador_id = shared_preferences.getInt("id", 0)

        call.enqueue(object : Callback<List<Problema>> {
            override fun onResponse(call: Call<List<Problema>>, response: Response<List<Problema>>) {
                if(response.isSuccessful){
                    problemas = response.body()!!
                    for(problema in problemas){
                        if(problema.utilizador_id == utilizador_id){
                            Toast.makeText(this@MapsActivity, problema.descricao, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MapsActivity,Problema_Editar_Apagar::class.java)
                            intent.putExtra(PARAM_ID, problema.id.toString())
                            intent.putExtra(PARAM_TIPO, problema.tipo)
                            intent.putExtra(PARAM_DESCRICAO, problema.descricao)
                            intent.putExtra(PARAM_LATITUDE, problema.latitude)
                            intent.putExtra(PARAM_LONGITUDE, problema.longitude)
                            intent.putExtra(PARAM_LONGITUDE, problema.longitude)
                            intent.putExtra(PARAM_UTILIZADOR_ID, problema.utilizador_id.toString())
                            startActivityForResult(intent, newProblemaEditarActivityRequestCode)
                        }else{
                            Toast.makeText(this@MapsActivity,R.string.id_error_edit, Toast.LENGTH_LONG).show()
                        }

                    }
                }
            }
            override fun onFailure(call: Call<List<Problema>>, t: Throwable) {
                Toast.makeText(this@MapsActivity,"${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
        mMap.setOnInfoWindowClickListener(this)
        setUpMap()
    }

    private fun setUpMap() {
        if(ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

            return
        } else{
            //1
            mMap.isMyLocationEnabled = true

            //2
            fusedLocationClient.lastLocation.addOnSuccessListener(this) {location ->
                //3
                if(location != null){
                    lastLocation = location
                    //Toast.makeText(this@MapsActivity, lastLocation.toString(), Toast.LENGTH_SHORT).show()
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // EDITAR E APAGAR NOTA
        if (requestCode == newProblemaEditarActivityRequestCode && resultCode == Activity.RESULT_OK) {
            var id = data?.getStringExtra(Problema_Editar_Apagar.EDIT_ID)
            var id_delete = data?.getStringExtra(Problema_Editar_Apagar.DELETE_ID)
            var edit_tipo = data?.getStringExtra(Problema_Editar_Apagar.EDIT_TIPO).toString()
            var edit_descricao = data?.getStringExtra(Problema_Editar_Apagar.EDIT_DESCRICAO).toString()
            var edit_latitude = data?.getDoubleExtra(Problema_Editar_Apagar.EDIT_LATITUDE, 0.0).toString()
            var edit_longitude = data?.getDoubleExtra(Problema_Editar_Apagar.EDIT_LONGITUDE, 0.0).toString()
            val utilizador_id = shared_preferences.getInt("id", 0)

            if(data?.getStringExtra(Problema_Editar_Apagar.STATUS) == "EDIT"){
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.editar(
                    id = id,
                    latitude = edit_latitude,
                    longitude = edit_longitude,
                    tipo = edit_tipo,
                    descricao = edit_descricao,
                    imagem = "Imagem",
                    utilizador_id = utilizador_id)

                call.enqueue(object : Callback<OutputEditar> {
                    override fun onResponse(call: Call<OutputEditar>, response: Response<OutputEditar>){
                        if (response.isSuccessful){
                            val c: OutputEditar = response.body()!!
                            Toast.makeText(this@MapsActivity, c.MSG, Toast.LENGTH_LONG).show()
                            val intent = Intent(this@MapsActivity, MapsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<OutputEditar>, t: Throwable){
                        Toast.makeText(this@MapsActivity,"${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else if(data?.getStringExtra(Problema_Editar_Apagar.STATUS) == "DELETE"){

                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.apagar(
                    id = id_delete)
                Log.d("****DELETE", id_delete.toString())

                call.enqueue(object : Callback<OutputApagar> {
                    override fun onResponse(call: Call<OutputApagar>, response: Response<OutputApagar>){
                        if (response.isSuccessful){
                            val c: OutputApagar = response.body()!!
                            Toast.makeText(this@MapsActivity, c.MSG, Toast.LENGTH_LONG).show()
                            val intent = Intent(this@MapsActivity, MapsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<OutputApagar>, t: Throwable){
                        Toast.makeText(this@MapsActivity,"${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            if(data?.getStringExtra(EditNotas.STATUS) == "EDIT"){
                Toast.makeText(this, R.string.edit_problem_error, Toast.LENGTH_SHORT).show()
            } else if(data?.getStringExtra(EditNotas.STATUS) == "DELETE"){
                Toast.makeText(this, R.string.delete_problem_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val STATUS = ""
        const val DELETE_ID = "DELETE_ID"
        const val PARAM_ID = "PARAM_ID"
        const val PARAM_TIPO = "PARAM_TIPO"
        const val PARAM_DESCRICAO = "PARAM_DESCRICAO"
        const val PARAM_LATITUDE = "PARAM_LATITUDE"
        const val PARAM_LONGITUDE = "PARAM_LONGITUDE"
        const val PARAM_UTILIZADOR_ID = "PARAM_UTILIZADOR_ID"
    }

}