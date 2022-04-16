package com.example.book_hub.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName  = "books" )
data class BookEntity(
        @PrimaryKey val bookId : Int,
        @ColumnInfo(name ="book_name") val bookName : String,
        @ColumnInfo(name ="book_author") val bookAuthor : String,
        @ColumnInfo(name ="book_rating") val bookRating : String,
        @ColumnInfo(name ="book_price") val bookPrice : String,
        @ColumnInfo(name ="book_desc") val bookDesc : String,
        @ColumnInfo(name ="book_image") val bookImage : String
)

