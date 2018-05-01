package com.group5.sanath.equipmentmanagement

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val serverAddress = view.findViewById<EditText>(R.id.serverAddress)
        val cameraAddress = view.findViewById<EditText>(R.id.cameraAddress)
        serverAddress.setText(GlobalVars.serverURL,TextView.BufferType.EDITABLE)
        cameraAddress.setText(GlobalVars.cameraIP,TextView.BufferType.EDITABLE)
        //cameraAddress.inputType = InputType.TYPE_NULL

        val saveBtn = view.findViewById<Button>(R.id.addressSave)
        saveBtn.setOnClickListener {
            val sharedPref = this.activity.applicationContext.getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("server",serverAddress.text.toString())
            GlobalVars.serverURL = serverAddress.text.toString()
            GlobalVars.cameraIP = GlobalVars.serverURL+"/get/viewFeed.php"
            editor.putString("camera",GlobalVars.cameraIP)
            if (editor.commit()){
                Toast.makeText(activity.baseContext,"Details Saved", Toast.LENGTH_SHORT).show()
                activity.onBackPressed()
            } else {
                Toast.makeText(activity.baseContext,"Error saving", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }


    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(uri: Uri)
    }

}
