package com.sorongos.chatting

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sorongos.chatting.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var auth: FirebaseAuth = Firebase.auth
    lateinit var email: String
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkId()
        checkPw()

        binding.signUpButton.setOnClickListener {
            email = binding.logInputId.text.toString()
            password = binding.logInputPw.text.toString()
            createAccount(email, password)
        }

        binding.signInButton.setOnClickListener {
            email = binding.logInputId.text.toString()
            password = binding.logInputPw.text.toString()
            signIn(email, password)
        }

    }

    /**화원가입*/
    private fun createAccount(email: String, password: String) {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        Toast.makeText(this, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    // 이미 등록된 사용자의 경우
                    else {
                        Log.d(ContentValues.TAG, "createUserWithEmail:failed")
                        Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    /**로그인*/
    private fun signIn(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    //로그인 성공시 mainActivity로 이동
                    if (task.isSuccessful) {
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    //로그인 실패
                    } else {
                        Log.d(ContentValues.TAG, "signInWithEmail: failed")
                    }
                }
        }
    }

    /**아이디 형식은 이메일 주소 형식이어야함*/
    private fun checkId() {
        binding.logInputId.addTextChangedListener {
            val email = binding.logInputId.text.toString()
            val pattern = Patterns.EMAIL_ADDRESS
            binding.idTextInputLayout.error = if (pattern.matcher(email).matches()) null
            else "이메일 주소 형식을 입력해주세요."
        }
    }

    /**비밀번호는 8자리 이상이어야함*/
    private fun checkPw() {
        binding.logInputPw.addTextChangedListener {
            it?.let { text ->
                binding.pwTextInputLayout.error = if (text.length < 8) "8자 이상 입력해주세요" else null
            }
        }
    }
}