package com.group5.sanath.equipmentmanagement

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.VideoView
import kotlinx.android.synthetic.main.fragment_video_feed.*
import java.net.URL


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [VideoFeed.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class VideoFeed : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_video_feed, container, false)

        /*val vidView = view.findViewById<VideoView>(R.id.videoView)
        val vidURL = Uri.parse(GlobalVars.cameraIP)
        vidView.setVideoURI(vidURL)
        vidView.start()*/

        val web_view = view.findViewById<WebView>(R.id.webView)
        //val dispURL = Uri.parse(GlobalVars.cameraIP)
        web_view.loadUrl(GlobalVars.cameraIP)

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

}
