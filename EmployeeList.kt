package com.group5.sanath.equipmentmanagement

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class EmployeeList : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var manager: LinearLayoutManager
    private lateinit var adapter: employeeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_employee_list, container, false)

        manager = LinearLayoutManager(this.activity.applicationContext)
        val recycler = view.findViewById<RecyclerView>(R.id.employeeListView)
        recycler.layoutManager = manager
        val divider = DividerItemDecoration(recycler.context,manager.orientation)
        recycler.addItemDecoration(divider)
        adapter = employeeAdapter(GlobalVars.allEmployees)
        recycler.adapter = adapter

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
