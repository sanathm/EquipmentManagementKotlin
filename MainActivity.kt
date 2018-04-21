package com.group5.sanath.equipmentmanagement

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        println("Alive")
/*
        val myEquipFragment = myEquipment()
        //myEquipFragment.arguments = intent.extras
        val fragTransaction = fragmentManager.beginTransaction()
        fragTransaction.add(R.id.frag_container, myEquipFragment)
        fragTransaction.commit()
*/
        println("Still Alive")

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
            val array = extras.get("Equipment") as Array<Int>
            val equip = mutableListOf<Int>()
            for (index in 0..array.count()-1) {
                equip.add(array[index])
            }
            currentUser.Equipment = equip
        } else {
            println("admn")
            currentUser.Password = extras.getString("Password")
            // TODO enable admin functions
        }


        println("And now?")

        val nav_header = nav_view.getHeaderView(0)
        nav_header.nametext.text = currentUser.FirstName + " " + currentUser.Surname
        nav_header.subtext.text = currentUser.EmailAddress

        println("How about now?")

        /*var testString: String = ""
        for (index in 0..3){
            testString = testString.plus(currentUser.Equipment[index])
        }*/

        println("Still?")

        //testText.text = testString

        nav_view.setNavigationItemSelectedListener(this)
        println("Wtf")
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
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
