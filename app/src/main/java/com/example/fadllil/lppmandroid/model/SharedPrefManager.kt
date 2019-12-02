package com.example.fadllil.lppmandroid.model

import android.content.Context

class SharedPrefManager private constructor(private val mCtx: Context){

    val isLoggedIn:Boolean
        get(){
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,  Context.MODE_PRIVATE )
            return sharedPreferences.getInt("id", -1) != -1
        }

    val user:User
        get(){
            val SharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(
                    SharedPreferences.getInt("id", -1).toString(),
                    SharedPreferences.getString("username", null),
                    SharedPreferences.getString("email", null)
            )
        }

    fun saveUser(user: User){
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("id", user.id!!)
        editor.putString("username", user.username)
        editor.putString("email", user.email)

        editor.apply()
    }

    fun clear(){
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object{
        private val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPrefManager? = null

        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager{
            if(mInstance == null){
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}