package com.sorongos.chatting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sorongos.chatting.databinding.FragmentChatroomlistBinding
import com.sorongos.chatting.databinding.FragmentUserlistBinding
import com.sorongos.chatting.user.UserAdapter

class ChatroomFragment : Fragment(R.layout.fragment_userlist) {
    private lateinit var binding : FragmentChatroomlistBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatroomlistBinding.bind(view)
    }
}