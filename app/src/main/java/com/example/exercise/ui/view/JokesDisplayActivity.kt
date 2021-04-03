package com.example.exercise.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.exercise.R
import com.example.exercise.ui.util.CommonUtil

class JokesDisplayActivity : AppCompatActivity() {
    var jokesDisplayMainFragment: JokesDisplayMainFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.joke_display_activity_layout)
        val firstName:String? = intent.getStringExtra(CommonUtil.FIRST_NAME)
        val lastName:String? = intent.getStringExtra(CommonUtil.LAST_NAME)

         jokesDisplayMainFragment = supportFragmentManager.findFragmentById(R.id.jokesDisplayMainFragment) as JokesDisplayMainFragment?
           jokesDisplayMainFragment?.setFirstNameAndLastName(firstName!!,lastName!!)
    }

    
    override fun onBackPressed() {
       var callSuperMethod :Boolean? = true  
        // Uncommenting below code enables Viewpager to navigate to previous page if not reached to end, when user presses back key 
//       if(jokesDisplayMainFragment != null) {
//           callSuperMethod = jokesDisplayMainFragment?.onActivityBackPressed()
//       }
        if(callSuperMethod == true) {
            super.onBackPressed()
        }
    }
}