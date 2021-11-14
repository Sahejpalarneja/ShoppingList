package com.example.shoppinglist.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Item")
data class Item(
    @PrimaryKey(autoGenerate = true) var ItemId: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "status") var done: Boolean,
    @ColumnInfo(name = "description") var desc: String?,
    @ColumnInfo(name = "category") var category: Int,
    @ColumnInfo(name = "price") var price: Float?,
    @ColumnInfo(name = "currencies") var currencies: List<Float>
) :Serializable