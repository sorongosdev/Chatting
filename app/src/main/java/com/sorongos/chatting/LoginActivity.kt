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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.sorongos.chatting.Key.Companion.DB_USERS
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

        /**회원가입*/
        binding.signUpButton.setOnClickListener {
            email = binding.logInputId.text.toString()
            password = binding.logInputPw.text.toString()
            createAccount(email, password)
        }

        /**로그인*/
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
                        Toast.makeText(this, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    // 이미 등록된 사용자의 경우
                    else {
                        Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    /**로그인*/
    private fun signIn(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    //로그인 성공시 mainActivity로 이동
                    val currentUser = auth.currentUser
                    if (task.isSuccessful && currentUser != null) {
                        val userId = currentUser.uid

                        Firebase.messaging.token.addOnCompleteListener {
                            val token = it.result
                            val user = mutableMapOf<String, Any>()
                            user["userId"] = userId
                            user["username"] = email
                            user["fcmToken"] = token

                            Firebase.database.reference.child(DB_USERS).child(userId)
                                .updateChildren(user)

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

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