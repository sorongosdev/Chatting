package com.sorongos.chatting.chatdetail

data class ChatItem (
    val chatId: String? = null, //채팅의 아이디
    val userId: String? = null, //유저 아이디
    val message: String? = null //실제 무슨 대화
)