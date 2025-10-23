package com.example.facesecure

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.facesecure.data.local.AppDatabase
import com.example.facesecure.data.local.UserDao
import com.example.facesecure.data.model.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDatabaseTest {

    private lateinit var db: AppDatabase
    private lateinit var userDao: com.example.facesecure.data.local.UserDao

    @Before
    fun createDb() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertUser() = runBlocking {

        val user = User(
            email = "test",
            password = "123",
            faceEmbedding = listOf(0.1f, 0.2f, 0.3f)
        )

        userDao.insertUser(user)

        val users = userDao.getAll()
        assertEquals(1, users.size)
        assertEquals("test@example.com", users[0].email)
        assertEquals(listOf(0.1f, 0.2f, 0.3f), users[0].faceEmbedding)

    }

}