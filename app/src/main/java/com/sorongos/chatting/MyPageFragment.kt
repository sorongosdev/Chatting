package com.sorongos.chatting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.sorongos.chatting.databinding.FragmentMypageBinding

class MyPageFragment : Fragment(R.layout.fragment_userlist) {
    private lateinit var binding : FragmentMypageBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMypageBinding.bind(view)
    }
}