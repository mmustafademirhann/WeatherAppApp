package com.example.vbchatgptweatherapp.presentation // Adjust package name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.vbchatgptweatherapp.R // Adjust import

class WelcomeLoadFragment  : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome_load, container, false) // Adjust layout name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val welcomeTextView = view.findViewById<TextView>(R.id.welcomeTextView)

        // Create a TranslateAnimation
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, // Start from the left
            Animation.RELATIVE_TO_SELF, 0f, // Move to the right (off-screen)
            Animation.RELATIVE_TO_SELF, 0f, // No vertical movement
            Animation.RELATIVE_TO_SELF, 10f
        )
        animation.duration = 7000 // Animation duration (2 seconds)
        animation.repeatCount = Animation.INFINITE // Repeat indefinitely
        animation.repeatMode = Animation.RESTART // Restart from the beginning

        // Start the animation
        welcomeTextView.startAnimation(animation)

    }
}