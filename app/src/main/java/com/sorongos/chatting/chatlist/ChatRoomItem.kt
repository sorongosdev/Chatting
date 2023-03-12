package com.sorongos.chatting.chatlist

data class ChatRoomItem(
    val chatRoomId: String,
    val otherUserName: String,
    val lastMessage: String
)