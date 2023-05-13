package com.example.homework1.presentation.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.homework1.databinding.ItemCityBinding
import com.example.homework1.domain.model.DetailModel
import com.example.homework1.domain.model.ListModel

class CityAdapter(
    private var listWeather: ListModel,
    private val glide: RequestManager,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<CityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder = CityHolder(
        binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        glide = glide,
        onItemClick = onItemClick
    )

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.onBind(listWeather.listModel[position])
    }

    override fun getItemCount(): Int = listWeather.listModel.size
}
