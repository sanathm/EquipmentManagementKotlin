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
import android.widget.Adapter
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_my_equipment.*

private const val type = "all"

class myEquipment() : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var listType: String
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var adapter: myEquipAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listType = it.getString("type")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_equipment, container, false)

        layoutManager = LinearLayoutManager(this.activity.applicationContext)
        val recycler = view.findViewById<RecyclerView>(R.id.myEquipListView)
        recycler.layoutManager = layoutManager
        val divider = DividerItemDecoration(recycler.context,layoutManager.orientation)
        recycler.addItemDecoration(divider)
        if (listType == "employee") {
            adapter = myEquipAdapter(GlobalVars.empEquipment)
        } else {
            adapter = myEquipAdapter(GlobalVars.allEquipment)
        }
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

    companion object {

        @JvmStatic
        fun newInstance(type: String) =
                myEquipment().apply {
                    arguments = Bundle().apply {
                        putString("type",type)
                    }
                }
    }
}
