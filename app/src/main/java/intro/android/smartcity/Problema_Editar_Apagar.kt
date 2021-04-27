package intro.android.smartcity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import intro.android.smartcity.MapsActivity.Companion.PARAM_DESCRICAO
import intro.android.smartcity.MapsActivity.Companion.PARAM_ID
import intro.android.smartcity.MapsActivity.Companion.PARAM_TIPO

class Problema_Editar_Apagar : AppCompatActivity() {

    private lateinit var editTipoView: EditText
    private lateinit var editDescricaoView: EditText
    private lateinit var shared_preferences: SharedPreferences
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problema__editar__apagar)

        editTipoView = findViewById(R.id.edit_tipo)
        editDescricaoView = findViewById(R.id.edit_descricao)

        var id = intent.getStringExtra(PARAM_ID)
        var tipo = intent.getStringExtra(PARAM_TIPO)
        var descricao = intent.getStringExtra(PARAM_DESCRICAO)
        editTipoView.setText(tipo.toString())
        editDescricaoView.setText(descricao.toString())
        Log.d("****TIPO", tipo.toString())

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



        val button = findViewById<Button>(R.id.button_editar)
        button.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra(EDIT_ID, id)
            if (TextUtils.isEmpty(editTipoView.text)  || TextUtils.isEmpty(editDescricaoView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val edit_tipo = editTipoView.text.toString()
                replyIntent.putExtra(EDIT_TIPO, edit_tipo)
                val edit_descricao = editDescricaoView.text.toString()
                replyIntent.putExtra(EDIT_DESCRICAO, edit_descricao)
                val latitude = latitude
                replyIntent.putExtra(EDIT_LATITUDE, latitude)
                val longitude = longitude
                replyIntent.putExtra(EDIT_LONGITUDE, longitude)
                replyIntent.putExtra(STATUS, "EDIT")
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }

        val button_delete = findViewById<Button>(R.id.button_apagar)
        button_delete.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTipoView.text)  || TextUtils.isEmpty(editDescricaoView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(DELETE_ID, id)
                replyIntent.putExtra(STATUS, "DELETE")
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }

    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

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

    companion object {
        const val STATUS = ""
        const val DELETE_ID = "DELETE_ID"
        const val EDIT_ID = "EDIT_ID"
        const val EDIT_TIPO = "EDIT_TIPO"
        const val EDIT_DESCRICAO = "EDIT_DESCRICAO"
        const val EDIT_LATITUDE = "EDIT_LATITUDE"
        const val EDIT_LONGITUDE = "EDIT_LONGITUDE"
    }
}