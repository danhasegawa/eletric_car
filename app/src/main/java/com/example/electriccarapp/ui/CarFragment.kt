@file:Suppress("DEPRECATION")

package com.example.electriccarapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.electriccarapp.R
import com.example.electriccarapp.data.CarsApi
import com.example.electriccarapp.data.local.CarRepository
import com.example.electriccarapp.domain.Carro
import com.example.electriccarapp.ui.adapter.CarAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarFragment : Fragment() {

    private lateinit var listaCarros: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noInternetImage: ImageView
    private lateinit var noInternetText: TextView
    private lateinit var carsApi: CarsApi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRetrofit()
        setupView(view)
    }

    override fun onResume() {
        super.onResume()
        if (checkForInternet(context)) {
            getAllCars()
        } else {
            emptyState()
        }
    }

    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://igorbag.github.io/cars-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        carsApi = retrofit.create(CarsApi::class.java)
    }

    private fun getAllCars() {
        carsApi.getAllCars().enqueue(object : Callback<List<Carro>> {
            override fun onResponse(call: Call<List<Carro>>, response: Response<List<Carro>>) {
                if (response.isSuccessful) {
                    progressBar.isVisible = false
                    noInternetImage.isVisible = false
                    noInternetText.isVisible = false

                    response.body()?.let {
                        setupList(it)
                    }
                } else {
                    Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Carro>>, t: Throwable) {
                Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun emptyState() {
        progressBar.isVisible = false
        listaCarros.isVisible = false
        noInternetImage.isVisible = true
        noInternetText.isVisible = true
    }

    private fun setupView(view: View) {
        view.apply {
            listaCarros = findViewById(R.id.rv_lista_carros)
            progressBar = findViewById(R.id.pb_loader)
            noInternetImage = findViewById(R.id.iv_empty_state)
            noInternetText = findViewById(R.id.tv_no_wifi)
        }
    }

    private fun setupList(lista: List<Carro>) {
        val carroAdapter = CarAdapter(lista)
        listaCarros.apply {
            isVisible = true
            adapter = carroAdapter
        }
        carroAdapter.carItemLister = { carro ->
            CarRepository(requireContext()).saveIfNotExist(carro)
        }
    }


    private fun checkForInternet(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }

        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }
}


//Utilizar o retrofit como abastracao do AsyncTask
//
//    private fun callService() {
//        val urlBase = "https://igorbag.github.io/cars-api/cars.json"
//        MyTask().execute(urlBase)
//        progressBar.isVisible = true
//    }
//    inner class MyTask : AsyncTask<String, String, String>() {
//
//        @Deprecated("Deprecated in Java")
//        override fun doInBackground(vararg url: String?): String {
//            var urlConnection: HttpURLConnection? = null
//
//            try {
//                val urlBase = URL(url[0])
//                urlConnection = urlBase.openConnection() as HttpURLConnection
//                urlConnection.connectTimeout = 60000
//                urlConnection.readTimeout = 60000
//                urlConnection.setRequestProperty("Accept", "application/json")
//
//                val responseCode = urlConnection.responseCode
//
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    var response = urlConnection.inputStream.bufferedReader().use { it.readText() }
//                    publishProgress(response)
//                } else {
//                    Log.e("Erro", "Servico indisponivel no momento .....")
//                }
//
//            } catch (_: Exception) {
//
//            } finally {
//                urlConnection?.disconnect()
//            }
//            return ""
//        }
//
//        @Deprecated("Deprecated in Java")
//        override fun onProgressUpdate(vararg values: String?) {
//            try {
//                val jsonArray = JSONTokener(values[0]).nextValue() as JSONArray
//
//                for (i in 0 until jsonArray.length()) {
//                    val id = jsonArray.getJSONObject(i).getString("id")
//                    val preco = jsonArray.getJSONObject(i).getString("preco")
//                    val bateria = jsonArray.getJSONObject(i).getString("bateria")
//                    val potencia = jsonArray.getJSONObject(i).getString("potencia")
//                    val recarga = jsonArray.getJSONObject(i).getString("recarga")
//                    val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")
//
//                    val model = Carro(
//                        id = id.toInt(),
//                        preco = preco,
//                        bateria = bateria,
//                        potencia = potencia,
//                        recarga = recarga,
//                        urlPhoto = urlPhoto
//                    )
//                    carrosArray.add(model)
//                }
//                progressBar.isVisible = false
//                noInternetImage.isVisible = false
//                noInternetText.isVisible = false
//                //setupList()
//
//            } catch (ex: Exception) {
//                Log.e("Erro ->", ex.message.toString())
//            }
//        }
//    }

