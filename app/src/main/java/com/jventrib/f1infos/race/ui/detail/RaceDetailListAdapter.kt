package com.jventrib.f1infos.race.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.clear
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.commit451.coiltransformations.facedetection.CenterOnFaceTransformation
import com.jventrib.f1infos.common.ui.getColorWithAlpha
import com.jventrib.f1infos.databinding.ItemRaceResultBinding
import com.jventrib.f1infos.race.model.Race
import com.jventrib.f1infos.race.model.db.RaceResultFull

class RaceResultListAdapter internal constructor(
    val context: Context,
    private val listener: (Race, ItemRaceResultBinding) -> Unit
) : RecyclerView.Adapter<RaceResultListAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var raceResults = emptyList<RaceResultFull>()

    inner class ViewHolder(val binding: ItemRaceResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRaceResultBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = raceResults.toList()[position]
        holder.binding.textDriverName.text =
            "${current.raceResult.position}: ${current.driver.givenName} ${current.driver.familyName}"
        holder.binding.textConstructor.text = current.constructor.name
        holder.binding.textDriverPoints.text = current.raceResult.points.toString()

//        if (current.constructor.image != null && current.constructor.image != "NONE") {
//            holder.binding.textConstructor.loadBackground(current.constructor.image)
//        } else
//            holder.binding.textConstructor.background = null


        var colorId = context.resources.getIdentifier(
            current.constructor.id,
            "color",
            context.packageName
        )

        val color = context.resources.getString(colorId)
        val parseColor = Color.parseColor(color)
        val colorWithAlpha = getColorWithAlpha(parseColor, 0.9f)
        holder.binding.spaceConstructorColor.setBackgroundColor(parseColor)

        current.driver.image?.let {
            holder.binding.imageDriver.load(it) {
                transformations(
                    listOf(
                        CenterOnFaceTransformation(zoom = 80),
                        CircleCropTransformation()
                    )
                )
            }

        } ?: let {
            holder.binding.imageDriver.clear()
            holder.binding.imageDriver.setImageDrawable(null)
        }
//        ViewCompat.setTransitionName(holder.binding.root, "race_card${current.round}")
//        ViewCompat.setTransitionName(holder.binding.imageFlag, "race_image_flag${current.round}")
//        ViewCompat.setTransitionName(holder.binding.textRaceDate, "text_race_date${current.round}")

//        holder.itemView.setOnClickListener { listener(current, holder.binding) }
    }


    internal fun setRaceResult(list: List<RaceResultFull>) {
        this.raceResults = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = raceResults.size
}

private fun View.loadBackground(image: String?) {
    ImageRequest.Builder(this.context).data(image).target { this.background = it }.build().also {
        this.context.imageLoader.enqueue(it)
    }
}