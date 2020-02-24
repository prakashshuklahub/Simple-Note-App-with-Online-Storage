package com.languagexx.simplenotes.di

import androidx.lifecycle.ViewModel

import com.languagexx.simplenotes.ui.main.NoteViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    // Method #1
    @Binds
    @IntoMap
    @ViewModelKey(NoteViewModel::class)
    abstract fun bindMainViewModel(moviesViewModel: NoteViewModel): ViewModel
}
