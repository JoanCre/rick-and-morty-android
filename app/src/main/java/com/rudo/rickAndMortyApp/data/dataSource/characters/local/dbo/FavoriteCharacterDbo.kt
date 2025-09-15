package com.rudo.rickAndMortyApp.data.dataSource.characters.local.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room DBO (Database Object) representing a complete favorite character in the local database.
 * Enhanced approach - stores full character data to reduce API calls and improve performance.
 * Follows Single Responsibility Principle - handles favorite character persistence with full data.
 */
@Entity(tableName = "favorite_characters")
data class FavoriteCharacterDbo(
    @PrimaryKey
    val id: Int,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "status")
    val status: String,
    
    @ColumnInfo(name = "species")
    val species: String,
    
    @ColumnInfo(name = "type")
    val type: String,
    
    @ColumnInfo(name = "gender")
    val gender: String,
    
    @ColumnInfo(name = "origin")
    val origin: String,
    
    @ColumnInfo(name = "location")
    val location: String,
    
    @ColumnInfo(name = "image")
    val image: String,
    
    @ColumnInfo(name = "episodes")
    val episodes: List<String>, // Episode URLs with TypeConverter
    
    @ColumnInfo(name = "url")
    val url: String,
    
    @ColumnInfo(name = "created")
    val created: String,
    
    @ColumnInfo(name = "added_date")
    val addedDate: Long = System.currentTimeMillis()
)
