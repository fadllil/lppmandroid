package com.example.fadllil.lppmandroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fadllil.lppmandroid.admin.HomeAdminActivity
import retrofit2.Call
import com.example.fadllil.lppmandroid.model.LoginResponse
import com.example.fadllil.lppmandroid.model.SharedPrefManager
import com.example.fadllil.lppmandroid.service.RetrofitClient
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_sign_in.setOnClickListener{

            val username = Edt_username.text.toString().trim()
            val password = Edt_password.text.toString().trim()
//            startActivity(Intent(this, MenuActivity::class.java))

            if(username.isEmpty()){
                Edt_username.error = "Email required"
                Edt_username.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()){
                Edt_password.error = "Password required"
                Edt_password.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.userLogin(username, password)
                    .enqueue(object : Callback<LoginResponse> {
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                                call: Call<LoginResponse>,
                                response: Response<LoginResponse>
                        ) {
                            if (response.body()?.error==false){
                                Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                                SharedPrefManager.getInstance(applicationContext)
                                        .saveUser(response.body()?.data?.user!!)
                                val intent = Intent(applicationContext, HomeAdminActivity::class.java)
                                intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                startActivity(intent)
                            }else{
                                Toast.makeText(applicationContext, "username atau password salah", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
        }
    }

    override fun onStart() {
        super.onStart()

        if(SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, HomeAdminActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}
