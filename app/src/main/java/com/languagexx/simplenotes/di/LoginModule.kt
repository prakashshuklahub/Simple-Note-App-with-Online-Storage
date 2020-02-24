package com.languagexx.simplenotes.di

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.languagexx.simplenotes.R
import dagger.Module
import dagger.Provides

//This module is part of subcomponent LoginActivity
@Module
class LoginModule {

    // Method #1
    @Provides
    fun providesGso(app: Application): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(app.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    // Method #2
    @Provides
    fun providesGoogleSignInClients(
        gso: GoogleSignInOptions,
        app: Application
    ): GoogleSignInClient {
        return GoogleSignIn.getClient(app, gso)
    }
}
