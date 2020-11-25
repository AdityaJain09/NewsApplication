package com.androiddevs.mvvmnewsapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentViewReusability : FragmentManager.FragmentLifecycleCallbacks()  {


    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
    }

}