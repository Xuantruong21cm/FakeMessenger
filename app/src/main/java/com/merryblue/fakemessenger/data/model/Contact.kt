package com.merryblue.fakemessenger.data.model

data class Contact(
    val contactId: Int,
    val picturePath: String,
    val name: String,
    val isFavourite: Boolean,
    val isOnline: Boolean,
    val isStory: Boolean,
    val isMute: Boolean,
    val isBlock: Boolean,
    val isTheme: Boolean,
    val isFamous: Boolean,
    val isFriend: Boolean,
    val education: String,
    val work: String,
    val numberOfLikes: Int,
    val category: String,
    val bgColor : String,
    val lastMessage : String,
    val time : String
)