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



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "equip"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [myEquipment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [myEquipment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class myEquipment : Fragment() {
    // TODO: Rename and change types of parameters
    private var equip: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var adapter: myEquipAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            equip = it.getString(ARG_PARAM1)
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
        adapter = myEquipAdapter(GlobalVars.empEquipment)
        recycler.adapter = adapter


        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment myEquipment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(equip: String) =
                myEquipment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, equip)
                    }
                }
    }
}
