package com.example.gamepikachu

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.gamepikachu.databinding.ItemPikachuBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PikachuAdapter : Adapter<PikachuAdapter.ViewHolder>() {
    var pikachus = arrayListOf<Pikachu>()
    var index = -1
    var isSelect = false
    var pikachuSelected = Pikachu(0, 0)
    var isFinish: ((Boolean) -> Unit)? = null
    var isCorrect: (() -> Unit)? = null

    class ViewHolder(val v: ItemPikachuBinding) : RecyclerView.ViewHolder(v.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PikachuAdapter.ViewHolder {
        val v = ItemPikachuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: PikachuAdapter.ViewHolder,
        @SuppressLint("RecyclerView") position: Int,
    ) {
        holder.v.imgPikachu.setImageResource(pikachus[position].image)

        holder.itemView.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                if (index != position) {
                    if (isSelect) {
                        isSelect = false
                        if (pikachuSelected.type == pikachus[position].type) {
                            pikachus[position].disable = true
                            pikachus[index].disable = true
                            isCorrect?.invoke()
                            if (isFinish()) {
                                isFinish?.invoke(true)
                            }
                        } else {
                            index = -1
                        }
                    } else {
                        isSelect = true
                        pikachuSelected = pikachus[position]
                        index = position
                    }
                } else {
                    index = -1
                    isSelect = false
                }
                notifyDataSetChanged()
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            if (pikachus[position].disable) {
                holder.v.bgPikachu.visibility = View.INVISIBLE
                holder.v.bgPikachu.isEnabled = false
            } else {
                holder.v.bgPikachu.visibility = View.VISIBLE
                holder.v.bgPikachu.isEnabled = true
            }
        }

        if (index == position) {
            holder.v.bgPikachu.let {
                it.setCardBackgroundColor(it.context.getColor(R.color.bg_select))
            }
        } else {
            holder.v.bgPikachu.let {
                it.setCardBackgroundColor(it.context.getColor(R.color.white))
            }
        }
    }

    private fun isFinish(): Boolean {
        var finish = true
        pikachus.forEach {
            if (!it.disable) {
                finish = false
            }
        }
        return finish
    }

    override fun getItemCount(): Int = pikachus.size
}