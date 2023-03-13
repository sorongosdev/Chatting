package com.sorongos.chatting.chatlist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
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
import com.sorongos.chatting.databinding.FragmentChatroomlistBinding

class ChatListFragment : Fragment(R.layout.fragment_chatroomlist) {
    private lateinit var binding : FragmentChatroomlistBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatroomlistBinding.bind(view)

        val chatListAdapter = ChatAdapter{chatRoomItem ->

            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(ChatActivity.EXTRA_OTHER_USER_ID,chatRoomItem.otherUserId)
            intent.putExtra(ChatActivity.EXTRA_CHAT_ROOM_ID,chatRoomItem.chatRoomId)
            startActivity(intent)
        }
        binding.chatListRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            adapter = chatListAdapter
        }
        val currentUserId = Firebase.auth.currentUser?.uid ?: return
        val chatRoomsDB = Firebase.database.reference.child(Key.DB_CHAT_ROOMS).child(currentUserId)

        //데이터가 바뀔때마다 수신
        chatRoomsDB.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatRoomList = snapshot.children.map{
                    //변환
                    it.getValue(ChatRoomItem::class.java)
                }
                chatListAdapter.submitList(chatRoomList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}