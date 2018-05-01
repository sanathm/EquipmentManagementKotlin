package com.group5.sanath.equipmentmanagement

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.app_bar_main.*

class empEquipment : AppCompatActivity(), myEquipment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emp_equipment)


        val name = intent.extras.getString("user")

        val equip_list = myEquipment.newInstance("employee")
        val fragTransaction = fragmentManager.beginTransaction()
        fragTransaction.add(R.id.empEquipContainer, equip_list)
        fragTransaction.commit()
        this.supportActionBar!!.title = name+"'s Equipment"
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
