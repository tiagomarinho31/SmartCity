package intro.android.smartcity

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import intro.android.smartcity.NotasPessoais.AddNotas
import intro.android.smartcity.api.EndPoints
import intro.android.smartcity.api.OutputLogin
import intro.android.smartcity.api.OutputReportar
import intro.android.smartcity.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Problema_Reportar : AppCompatActivity() {

    private lateinit var editTipoView: EditText
    private lateinit var editDescricaoView: EditText
    private lateinit var editImagemView: EditText
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problema__reportar)

        editTipoView = findViewById(R.id.tipo)
        editDescricaoView = findViewById(R.id.descricao)
        editImagemView = findViewById(R.id.imagem)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                lastLocation = p0?.lastLocation!!
                latitude = lastLocation.latitude
                longitude = lastLocation.longitude
            }
        }

        createLocationRequest()

    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    }

    fun reportar(view: View) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val latitude = latitude
        val longitude = longitude
        val tipo = editTipoView.text.toString()
        val descricao = editDescricaoView.text.toString()
        val imagem = editImagemView.text.toString()
        val utilizador_id = 1

        val call = request.reportar(
                latitude = latitude.toString(),
                longitude = longitude.toString(),
                tipo = tipo,
                descricao = descricao,
                imagem = imagem,
                utilizador_id = utilizador_id)

        call.enqueue(object : Callback<OutputReportar> {
            override fun onResponse(call: Call<OutputReportar>, response: Response<OutputReportar>){
                if (response.isSuccessful){
                    val c: OutputReportar = response.body()!!
                    Toast.makeText(this@Problema_Reportar, c.MSG, Toast.LENGTH_LONG).show()
                    val intent = Intent(this@Problema_Reportar, MapsActivity::class.java)
                    startActivity(intent);
                    finish()

                    }
                }
            override fun onFailure(call: Call<OutputReportar>, t: Throwable){
                Toast.makeText(this@Problema_Reportar,"${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

            return
        }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

    }
}