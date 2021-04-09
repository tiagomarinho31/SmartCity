package intro.android.smartcity.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET ("/smartcity/api/utilizador/")
    fun getUtilizadores(): Call<List<Utilizador>>

    @GET ("/smartcity/api/utilizador/{id}")
    fun getUtilizadorById(): Call<Utilizador>

    @GET ("/smartcity/api/problema")
    fun getProblemas(): Call<List<Problema>>

    @GET ("/smartcity/api/problema/{id}")
    fun getProblemaById(): Call<Problema>

}