package com.umc.playkuround.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMainBinding
import com.umc.playkuround.fragment.BadgeFragment
import com.umc.playkuround.fragment.HomeFragment
import com.umc.playkuround.fragment.MyPageFragment
import com.umc.playkuround.fragment.RankingFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pager.isUserInputEnabled = true
        binding.pager.adapter = ViewPagerAdapter(supportFragmentManager,lifecycle)
        binding.pager.registerOnPageChangeCallback(PageChangeCallback())
        binding.bottomNavigationView.setOnItemSelectedListener { navigationSelected(it) }

    }

    private fun navigationSelected(item: MenuItem): Boolean {
        val checked = item.setChecked(true)
        when (checked.itemId) {
            R.id.home_fragment-> {
                binding.pager.currentItem = 0
                return true
            }
            R.id.badge_fragment -> {
                binding.pager.currentItem = 1
                return true
            }
            R.id.ranking_fragment -> {
                binding.pager.currentItem = 2
                return true
            }
            R.id.mypage_fragment -> {
                binding.pager.currentItem = 3
                return true
            }
        }
        return false
    }

    private inner class ViewPagerAdapter (fragmentManager: FragmentManager,lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle){
        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> BadgeFragment()
                2 -> RankingFragment()
                3 -> MyPageFragment()
                else -> error("no such position: $position")
            }
        }

    }

    private inner class PageChangeCallback: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.bottomNavigationView.selectedItemId = when (position) {
                0 -> R.id.home_fragment
                1 -> R.id.badge_fragment
                2 -> R.id.ranking_fragment
                3 -> R.id.mypage_fragment
                else -> error("no such position: $position")
            }
        }
    }



}
