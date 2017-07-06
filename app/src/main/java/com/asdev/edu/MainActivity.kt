package com.asdev.edu

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.asdev.edu.fragments.FragmentCollections
import com.asdev.edu.fragments.FragmentCreate
import com.asdev.edu.fragments.FragmentHome
import com.asdev.edu.fragments.FragmentProfile
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fragments = arrayOf(FragmentHome(), FragmentCollections(), FragmentProfile(), FragmentCreate())

    private val navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        // make sure it isn't already selected
        if(item.itemId == navigation.selectedItemId)
            return@OnNavigationItemSelectedListener false

        val fragment: Fragment = when (item.itemId) {
                R.id.navigation_home -> {
                    fragments[0]
                }
                R.id.navigation_collections -> {
                    fragments[1]
                }
                R.id.navigation_profile -> {
                    fragments[2]
                }
                R.id.navigation_create -> {
                    fragments[3]
                }
                else -> fragments[0]
            }

        // do a transaction of the new fragment
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                .replace(R.id.content, fragment)
                .commit()

        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

        // do the initial fragment
        supportFragmentManager.beginTransaction()
                .add(R.id.content, fragments[0])
                .commit()

//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
//
//        // disable shifting
//        for (int i = 0; i < menuView.getChildCount(); i++) {
//            BottomNavigationItemView child = (BottomNavigationItemView) menuView.getChildAt(i);
//            child.setShiftingMode(false);
//            child.setChecked(false);
//        }
    }

}
