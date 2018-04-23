package com.group5.sanath.equipmentmanagement

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_equipment_details.*

class EquipmentDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_details)

        val intent = getIntent()
        val id = intent.getIntExtra("id",0)
        val name = intent.getStringExtra("name")
        val descrip = intent.getStringExtra("description")

        equip_ID.text = id.toString()
        equip_name.setText(name,TextView.BufferType.EDITABLE)
        equip_descrip.setText(descrip,TextView.BufferType.EDITABLE)

    }
}
