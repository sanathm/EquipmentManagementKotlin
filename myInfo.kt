package com.group5.sanath.equipmentmanagement

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.DialogInterface
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_my_info.*
import kotlinx.android.synthetic.main.fragment_my_info.view.*
import java.text.SimpleDateFormat

class myInfo : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_info, container, false)
        val user = GlobalVars.currentUser
        view.ID_field.text = user.UserID.toString()
        view.nameField.setText(user.FirstName,TextView.BufferType.EDITABLE)
        view.surnameField.setText(user.Surname,TextView.BufferType.EDITABLE)
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        view.dob_field.setText(formatter.format(user.DOB),TextView.BufferType.EDITABLE)
        view.addr_field.setText(user.Address,TextView.BufferType.EDITABLE)
        view.email_field.setText(user.EmailAddress,TextView.BufferType.EDITABLE)
        view.phone_field.setText(user.PhoneNumber,TextView.BufferType.EDITABLE)
        view.pos_field.setText(user.Position,TextView.BufferType.EDITABLE)

        if (user.privelage == "Employee")
            view.pos_field.inputType = InputType.TYPE_NULL

        view.saveUser_button.setOnClickListener {
            user.FirstName = view.nameField.text.toString()
            user.Surname = view.surnameField.text.toString()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            user.DOB = formatter.parse(view.dob_field.text.toString())
            user.Address = view.addr_field.text.toString()
            user.EmailAddress = view.email_field.text.toString()
            user.PhoneNumber = view.phone_field.text.toString()
            user.Position = view.pos_field.text.toString()
            user.updateDB(activity.baseContext)
            GlobalVars.currentUser = user
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
