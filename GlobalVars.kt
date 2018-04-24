package com.group5.sanath.equipmentmanagement

object GlobalVars {
    val serverURL = "http://10.0.1.31:9000/api"
    val cameraIP = "http://10.0.1.6:8080/video"
    lateinit var currentUser:User
    var empEquipment = mutableListOf<Equipment>()
    var allEmployees = mutableListOf<User>()
}