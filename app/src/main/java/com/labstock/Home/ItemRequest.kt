package com.labstock.Home

data class ItemRequest(
    val projectName: String,
    val requestCode: String,
    val date: String,
    val topic: String,
    val lab: String,
    val description: String,
    val status: ItemStatus,
    val img: Int
)