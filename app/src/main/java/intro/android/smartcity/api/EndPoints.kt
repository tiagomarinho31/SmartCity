package intro.android.smartcity.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET ("/smartcity/api/utilizador")
    fun getUtilizadores(): Call<List<Utilizador>>

    @GET ("/smartcity/api/utilizador/{id}")
    fun getUtilizadorById(@Path("id") id:Int): Call<Utilizador>

    @GET ("/smartcity/api/problema")
    fun getProblemas(): Call<List<Problema>>

    @GET ("/smartcity/api/problema/{id}")
    fun getProblemasById(@Path("id") id: Int): Call<Problema>

    @FormUrlEncoded
    @POST("/smartcity/api/utilizador_login")
    fun login(@Field("username") username: String?, @Field("password") password: String?): Call<OutputLogin>

}