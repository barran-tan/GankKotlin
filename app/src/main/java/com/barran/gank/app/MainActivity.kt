package com.barran.gank.app

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem

import com.barran.gank.R
import com.barran.gank.service.beans.GankDataType

class MainActivity : AppCompatActivity() {

    lateinit var fragments: SparseArray<Fragment>

    val typeArray = arrayOf(GankDataType.ANDROID.ordinal, GankDataType.IOS.ordinal, GankDataType.PASTTIME.ordinal
            , GankDataType.EXPANDINFOMATION.ordinal, GankDataType.FRONTEND.ordinal, GankDataType.RECOMMEND.ordinal, GankDataType.APP.ordinal)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragments = SparseArray(GankDataType.values().size)

        val tabLayout = findViewById(R.id.activity_main_tab_layout) as TabLayout
        val viewPager = findViewById(R.id.activity_main_tab_viewpager) as ViewPager
        initViewPager(viewPager)

        val toolBar = findViewById(R.id.activity_main_toolbar) as Toolbar
        setSupportActionBar(toolBar)

        // 绑定到viewpager
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                toolBar.title = tab?.text
            }
        })

        findViewById(R.id.activity_main_fab).setOnClickListener {  }
    }

    private fun initViewPager(viewPager: ViewPager) {
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                val fragment: Fragment
                if (fragments.get(position) != null) {
                    fragment = fragments.get(position)
                } else {
                    fragment =
                            when (position) {
                                GankDataType.ANDROID.ordinal -> DataListFragment(position)
                                GankDataType.IOS.ordinal -> DataListFragment(position)
                                GankDataType.PASTTIME.ordinal -> DataListFragment(position)
                                GankDataType.EXPANDINFOMATION.ordinal -> DataListFragment(position)
                                GankDataType.FRONTEND.ordinal -> DataListFragment(position)
                                GankDataType.RECOMMEND.ordinal -> DataListFragment(position)
                                GankDataType.APP.ordinal -> DataListFragment(position)
                                else -> DataListFragment(GankDataType.ANDROID.ordinal)
                            }
                    fragments.put(position, fragment)
                }

                return fragment
            }

            override fun getCount(): Int = typeArray.size

            override fun getPageTitle(position: Int): CharSequence =
                    GankDataType.getName(position)
        }
    }

    private fun loadData() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.menu_history) {
            val intent = Intent(this, HistoryDatesActivity::class.java)
            startActivity(intent)
        }
        return true
    }
}
