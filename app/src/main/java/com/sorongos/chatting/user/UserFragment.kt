package com.sorongos.chatting.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sorongos.chatting.Key
import com.sorongos.chatting.R
import com.sorongos.chatting.databinding.FragmentUserlistBinding

class UserFragment : Fragment(R.layout.fragment_userlist) {
    private lateinit var binding: FragmentUserlistBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserlistBinding.bind(view)

        val userListAdapter = UserAdapter()
        binding.userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }

        val currentUserId = Firebase.auth.currentUser?.uid ?: ""

        Firebase.database.reference.child(Key.DB_USERS)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userItemList = mutableListOf<UserItem>()
                    snapshot.children.forEach {
                        /**
                         * userList에 나는 나오지 않아도 됨.
                         * 내 아이디가 아니라면 리스트에 추가
                         * user가 매핑되지 않을시 종료
                         * */
                        val user = it.getValue(UserItem::class.java)
                        user ?: return
                        if (user?.userId != currentUserId) {
                            userItemList.add(user)
                        }
                    }
                    userListAdapter.submitList(userItemList)
                }

                /**데이터 조회하다 실패*/
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "데이터 조회에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }
}