package com.group5.sanath.equipmentmanagement

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,myEquipment.OnFragmentInteractionListener, myInfo.OnFragmentInteractionListener, VideoFeed.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, EmployeeList.OnFragmentInteractionListener {

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

        //TODO move to onStart()
        if (priv == "Employee") {
            println("emp")

            val id = currentUser.UserID

            // Instantiate the RequestQueue.

            val url = GlobalVars.serverURL+"/get/myEquipment.php?id=$id"
            println(url)

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.GET, url,
                    Response.Listener<String> { response ->

                        println("Response is: ${response}")
                        val jsonArray = JSONArray(response)
                        val equip = mutableListOf<String>()
                        for (index in 0..jsonArray.length()-1) {
                            println(jsonArray[index])
                            val json = jsonArray.getJSONObject(index)
                            val equip_id = json.getString("equip_id")
                            equip.add(equip_id)
                            val name = json.getString("name")
                            val desc = json.getString("description")
                            val loaned = json.getInt("loaned_out_to")
                            myEquipment.add(Equipment(equip_id,name,desc,loaned))
                        }
                        currentUser.Equipment = equip

                        // TODO refresh ui
                        GlobalVars.empEquipment = myEquipment

                        val myEquipFragment = com.group5.sanath.equipmentmanagement.myEquipment.newInstance("employee")
                        val fragTransaction = fragmentManager.beginTransaction()
                        fragTransaction.add(R.id.frag_container, myEquipFragment)
                        fragTransaction.commit()
                        this.toolbar.title = "My Equipment"

                    },
                    Response.ErrorListener { println("Connection Error: {$url}" )})

            // Add the request to the RequestQueue.
           GlobalVars.queue.add(stringRequest)

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


            // Instantiate the RequestQueue.

            val url = GlobalVars.serverURL+"/get/equipment.php"
            println(url)

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.GET, url,
                    Response.Listener<String> { response ->

                        println("Response is: ${response}")
                        val jsonArray = JSONArray(response)
                        val equip = mutableListOf<String>()
                        for (index in 0..jsonArray.length()-1) {
                            println(jsonArray[index])
                            val json = jsonArray.getJSONObject(index)
                            val equip_id = json.getString("equip_id")
                            equip.add(equip_id)
                            val name = json.getString("name")
                            val desc = json.getString("description")
                            val loaned = json.getInt("loaned_out_to")
                            myEquipment.add(Equipment(equip_id,name,desc,loaned))
                        }

                        // TODO refresh ui
                        GlobalVars.allEquipment = myEquipment

                        val myEquipFragment = com.group5.sanath.equipmentmanagement.myEquipment.newInstance("all")
                        val fragTransaction = fragmentManager.beginTransaction()
                        fragTransaction.add(R.id.frag_container, myEquipFragment)
                        fragTransaction.commit()
                        this.toolbar.title = "Equipment"

                    },
                    Response.ErrorListener { println("Connection Error: {$url}" )})

            // Add the request to the RequestQueue.
           GlobalVars.queue.add(stringRequest)
            val empURL = GlobalVars.serverURL+"/get/employees.php"

            val empRequest = StringRequest(Request.Method.GET, empURL,
                    Response.Listener<String> { response ->

                        println("Response is: ${response}")
                        val jsonArray = JSONArray(response)
                        val users = mutableListOf<User>()
                        for (index in 0..jsonArray.length()-1) {
                            val json = jsonArray.getJSONObject(index)
                            val ID = json.getInt("employee_id")
                            val PIN = json.getInt("pin")
                            val priv = "Employee"
                            val fname = json.getString("first_names")
                            val sname = json.getString("surname")
                            val dateString = json.getString("date_of_birth")
                            val formatter = SimpleDateFormat("yyyy-MM-dd")
                            val dob = formatter.parse(dateString)
                            val addr = json.getString("physical_address")
                            val email = json.getString("email_address")
                            val phone = json.getString("phone_number")
                            val pos = json.getString("position")
                            users.add(User(ID,PIN,priv,fname,sname,dob,addr,email,phone,pos))

                        }

                        // TODO refresh ui
                        GlobalVars.allEmployees = users

                    },
                    Response.ErrorListener { println("Connection Error: {$url}" )})

            // Add the request to the RequestQueue.
           GlobalVars.queue.add(empRequest)

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
                val myEquipFragment: myEquipment
                if (currentUser.privelage == "Admin") {
                    myEquipFragment = com.group5.sanath.equipmentmanagement.myEquipment.newInstance("all")
                    this.toolbar.title = "Equipment"
                } else {
                    myEquipFragment = com.group5.sanath.equipmentmanagement.myEquipment.newInstance("employee")
                    this.toolbar.title = "My Equipment"
                }
                val fragTransaction = fragmentManager.beginTransaction()
                fragTransaction.replace(R.id.frag_container, myEquipFragment)
                //fragTransaction.addToBackStack(null)
                fragTransaction.commit()
            }
            R.id.nav_myInfo -> {
                val myInfoFrag = myInfo()
                val fragTransaction = fragmentManager.beginTransaction()
                fragTransaction.replace(R.id.frag_container, myInfoFrag)
                //fragTransaction.addToBackStack(null)
                fragTransaction.commit()
                this.toolbar.title = "My Details"
            }
            R.id.nav_employees -> {
                if (GlobalVars.currentUser.privelage == "Admin") {
                    val empFragment = EmployeeList()
                    val fragTransaction = fragmentManager.beginTransaction()
                    fragTransaction.replace(R.id.frag_container, empFragment)
                    //fragTransaction.addToBackStack(null)
                    fragTransaction.commit()
                    this.toolbar.title = "Employees"
                } else {
                    val dialog = AlertDialog.Builder(this).create()
                    dialog.setTitle("Not Authorised")
                    dialog.setMessage("This feature is only available to administrators")
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK",{ dialogInterface: DialogInterface, i: Int -> })
                    dialog.show()
                }
            }
            R.id.nav_settings -> {
                val settings = SettingsFragment()
                val fragTransaction = fragmentManager.beginTransaction()
                fragTransaction.replace(R.id.frag_container, settings)
                //fragTransaction.addToBackStack(null)
                fragTransaction.commit()
                this.toolbar.title = "Settings"
            }
            R.id.nav_feed -> {
                if (GlobalVars.currentUser.privelage == "Admin") {
                    val videoFrag = VideoFeed()
                    val fragTransaction = fragmentManager.beginTransaction()
                    fragTransaction.replace(R.id.frag_container, videoFrag)
                    //fragTransaction.addToBackStack(null)
                    fragTransaction.commit()
                    this.toolbar.title = "Video Feed"
                } else {
                    val dialog = AlertDialog.Builder(this).create()
                    dialog.setTitle("Not Authorised")
                    dialog.setMessage("This feature is only available to administrators")
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK",{ dialogInterface: DialogInterface, i: Int -> })
                    dialog.show()
                }
            }
            R.id.nav_logout -> {
                super.onBackPressed()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
