package com.example.homework1.presentation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.homework1.R
import com.example.homework1.databinding.FragmentSearchingBinding
import com.example.homework1.presentation.rv.CityAdapter
import com.example.homework1.presentation.viewmodel.DetailViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.Flowables
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class SearchingFragment : Fragment() {

    private var binding: FragmentSearchingBinding? = null
    private var searchDisposable: Disposable? = null
    private lateinit var client: FusedLocationProviderClient
    private var query: String = ""
    private val long: Double = 104.2964
    private val lat: Double = 52.2978

    //    private val viewModelList: SearchViewModel by inject()
    private val viewModel: DetailViewModel by inject()
    private var recyclerView: RecyclerView? = null

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
        binding?.run {
            etCity.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.onLoadClick(etCity.text.toString())
                }
                true
            }
            tiCity.setOnClickListener {
                if (query != "") {
                    parentFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.container,
                            InfoFragment.newInstance(query)
                        )
                        .addToBackStack(null)
                        .commit()
                }
            }

            searchDisposable = etCity.observeQuery()
                .filter { it.length > 2 }
                .debounce(500L, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    Log.e("SEARCH QUERY", it)
                    viewModel.onLoadClick(it)
                    query = it
                }, onError = {
//                    Log.e("Error", it.message.toString())
                })
        }
        getLastLocation()
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        checkPermissions()
        client.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    viewModel.onLoadClickList(location.latitude, location.longitude)
                } else {
                    Snackbar.make(
                        requireView(),
                        "Your location is not available",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    viewModel.onLoadClickList(lat, long)
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
            viewModel.onLoadClickList(lat, long)
        }
    }

    private fun EditText.observeQuery() =
        Flowables.create<String>(mode = BackpressureStrategy.LATEST) { emitter ->
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Not used
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    emitter.onNext(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {
                    // Not used
                }
            }
            addTextChangedListener(textWatcher)
        }

    private fun initObservers() {
        with(viewModel) {
            loading.observe(viewLifecycleOwner) {
                binding?.progress?.isVisible = it
            }
            weather.observe(viewLifecycleOwner) {
                if (it == null) return@observe
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.container,
                        InfoFragment.newInstance(it.name!!)
                    )
                    .addToBackStack(null)
                    .commit()
            }
            error.observe(viewLifecycleOwner) {
                if (it == null) return@observe
                Log.e("SearchingFragment", it.localizedMessage)
            }

            weatherList.observe(viewLifecycleOwner) {
                if (it == null) return@observe
                recyclerView?.run {
                    adapter = CityAdapter(
                        it,
                        glide = Glide.with(this@SearchingFragment)
                    ) { cityName ->
                        parentFragmentManager.beginTransaction()
                            .replace(
                                R.id.container,
                                InfoFragment.newInstance(cityName)
                            )
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchDisposable?.dispose()
    }

    companion object {
        fun newInstance() = SearchingFragment()
    }
}
