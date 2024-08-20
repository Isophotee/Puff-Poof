package com.example.puffpoof

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OTPActivity : AppCompatActivity() {

    private lateinit var otpEditText: EditText
    private lateinit var verifyButton: Button
    private var receivedOtp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        otpEditText = findViewById(R.id.otp_input)
        verifyButton = findViewById(R.id.verify_button)

        receivedOtp = intent.getStringExtra("otp")

        verifyButton.setOnClickListener {
            verifyOTP()
        }
    }

    private fun verifyOTP() {
        val enteredOtp = otpEditText.text.toString().trim()

        if (TextUtils.isEmpty(enteredOtp)) {
            Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show()
            return
        }

        if (enteredOtp == receivedOtp) {
            Toast.makeText(this, "OTP verified successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Invalid OTP, please try again", Toast.LENGTH_SHORT).show()
        }
    }
}
