package com.sorongos.chatting.chatlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sorongos.chatting.R
import com.sorongos.chatting.databinding.FragmentUserlistBinding

class ChatFragment : Fragment(R.layout.fragment_userlist) {
    private lateinit var binding : FragmentUserlistBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserlistBinding.bind(view)

        val userListAdapter = ChatAdapter()
        binding.userListRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }

        userListAdapter.submitList(
            mutableListOf<ChatRoomItem?>().apply{
                add(ChatRoomItem("11","22","33"))
            }
        )
    }
}