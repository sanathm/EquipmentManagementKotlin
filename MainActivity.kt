package com.group5.sanath.equipmentmanagement

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import android.widget.Toast
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

            val id = currentUser.UserID

            // Instantiate the RequestQueue.

            val url = GlobalVars.serverURL+"/get/myEquipment.php?id=$id"

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.GET, url,
                    Response.Listener<String> { response ->

                        val jsonArray = JSONArray(response)
                        val equip = mutableListOf<String>()
                        for (index in 0..jsonArray.length()-1) {
                            val json = jsonArray.getJSONObject(index)
                            val equip_id = json.getString("equip_id")
                            equip.add(equip_id)
                            val name = json.getString("name")
                            val desc = json.getString("description")
                            val loaned = json.getInt("loaned_out_to")
                            myEquipment.add(Equipment(equip_id,name,desc,loaned))
                        }
                        currentUser.Equipment = equip

                        GlobalVars.empEquipment = myEquipment

                        val myEquipFragment = com.group5.sanath.equipmentmanagement.myEquipment.newInstance("employee")
                        val fragTransaction = fragmentManager.beginTransaction()
                        fragTransaction.add(R.id.frag_container, myEquipFragment)
                        fragTransaction.commit()
                        this.toolbar.title = "My Equipment"

                    },
                    Response.ErrorListener {
                        Toast.makeText(baseContext,"Connection Error", Toast.LENGTH_SHORT).show()
                    })

            // Add the request to the RequestQueue.
           GlobalVars.queue.add(stringRequest)


        } else {

            currentUser.Password = extras.getString("Password")

            val url = GlobalVars.serverURL+"/get/equipment.php"

            val stringRequest = StringRequest(Request.Method.GET, url,
                    Response.Listener<String> { response ->


                        val jsonArray = JSONArray(response)
                        val equip = mutableListOf<String>()
                        for (index in 0..jsonArray.length()-1) {

                            val json = jsonArray.getJSONObject(index)
                            val equip_id = json.getString("equip_id")
                            equip.add(equip_id)
                            val name = json.getString("name")
                            val desc = json.getString("description")
                            val loaned = json.getInt("loaned_out_to")
                            myEquipment.add(Equipment(equip_id,name,desc,loaned))
                        }

                        GlobalVars.allEquipment = myEquipment

                        val myEquipFragment = com.group5.sanath.equipmentmanagement.myEquipment.newInstance("all")
                        val fragTransaction = fragmentManager.beginTransaction()
                        fragTransaction.add(R.id.frag_container, myEquipFragment)
                        fragTransaction.commit()
                        this.toolbar.title = "Equipment"

                    },
                    Response.ErrorListener {
                        Toast.makeText(baseContext,"Connection Error", Toast.LENGTH_SHORT).show()
                    })

            GlobalVars.queue.add(stringRequest)

            val empURL = GlobalVars.serverURL+"/get/employees.php"

            val empRequest = StringRequest(Request.Method.GET, empURL,
                    Response.Listener<String> { response ->

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

                        GlobalVars.allEmployees = users

                    },
                    Response.ErrorListener {
                        Toast.makeText(baseContext,"Connection Error", Toast.LENGTH_SHORT).show()
                    })

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

    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val dialog = AlertDialog.Builder(this).create()
            dialog.setTitle("Logout")
            dialog.setMessage("Are you sure you would like to logout?")
            dialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK",{ dialogInterface: DialogInterface, i: Int -> })
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel",{ dialogInterface: DialogInterface, i: Int -> })
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener({
                dialog.dismiss()
                super.onBackPressed()
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_addUser -> {
                if (GlobalVars.currentUser.privelage == "Admin"){
                    val intent = Intent(applicationContext,EmployeeDetails::class.java)

                    intent.putExtra("empID",-1)

                    applicationContext.startActivity(intent)
                } else {
                    val dialog = AlertDialog.Builder(this).create()
                    dialog.setTitle("Not Authorised")
                    dialog.setMessage("This feature is only available to administrators")
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK",{ dialogInterface: DialogInterface, i: Int -> })
                    dialog.show()
                }

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Navigation Menu
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
