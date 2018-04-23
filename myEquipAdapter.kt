package com.group5.sanath.equipmentmanagement

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.myequip_row.view.*

class myEquipAdapter(private val equipment: MutableList<Equipment>): RecyclerView.Adapter<myEquipAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.myequip_row,parent,false)
        return viewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return equipment.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val current = equipment[position]
        holder.displayData(current)
    }

    class viewHolder(v: View): RecyclerView.ViewHolder(v),View.OnClickListener {

        private var view = v
        private var equip: Equipment? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent = Intent(view.context,EquipmentDetails::class.java)
            intent.putExtra("id",equip!!.ID)
            intent.putExtra("name",equip!!.Name)
            intent.putExtra("description",equip!!.Description)

            view.context.startActivity(intent)
        }

        fun displayData(equip: Equipment){
            this.equip = equip
            view.myequip_Name.text = equip.Name
        }



    }
}