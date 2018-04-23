package com.group5.sanath.equipmentmanagement

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URI
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,myEquipment.OnFragmentInteractionListener, myInfo.OnFragmentInteractionListener, VideoFeed.OnFragmentInteractionListener {

    lateinit var currentUser: User
    var myEquipment: MutableList<Equipment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        /*val myEquipFragment = myEquipment()
        //myEquipFragment.arguments = intent.extras
        val fragTransaction = fragmentManager.beginTransaction()
        fragTransaction.add(R.id.frag_container, myEquipFragment)
        fragTransaction.commit()*/

        val extras = intent.extras
        val ID = extras.getInt("ID")
        val PIN = extras.getInt("PIN")
        val priv = extras.getString("Privelage")
        val fname = extras.getString("Name")
        val sname = extras.getString("Surname")
        val dob = extras.get("DOB") as Date
        val addr = extras.getString("Address")
        val email = extras.getString("Email")
        val phone = extras.getString("Phone")
        val pos = extras.getString("Position")
        currentUser = User(ID,PIN,priv,fname,sname,dob,addr,email,phone,pos)
        if (priv == "Employee") {
            println("emp")

            val id = currentUser.UserID

            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)
            val url = GlobalVars.serverURL+"/get/myEquipment.php?id=$id"
            println(url)

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.GET, url,
                    Response.Listener<String> { response ->

                        println("Response is: ${response}")
                        val jsonArray = JSONArray(response)
                        val equip = mutableListOf<Int>()
                        for (index in 0..jsonArray.length()-1) {
                            println(jsonArray[index])
                            val json = jsonArray.getJSONObject(index)
                            val equip_id = json.getInt("equip_id")
                            equip.add(equip_id)
                            val name = json.getString("name")
                            val desc = json.getString("description")
                            val loaned = json.getInt("loaned_out_to")
                            myEquipment.add(Equipment(equip_id,name,desc,loaned))
                        }
                        currentUser.Equipment = equip

                        // TODO refresh ui
                        GlobalVars.empEquipment = myEquipment

                        val myEquipFragment = com.group5.sanath.equipmentmanagement.myEquipment()
                        val fragTransaction = fragmentManager.beginTransaction()
                        fragTransaction.add(R.id.frag_container, myEquipFragment)
                        fragTransaction.commit()

                    },
                    Response.ErrorListener { println("Connection Error: {$url}" )})

            // Add the request to the RequestQueue.
            queue.add(stringRequest)

            /*
            val array = extras.get("Equipment") as Array<Int>
            val equip = mutableListOf<Int>()
            for (index in 0..array.count()-1) {
                equip.add(array[index])
            }
            currentUser.Equipment = equip
            */
        } else {
            println("admn")
            currentUser.Password = extras.getString("Password")
            // TODO enable admin functions
        }

        GlobalVars.currentUser = currentUser

        val nav_header = nav_view.getHeaderView(0)
        nav_header.nametext.text = currentUser.FirstName + " " + currentUser.Surname
        nav_header.subtext.text = currentUser.EmailAddress

        nav_view.menu.getItem(0).setChecked(true)

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_myEquip -> {
                val myEquipFragment = com.group5.sanath.equipmentmanagement.myEquipment()
                val fragTransaction = fragmentManager.beginTransaction()
                fragTransaction.replace(R.id.frag_container, myEquipFragment)
                fragTransaction.addToBackStack(null)
                fragTransaction.commit()
                this.toolbar.title = "Manage Equipment"
            }
            R.id.nav_myInfo -> {
                val myInfoFrag = myInfo()
                val fragTransaction = fragmentManager.beginTransaction()
                fragTransaction.replace(R.id.frag_container, myInfoFrag)
                fragTransaction.addToBackStack(null)
                fragTransaction.commit()
                this.toolbar.title = "My Details"
            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_feed -> {
                val videoFrag = VideoFeed()
                val fragTransaction = fragmentManager.beginTransaction()
                fragTransaction.replace(R.id.frag_container, videoFrag)
                fragTransaction.addToBackStack(null)
                fragTransaction.commit()
                this.toolbar.title = "Video Feed"
            }
            R.id.nav_logout -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
