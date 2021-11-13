package com.example.shoppinglist.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Item::class),version = 3)
abstract class AppDatabase {
    abstract fun itemDao(): ItemDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context):AppDatabase{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.applicationContext(),
                        AppDatabase::class.java,"items.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return  INSTANCE!!
        }
        fun destroyInstance(){
            INSTANCE = null
        }
    }
}