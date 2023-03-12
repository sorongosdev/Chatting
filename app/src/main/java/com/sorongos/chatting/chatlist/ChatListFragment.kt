package com.sorongos.chatting.chatlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sorongos.chatting.R
import com.sorongos.chatting.databinding.FragmentChatroomlistBinding

class ChatListFragment : Fragment(R.layout.fragment_chatroomlist) {
    private lateinit var binding : FragmentChatroomlistBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatroomlistBinding.bind(view)

        val chatListAdapter = ChatAdapter()
        binding.chatListRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            adapter = chatListAdapter
        }

        chatListAdapter.submitList(
            mutableListOf<ChatRoomItem?>().apply{
                add(ChatRoomItem("11","22","33"))
            }
        )
    }
}