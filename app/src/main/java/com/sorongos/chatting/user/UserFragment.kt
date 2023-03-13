package com.sorongos.chatting.user

import android.content.Intent
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
import com.sorongos.chatting.chatdetail.ChatActivity
import com.sorongos.chatting.chatlist.ChatRoomItem
import com.sorongos.chatting.databinding.FragmentUserlistBinding
import java.util.*

class UserFragment : Fragment(R.layout.fragment_userlist) {
    private lateinit var binding: FragmentUserlistBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserlistBinding.bind(view)

        val userListAdapter = UserAdapter { otherUser ->

            val myUserId = Firebase.auth.currentUser?.uid ?: ""
            val chatRoomDB = Firebase.database.reference.child(Key.DB_CHAT_ROOMS).child(myUserId)
                .child(otherUser.userId ?: "")

            chatRoomDB.get().addOnSuccessListener {
                //이미 만들어진 채팅방, 데이터 존재
                var chatRoomId = ""
                if (it.value != null) {
                    val chatRoom = it.getValue(ChatRoomItem::class.java)
                    chatRoomId = chatRoom?.chatRoomId ?: ""
                } else {
                    //새로운 채팅방 생성, 랜덤 스트링을 뽑아줌
                    chatRoomId = UUID.randomUUID().toString()
                    val newChatRoom = ChatRoomItem(
                        chatRoomId = chatRoomId,
                        otherUserName = otherUser.username,
                        otherUserId = otherUser.userId,
                    )
                    chatRoomDB.setValue(newChatRoom)
                }
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra(ChatActivity.EXTRA_OTHER_USER_ID,otherUser.userId)
                intent.putExtra(ChatActivity.EXTRA_CHAT_ROOM_ID,chatRoomId)
                startActivity(intent)
            }

            "ChatRooms/myUserId/otherUserId"
            "otherUserId"
            "chatRoomId"
        }
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