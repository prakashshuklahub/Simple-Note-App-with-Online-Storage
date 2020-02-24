package com.languagexx.simplenotes.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.languagexx.simplenotes.R
import com.languagexx.simplenotes.session.SessionManager
import com.languagexx.simplenotes.ui.main.MainActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {


    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject
    lateinit var gso: GoogleSignInOptions


    @Inject
    lateinit var sessionManager: SessionManager

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    // Method #1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        SkipLoginIfUserExist()

        //Login button will fire an intent and will wait for the result in onActivityResult Method
        sign_in_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    // Method #2
    // Will  get the result from Intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //This was our request #9001
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Success case
                //Go to Main Activity with Loged in User
                val account = task.getResult(ApiException::class.java)
                Toast.makeText(this, account?.displayName, Toast.LENGTH_SHORT).show()
                sessionManager.setToken(account?.id.toString())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: ApiException) {
                //Error case
                Toast.makeText(this, "error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method #3
    // This method will check if the user is already exist in database , so we do not need to show user
    // login activity
    fun SkipLoginIfUserExist() {
        sessionManager.getToken().observe(this, Observer {
            it?.let {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }


}

