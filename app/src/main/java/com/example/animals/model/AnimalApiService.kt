package com.example.animals.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class AnimalApiService {
    private  val BASE_URL = "https://us-central1-apis-4674e.cloudfunctions.net"

    private  val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())   //used to convert the returned json into objects
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //used to convert the objects into observables to be consumed
            .build()
            .create(AnimalAPI::class.java)

    fun getAPIKey(): Single<ApiKey> {
        return api.getAPIKey()
    }

    fun getAnimals(key: String) : Single<List<Animal>> {
        return api.getAnimals(key)
    }
}