package com.example.homework1.presentation.ui

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.homework1.App
import com.example.homework1.R
import com.example.homework1.databinding.FragmentInfoBinding
import com.example.homework1.databinding.FragmentSearchingBinding
import com.example.homework1.presentation.rv.CityAdapter
import com.example.homework1.presentation.viewmodel.SearchViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import timber.log.Timber
import javax.inject.Inject

class SearchingFragment : Fragment() {

    private var binding: FragmentSearchingBinding? = null
    private lateinit var client: FusedLocationProviderClient
    private val long: Double = 104.2964
    private val lat: Double = 52.2978
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private var recyclerView: RecyclerView? = null
    private var searchView: SearchView? = null
    private val viewModel: SearchViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initObservers()
        context?.also{
            client = LocationServices.getFusedLocationProviderClient(it)
        }
        return inflater.inflate(R.layout.fragment_searching, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSearchingBinding.bind(view)
        recyclerView = binding?.rv
        searchView = binding?.searchView
        getLastLocation()
        setOnSearchViewClickListener()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setOnSearchViewClickListener() {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                lifecycleScope.launch{
                    try {
                        viewModel.getWeather(query)
                    } catch (exception: Exception) {
                        withContext(Dispatchers.Main){
                            Snackbar.make(
                                requireView(),
                                "City was not found",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
//                onCityItemClick(query)
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
                    viewModel.getWeatherList(location.latitude, location.longitude)
                } else {
                    Snackbar.make(
                        requireView(),
                        "Your location is not available",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    viewModel.getWeatherList(lat, long)
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
            viewModel.getWeatherList(lat, long)
        }
    }

    private fun initObservers() {
        viewModel.weather.observe(viewLifecycleOwner) {
            it.fold(
                onSuccess = { detailModel ->
                    onCityItemClick(detailModel.name)
                },
                onFailure = { Timber.e("error") })
        }
        viewModel.weatherList.observe(viewLifecycleOwner) {
            it.fold(
                onSuccess = { listModel ->
                recyclerView?.run {
                    lifecycleScope.launch {
                        adapter = CityAdapter(
                            listModel,
                            glide = Glide.with(this@SearchingFragment),
                            onItemClick = ::onCityItemClick
                        )
                    }
                }
            },
                onFailure = { Timber.e("error") })
        }
    }
}
