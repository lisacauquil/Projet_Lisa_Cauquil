package com.example.myapplication.ui.accueil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class PassionAdapter(
    private val passions: List<String>,
    private val onCheckChanged: (String, Boolean) -> Unit
) : RecyclerView.Adapter<PassionAdapter.PassionViewHolder>() {

    private var preSelected: Set<String> = emptySet()

    fun setPreSelectedPassions(passions: List<String>) {
        preSelected = passions.toSet()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_passion, parent, false)
        return PassionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PassionViewHolder, position: Int) {
        val passion = passions[position]
        holder.checkBox.text = passion
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = preSelected.contains(passion)
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onCheckChanged(passion, isChecked)
        }
    }

    override fun getItemCount(): Int = passions.size

    class PassionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxPassion)
    }
}