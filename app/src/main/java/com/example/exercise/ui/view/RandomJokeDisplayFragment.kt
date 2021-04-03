package com.example.exercise.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.exercise.R
import com.example.exercise.ui.view.JokesDisplayMainFragment.Companion.ARG_JOKE

class RandomJokeDisplayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.joke_display, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_JOKE) }?.apply {
            val textView: TextView = view.findViewById(R.id.textViewJoke)
            textView.text = getString(ARG_JOKE).toString()
        }
    }

}