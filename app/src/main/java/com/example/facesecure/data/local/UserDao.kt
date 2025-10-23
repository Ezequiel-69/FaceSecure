package com.example.facesecure.data.local

import androidx.room.*
import com.example.facesecure.data.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

}