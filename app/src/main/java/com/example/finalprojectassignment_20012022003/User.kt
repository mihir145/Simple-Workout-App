package com.example.finalprojectassignment_20012022003

import org.json.JSONObject
import java.io.Serializable

class User(private var username: String, private var email: String, private var password: String) : Serializable {

    private var id: Int = -1

    fun setId(id:Int){
        this.id = id
    }

    fun getId():Int{
        return this.id
    }

    fun getUsername():String {
        return this.username
    }

    fun getPassword():String {
        return this.password
    }

    fun getEmail():String {
        return this.email
    }

}