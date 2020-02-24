package com.languagexx.simplenotes.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface LoginDao {

    // Method #1
    @Query("delete from tbl_login")
    fun deleteToken()

    // Method #2
    @Insert
    fun setToken(loginToken: LoginToken)

    // Method #3
    @Query("select * from tbl_login")
    fun getToken(): LoginToken
}