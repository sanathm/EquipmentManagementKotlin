package com.group5.sanath.equipmentmanagement

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.employee_row.view.*

class employeeAdapter(private val employees: MutableList<User>):RecyclerView.Adapter<employeeAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.employee_row,parent,false)
        return employeeAdapter.viewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.displayData(employees[position])
    }



    class viewHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view = v
        private var employee: User? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent = Intent(view.context,EmployeeDetails::class.java)

            intent.putExtra("empID",employee!!.UserID)
            //intent.putExtra("empID",-1)

            view.context.startActivity(intent)
        }

        fun displayData(emp: User){
            this.employee = emp
            view.emp_id.text = emp.UserID.toString()
            view.emp_name.text = emp.FirstName + " " + emp.Surname
            view.emp_email.text = emp.EmailAddress
        }

    }
}