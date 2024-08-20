package com.example.puffpoof

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var genderGroup: RadioGroup
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        emailEditText = findViewById(R.id.email)
        phoneEditText = findViewById(R.id.phone)
        genderGroup = findViewById(R.id.gender_group)
        registerButton = findViewById(R.id.register_button)
        loginLink = findViewById(R.id.login_link)
        dbHelper = DBHelper(this)

        registerButton.setOnClickListener { register() }
        loginLink.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun register() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val selectedGenderId = genderGroup.checkedRadioButtonId

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email) ||
            TextUtils.isEmpty(phone) || selectedGenderId == -1
        ) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isUsernameUnique(username)) {
            Toast.makeText(this, "Username is already registered", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return
        }

        if (!email.endsWith("@puff.com")) {
            Toast.makeText(this, "Email must end with '@puff.com'", Toast.LENGTH_SHORT).show()
            return
        }

        if (phone.length < 11 || phone.length > 13) {
            Toast.makeText(this, "Phone number must be between 11 and 13 characters", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedGenderButton = findViewById<RadioButton>(selectedGenderId)
        val gender = selectedGenderButton.text.toString()

        // Store user information in the database
        dbHelper.insertUser(username, email, password, phone, gender)
        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isUsernameUnique(username: String): Boolean {
        return !dbHelper.isUsernameExist(username)
    }
}
