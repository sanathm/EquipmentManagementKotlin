package com.group5.sanath.equipmentmanagement

class Equipment(val ID: Int, var Name: String, var Description: String, var Loaned: Int) {

    var inStock: Boolean = (Loaned!=0)

}