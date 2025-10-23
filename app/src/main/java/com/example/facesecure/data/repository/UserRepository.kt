package com.example.facesecure.data.repository
import com.example.facesecure.data.local.UserDao
import com.example.facesecure.data.model.User

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun getAllUsers(): List<User> = userDao.getAll()

}