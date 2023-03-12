package com.sorongos.chatting.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.sorongos.chatting.R
import com.sorongos.chatting.databinding.FragmentMypageBinding

class MyPageFragment : Fragment(R.layout.fragment_mypage) {
    private lateinit var binding : FragmentMypageBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMypageBinding.bind(view)

        
    }
}