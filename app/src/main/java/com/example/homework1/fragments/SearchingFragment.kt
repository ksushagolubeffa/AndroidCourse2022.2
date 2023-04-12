package com.example.homework1.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.homework1.R
import com.example.homework1.data.DataContainer
import com.example.homework1.databinding.FragmentSearchingBinding
import com.example.homework1.rv.CityAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class SearchingFragment : Fragment() {

    private var binding: FragmentSearchingBinding? = null
    private lateinit var client: FusedLocationProviderClient
    private val long: Double = 104.2964
    private val lat: Double = 52.2978
    private var recyclerView: RecyclerView? = null
    private var searchView: SearchView? = null
    private val api = DataContainer.weatherApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.also {
            client = LocationServices.getFusedLocationProviderClient(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_searching, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSearchingBinding.bind(view)
        recyclerView = view.findViewById(R.id.rv)
        searchView = view.findViewById(R.id.searchView)
        getLastLocation()
        setOnSearchViewClickListener()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setOnSearchViewClickListener() {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                lifecycleScope.launch {
                    try {
                        api.getWeather(query)
                        onCityItemClick(query)
                    } catch (exception: Exception) {
                        Snackbar.make(
                            requireView(),
                            "City was not found",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })
    }

    private fun onCityItemClick(name: String) {
        var bundle: Bundle?
        name.also {
            bundle = Bundle().apply {
                putString("CITY_NAME", name)
            }
        }
        findNavController().navigate(R.id.action_searchingFragment_to_infoFragment, bundle)
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        checkPermissions()
        client.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    rvCreator(location.latitude, location.longitude)
                } else {
                    Snackbar.make(
                        requireView(),
                        "Your location is not available",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    rvCreator(lat, long)
                }
            }
    }

    private fun checkPermissions() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            rvCreator(lat, long)
        }
    }

    private fun rvCreator(latitude: Double, longitude: Double) {
        recyclerView?.run {
            lifecycleScope.launch {
                adapter = CityAdapter(
                    api.getWeatherList(latitude = latitude, longitude = longitude, count = 10),
                    glide = Glide.with(this@SearchingFragment),
                    onItemClick = ::onCityItemClick
                )
            }
        }
    }
}
