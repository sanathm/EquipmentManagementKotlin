package com.group5.sanath.equipmentmanagement

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)

        login_button.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("ID",1)
            intent.putExtra("PIN",1)
            intent.putExtra("Privelage","Employee")
            intent.putExtra("Name","Sanath")
            intent.putExtra("Surname","Maharaj")
            intent.putExtra("DOB",Date())
            intent.putExtra("Address","Ressies")
            intent.putExtra("Email","test@example.com")
            intent.putExtra("Phone","0123456789")
            intent.putExtra("Position","Worker")
            intent.putExtra("Equipment", arrayOf(5,1,2,3))

            startActivity(intent)
            println("Hello?")
        }
    }
}
