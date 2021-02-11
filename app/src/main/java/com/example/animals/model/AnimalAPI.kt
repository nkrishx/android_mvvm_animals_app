package com.example.animals.model

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AnimalAPI {
    @GET("getKey")
    fun getAPIKey() : Single<ApiKey> //single is an observable which either returns a single value or error, normally an observable can return multiple values, but single just returns one.

    @FormUrlEncoded //allows to use fields in post
    @POST("getAnimals")
    fun getAnimals(@Field("key") key:String) : Single<List<Animal>>
}