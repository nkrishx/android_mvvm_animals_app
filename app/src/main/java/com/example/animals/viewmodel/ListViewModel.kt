package com.example.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.animals.model.Animal
import com.example.animals.model.AnimalApiService
import com.example.animals.model.ApiKey
import com.example.animals.util.SharedPreferenceHelper
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel(application:Application): AndroidViewModel(application) {

    val animals by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy { MutableLiveData<Boolean>()}
    val loading by lazy { MutableLiveData<Boolean>()}

    private val disposable = CompositeDisposable() //used to clear or dispose the observable after its life cycle ends
    private val apiService = AnimalApiService()
    private val prefs = SharedPreferenceHelper(getApplication())
    private var invalidAPIKey = false //used in a case such that they key is retrieved but is invalid, a flag variable

    fun refresh(){
        //getAnimals()
        //getKey()
        invalidAPIKey = false //resetting it everytime in refresh
        val key : String? = prefs.getAPIKey()
        if(key.isNullOrEmpty()){
            getKey()
        }else {
            getAnimals(key)
        }
        loading.value = true
    }

    fun getKey(){
        disposable.add(
                apiService.getAPIKey() //this gets the data from the backend and will take some amount of time, hence we need to do it in background
                        .subscribeOn(Schedulers.newThread()) //this does the above, tells run it in a new background thread
                        .observeOn(AndroidSchedulers.mainThread()) //once the background fethcing is done we want it in main thread and this does it
                        .subscribeWith(object : DisposableSingleObserver<ApiKey>() { //a single returns a single object or error, implemented in the member functions here
                            override fun onSuccess(key: ApiKey) {
                                if(key.key.isNullOrEmpty())
                                {
                                    loadError.value = true
                                    loading.value = false
                                }else {
                                    prefs.saveAPIKey(key.key)
                                    getAnimals(key.key)
                                }
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                                loadError.value = true
                                loading.value = false
                            }

                        })
        )
    }

    private fun getAnimals(key: String) {
        //previous implementation with local mock data
//        val a1 = Animal("Alligator")
//        val a2 = Animal("Bear")
//        val a3 = Animal("Cat")
//        val a4 = Animal("Dog")
//        val a5 = Animal("Elephant")

//        val animalList : ArrayList<Animal> = arrayListOf(a1,a2,a3,a4,a5)

//        animals.value = animalList
//        loadError.value = false
//        loading.value = false

        disposable.add(
                apiService.getAnimals(key)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<List<Animal>>() {
                            override fun onSuccess(animalList: List<Animal>) {
                                loadError.value = false
                                loading.value = false
                                animals.value = animalList
                            }

                            override fun onError(e: Throwable) {
                                if(!invalidAPIKey){
                                    invalidAPIKey = true
                                    getKey()
                                }else {
                                    e.printStackTrace()
                                    loading.value = false
                                    loadError.value = true
                                }
                            }

                        })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()    //clears the disposable on end of lifecycle to avoid memory leaks
    }
}