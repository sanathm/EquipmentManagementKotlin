package com.group5.sanath.equipmentmanagement

class Equipment(val ID: String, var Name: String, var Description: String, var Loaned: Int) {

    var inStock: Boolean = (Loaned==0)

    fun findLoaner():Int {
        if (GlobalVars.allEmployees != null)
            for (index in 0..GlobalVars.allEmployees.count()-1){
                if (GlobalVars.allEmployees[index].UserID == Loaned)
                    return index
            }
        return -1
    }

}