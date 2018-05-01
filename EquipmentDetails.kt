package com.group5.sanath.equipmentmanagement

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_equipment_details.*

class EquipmentDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_details)

        val intent = getIntent()
        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        val descrip = intent.getStringExtra("description")
        val loaned = intent.getIntExtra("loaned",0)

        val currentEquip = Equipment(id,name,descrip,loaned)

        var equipIndex = 0

        for (index in 0..GlobalVars.allEquipment.count()-1){
            if (GlobalVars.allEquipment[index].ID == id){
                equipIndex = index
                break
            }
        }

        equip_ID.text = id
        equip_name.setText(name,TextView.BufferType.EDITABLE)
        equip_descrip.setText(descrip,TextView.BufferType.EDITABLE)

        if (GlobalVars.currentUser.privelage == "Employee") {
            loaned_ID.text = GlobalVars.currentUser.FirstName
            equip_editing.visibility = View.INVISIBLE
        } else {
            if (currentEquip.inStock)
                loaned_ID.text = "In Stock"
            else {
                val userIndex = currentEquip.findLoaner()
                if (userIndex == -1)
                    loaned_ID.text = "Error"
                else
                    loaned_ID.text = GlobalVars.allEmployees[userIndex].FirstName
            }
        }

        equip_save.setOnClickListener {
            currentEquip.Name = equip_name.text.toString()
            currentEquip.Description = equip_descrip.text.toString()
            currentEquip.updateDB(baseContext)
            GlobalVars.allEquipment[equipIndex] = currentEquip
        }

        returned_btn.setOnClickListener {
            if (!currentEquip.inStock){
                currentEquip.Loaned = 0
                currentEquip.inStock = true
                currentEquip.updateDB(baseContext)
                loaned_ID.text = "In Stock"
            } else
                Toast.makeText(baseContext,"Already in stock", Toast.LENGTH_SHORT).show()
        }

    }
}
