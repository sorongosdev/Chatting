package com.sorongos.chatting.chatdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sorongos.chatting.Key
import com.sorongos.chatting.databinding.ActivityChatdetailBinding
import com.sorongos.chatting.user.UserItem

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatdetailBinding
    private var chatRoomId: String = ""
    private var otherUserId: String = ""
    private var myUserId: String = ""
    private var myUserName: String = ""

    private val chatItemList = mutableListOf<ChatItem>()

    // putExtra
    // chatRoomID : 채팅방 아이디
    // otherUserId : 상대방에 대한 정보

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatRoomId = intent.getStringExtra(EXTRA_CHAT_ROOM_ID) ?: return
        otherUserId = intent.getStringExtra(EXTRA_OTHER_USER_ID) ?: return
        myUserId = Firebase.auth.currentUser?.uid ?: ""

        val chatAdapter = ChatAdapter()

        //db 조회 두번 : mine, other
        Firebase.database.reference.child(Key.DB_USERS).child(myUserId).get()
            .addOnSuccessListener {
                val myUserItem = it.getValue(UserItem::class.java)
                myUserName = myUserItem?.username ?: ""
            }
        Firebase.database.reference.child(Key.DB_USERS).child(otherUserId).get()
            .addOnSuccessListener {
                val otherUserItem = it.getValue(UserItem::class.java)
                chatAdapter.otherUserItem = otherUserItem
            }

        //chatting 가져오기
        Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId).addChildEventListener(
            object : ChildEventListener {
                /**아이템 하나하나 호출됨*/
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val chatItem = snapshot.getValue(ChatItem::class.java)
                    chatItem ?: return

                    chatItemList.add(chatItem)
                    chatAdapter.submitList(chatItemList)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }

            }
        )

        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }

        binding.sendButton.setOnClickListener {
            val message = binding.messageEditText.text.toString()

            if (message.isEmpty()) {
                return@setOnClickListener
            }
            val newChatItem = ChatItem(
                message = message,
                userId = myUserId,

                )

            Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId).push().apply {
                newChatItem.chatId = key // 자동 키생성
                setValue(newChatItem)
            }
            val updates: MutableMap<String, Any> = hashMapOf(
                "${Key.DB_CHAT_ROOMS}/$myUserId/$otherUserId/lastMessage" to message,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/lastMessage" to message,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/chatRoomId" to chatRoomId, //내가 처음 생성하는 것임
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserId" to myUserId,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserName" to myUserName,
            )

            Firebase.database.reference.updateChildren(updates)

            binding.messageEditText.text.clear()
        }
    }

    companion object {
        const val EXTRA_CHAT_ROOM_ID = "chatRoomId"
        const val EXTRA_OTHER_USER_ID = "otherUserId"
    }
}