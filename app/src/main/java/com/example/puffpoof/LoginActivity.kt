package com.example.puffpoof

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView

    private val SEND_SMS_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)
        registerLink = findViewById(R.id.register_link)
        loginButton.setOnClickListener {
            if (checkAndRequestSmsPermission()) {
                login()
            }
        }
        registerLink.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkAndRequestSmsPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SEND_SMS_PERMISSION_REQUEST_CODE)
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SEND_SMS_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                login()
            } else {
                Toast.makeText(this, "Permission denied to send SMS", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Username must be filled", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password must be filled", Toast.LENGTH_SHORT).show()
            return
        }
        if (validateCredentials(username, password)) {
            val otp = generateOTP()
            sendOTPSMS("1234567890", otp)
            val intent = Intent(this@LoginActivity, OTPActivity::class.java)
            intent.putExtra("otp", otp)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateCredentials(username: String, password: String): Boolean {
        return "testuser" == username && "testpass" == password
    }

    private fun generateOTP(): String {
        val random = Random()
        val otp = 1000 + random.nextInt(9000)
        return otp.toString()
    }

    private fun sendOTPSMS(phoneNumber: String, otp: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, "Your OTP code is: $otp", null, null)
    }
}
