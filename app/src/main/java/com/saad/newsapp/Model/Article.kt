package com.saad.newsapp.Model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.ParcelField
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.io.Serializable

@Entity(
    tableName = "articles"
)
@Parcelize
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id:Int? =null,
    val author: String?,
    val content: String? = "",
    val description: String? ="",
    val publishedAt: String? ="",
    val source:@RawValue Source? = null,
    val title: String? ="",
    val url: String? = "",
    val urlToImage: String?= ""
): Parcelable