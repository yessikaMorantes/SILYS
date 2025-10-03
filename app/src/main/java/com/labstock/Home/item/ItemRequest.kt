package com.labstock.Home.item
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemRequest(
    val projectName: String,
    val requestCode: String,
    val date: String,
    val topic: String,
    val lab: String,
    val description: String,
    val status: ItemStatus,
    val img: Int
) : Parcelable