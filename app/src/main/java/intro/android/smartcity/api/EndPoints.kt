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
    fun getProblemasById(@Path("id") id: String?): Call<List<Problema>>

    @FormUrlEncoded
    @POST("/smartcity/api/utilizador_login")
    fun login(@Field("username") username: String?, @Field("password") password: String?): Call<OutputLogin>

    @FormUrlEncoded
    @POST("/smartcity/api/problema_post")
    fun reportar(@Field("latitude") latitude: String?,
                 @Field("longitude") longitude: String?,
                 @Field("tipo") tipo: String?,
                 @Field("descricao") descricao: String?,
                 @Field("imagem") imagem: String?,
                 @Field("utilizador_id") utilizador_id: Int?): Call<OutputReportar>

    @FormUrlEncoded
    @POST("/smartcity/api/problema_put/{id}")
    fun editar(@Path("id") id: String?,
               @Field("latitude") latitude: String?,
               @Field("longitude") longitude: String?,
               @Field("tipo") tipo: String?,
               @Field("descricao") descricao: String?,
               @Field("imagem") imagem: String?,
               @Field("utilizador_id") utilizador_id: Int?): Call<OutputEditar>


    @POST("/smartcity/api/problema_delete/{id}")
    fun apagar(@Path("id") id: String?): Call<OutputApagar>

}