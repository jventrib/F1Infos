package com.jventrib.f1infos.race.ui

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jventrib.f1infos.R
import com.jventrib.f1infos.common.ui.customDateTimeFormatter
import com.jventrib.f1infos.databinding.FragmentRaceBinding
import com.jventrib.f1infos.race.model.Race
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


class RaceListAdapter internal constructor(
    val context: Context,
    private val listener: (Race) -> Unit
) : RecyclerView.Adapter<RaceListAdapter.RaceViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var races = emptyList<Race>()

    inner class RaceViewHolder(binding: FragmentRaceBinding) : RecyclerView.ViewHolder(binding.root) {
        val raceNameItemView: TextView = binding.nameTextView
        val raceDateItemView: TextView = binding.dateTextView
        val flagItemView: ImageView = binding.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceViewHolder {
        val binding = FragmentRaceBinding.inflate(inflater, parent, false)
        return RaceViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RaceViewHolder, position: Int) {
        val current = races.toList()[position]
        holder.raceNameItemView.text = current.raceName
        current.datetime?.let {
            holder.raceDateItemView.text =
                ZonedDateTime.ofInstant(it, ZoneId.systemDefault()).format(
                    customDateTimeFormatter
                )
            holder.raceDateItemView.typeface =
                if (it.isAfter(Instant.now())) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        }
        current.circuit.location.flag?.let {
            val s = "https://www.countryflags.io/$it/flat/64.png"
            Glide
                .with(context)
                .load(s)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.flagItemView)
        }
        holder.itemView.setOnClickListener { listener(current) }
    }


    internal fun setRaces(races: List<Race>) {
        this.races = races
        notifyDataSetChanged()
    }

    override fun getItemCount() = races.size
}