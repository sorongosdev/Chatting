package com.sorongos.chatting.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sorongos.chatting.R
import com.sorongos.chatting.databinding.FragmentUserlistBinding

class UserFragment : Fragment(R.layout.fragment_userlist) {
    private lateinit var binding : FragmentUserlistBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserlistBinding.bind(view)

        val userListAdapter = UserAdapter()
        binding.userListRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
        }
    }
}