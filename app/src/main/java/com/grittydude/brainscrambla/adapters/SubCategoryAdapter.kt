package com.grittydude.brainscrambla.adapters
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.grittydude.brainscrambla.R
import com.grittydude.brainscrambla.datasource.SubCategories

class SubCategoryAdapter(private var subCategories: ArrayList<SubCategories>): RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val subCategoryName: TextView = itemView.findViewById(R.id.sub_category_textview)
        val playButton : MaterialButton = itemView.findViewById(R.id.sub_play)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_category_card_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val text = subCategories[position]
        holder.subCategoryName.text = text.name
        val name = text.name
        holder.playButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("Sub", name)
           Navigation.findNavController(holder.itemView).navigate(R.id.action_subCategoryFragment_to_game,bundle)
        }
    }

    override fun getItemCount(): Int {
        return subCategories.size
    }


    fun listFilter(filterList: ArrayList<SubCategories>){
        subCategories = filterList
        notifyDataSetChanged()
    }
    }

