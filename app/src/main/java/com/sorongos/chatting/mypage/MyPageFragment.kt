package com.sorongos.chatting.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sorongos.chatting.Key
import com.sorongos.chatting.LoginActivity
import com.sorongos.chatting.R
import com.sorongos.chatting.databinding.FragmentMypageBinding
import com.sorongos.chatting.user.UserItem

class MyPageFragment : Fragment(R.layout.fragment_mypage) {
    private lateinit var binding: FragmentMypageBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMypageBinding.bind(view)

        //원래 기능 설정
        val currentUserId = Firebase.auth.currentUser?.uid ?: ""
        val currentUserDB = Firebase.database.reference.child(Key.DB_USERS).child(currentUserId)

        currentUserDB.get().addOnSuccessListener {
            val currentUserItem = it.getValue(UserItem::class.java) ?: return@addOnSuccessListener

            binding.userNameEditText.setText(currentUserItem.username)
            binding.userDescriptionEditText.setText(currentUserItem.description)

        }

        binding.applyButton.setOnClickListener {
            val username = binding.userNameEditText.text.toString()
            val description = binding.userDescriptionEditText.text.toString()

            /**username이 empty면 오류 생성*/
            binding.userNameEditText.addTextChangedListener {
                it?.let { text ->
                    binding.userNameTextInputLayout.error = when (text.length) {
                        0 -> "값을 입력해주세요"
                        else -> null
                    }
                }
            }
            //firebase
            val user = mutableMapOf<String, Any>()
            user["username"] = username
            user["description"] = description
            currentUserDB.updateChildren(user)
        }

        /**로그아웃 하면 mainActivity 종료*/
        binding.signOutButton.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
    }
}