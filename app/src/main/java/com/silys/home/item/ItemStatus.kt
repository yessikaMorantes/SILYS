package com.silys.home.item
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemStatus(val statusName: String, val bgColor: Int, val textColor: Int) : Parcelable