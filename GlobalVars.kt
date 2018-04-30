package com.group5.sanath.equipmentmanagement

import com.android.volley.RequestQueue

object GlobalVars {
    lateinit var serverURL: String
    lateinit var cameraIP: String
    lateinit var currentUser: User
    lateinit var queue: RequestQueue
    var empEquipment = mutableListOf<Equipment>()
    var allEquipment = mutableListOf<Equipment>()
    var allEmployees = mutableListOf<User>()


}