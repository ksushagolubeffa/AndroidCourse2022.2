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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.homework1.R
import com.example.homework1.databinding.FragmentSearchingBinding
import com.example.homework1.presentation.rv.CityAdapter
import com.example.homework1.presentation.viewmodel.SearchViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchingFragment : Fragment() {

    private var binding: FragmentSearchingBinding? = null
    private lateinit var client: FusedLocationProviderClient
    private val long: Double = 104.2964
    private val lat: Double = 52.2978
    private val viewModel: SearchViewModel by viewModels()
    private var recyclerView: RecyclerView? = null
    private var searchView: SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initObservers()
        context?.also {
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
                lifecycleScope.launch {
                    try {
                        viewModel.getWeather(query)
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.container,
                                InfoFragment.newInstance(query)
                            )
                            .addToBackStack(null)
                            .commit()
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
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container,
                            InfoFragment.newInstance(detailModel.name)
                        )
                        .addToBackStack(null)
                        .commit()
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
                                glide = Glide.with(this@SearchingFragment)
                            ){cityName ->
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.container,
                                        InfoFragment.newInstance(cityName)
                                    )
                                    .addToBackStack(null)
                                    .commit()
                            }
                        }
                    }
                },
                onFailure = { Timber.e("error") })
        }
    }

    companion object {
        fun newInstance() = SearchingFragment()
    }
}
