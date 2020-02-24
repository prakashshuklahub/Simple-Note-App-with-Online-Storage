package com.languagexx.simplenotes.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.languagexx.simplenotes.persistence.LoginDao
import com.languagexx.simplenotes.persistence.LoginToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SessionManager @Inject constructor(
    val loginDao: LoginDao
) {

    val loginToken: LiveData<LoginToken>
        get() = _loginToken

    private val _loginToken = MutableLiveData<LoginToken>()

    // Method #1
    fun setToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            loginDao.setToken(LoginToken(token))
            _loginToken.postValue(LoginToken(token))
        }
    }

    // Method #2
    fun deleteToken() {
        CoroutineScope(Dispatchers.IO).launch {
            loginDao.deleteToken()
        }
    }

    // Method #3
    fun getToken(): LiveData<LoginToken> {
        CoroutineScope(Dispatchers.IO).launch {
            _loginToken.postValue(loginDao.getToken())
        }
        return loginToken
    }
}