package com.group5.sanath.equipmentmanagement

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class Container : AppCompatActivity(),SettingsFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        val settingsFrag = SettingsFragment()
        val fragTransaction = fragmentManager.beginTransaction()
        fragTransaction.add(R.id.login_frag_container, settingsFrag)
        fragTransaction.commit()

    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
