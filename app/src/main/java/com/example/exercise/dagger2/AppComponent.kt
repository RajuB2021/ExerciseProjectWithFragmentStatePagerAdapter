package com.example.exercise.dagger2

import com.example.exercise.ui.view.JokesDisplayMainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RandomRepositoryModule::class])
interface AppComponent {

    fun inject(target: JokesDisplayMainFragment)
}