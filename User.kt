package com.group5.sanath.equipmentmanagement

import java.util.*

data class User(val UserID: Int, var PIN: Int, var privelage: String, var FirstName: String, var Surname: String, var DOB: Date, var Address: String, var EmailAddress: String, var PhoneNumber: String, var Position: String) {

    var Equipment: MutableList<String> = mutableListOf()
    var Password = ""

    fun removeEquipment(equipID: String): Boolean {

        /*for (index in 0..Equipment.count()){
            if (Equipment[index] == equipID) {
                Equipment.removeAt(index)
            }
        }*/
        return Equipment.remove(equipID)
    }

}