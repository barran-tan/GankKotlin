package com.barran.gank.app

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.SparseArray
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.barran.gank.R
import com.barran.gank.service.beans.GankDataType

/**
 * @author tanwei
 */
class MainActivity : AppCompatActivity() {

    private lateinit var fragments: SparseArray<Fragment>

    private lateinit var drawerLayout: DrawerLayout

    val typeArray = arrayOf(GankDataType.ANDROID.ordinal, GankDataType.IOS.ordinal, GankDataType.PASTTIME.ordinal
            , GankDataType.EXPANDINFOMATION.ordinal, GankDataType.FRONTEND.ordinal, GankDataType.RECOMMEND.ordinal)

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

        findViewById(R.id.activity_main_fab).setOnClickListener {
            startActivity(Intent(
                    this,
                    ImagesActivity::class.java
            ))
        }

        // init drawer menu
        initDrawerMenu()
    }

    private fun initViewPager(viewPager: ViewPager) {
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                val fragment: Fragment
                if (fragments.get(position) != null) {
                    fragment = fragments.get(position)
                } else {
                    fragment =
                            when (typeArray[position]) {
                                GankDataType.ANDROID.ordinal -> DataListFragment(typeArray[position])
                                GankDataType.IOS.ordinal -> DataListFragment(typeArray[position])
                                GankDataType.PASTTIME.ordinal -> DataListFragment(typeArray[position])
                                GankDataType.EXPANDINFOMATION.ordinal -> DataListFragment(typeArray[position])
                                GankDataType.FRONTEND.ordinal -> DataListFragment(typeArray[position])
                                GankDataType.RECOMMEND.ordinal -> DataListFragment(typeArray[position])
                                else -> DataListFragment(GankDataType.ANDROID.ordinal)
                            }
                    fragments.put(position, fragment)
                }

                return fragment
            }

            override fun getCount(): Int = typeArray.size

            override fun getPageTitle(position: Int): CharSequence =
                    GankDataType.getName(typeArray[position])
        }
    }

    private fun initDrawerMenu() {

        drawerLayout = findViewById(
                R.id.activity_main_drawer) as DrawerLayout
        val navigationView = findViewById(
                R.id.activity_main_navigation_view) as NavigationView
        navigationView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.menu_navigation_history -> Toast.makeText(this, "history", Toast.LENGTH_SHORT).show()

                R.id.menu_navigation_favorite -> Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show()

                R.id.menu_navigation_about -> startActivity(Intent(this, AboutActivity::class.java))

            }

            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_history -> startActivity(Intent(this, HistoryDatesActivity::class.java))
            android.R.id.home -> onBackPressed()
            R.id.menu_menu ->
                if (drawerLayout.isDrawerOpen(Gravity.START))
                    drawerLayout.closeDrawer(Gravity.START)
                else
                    drawerLayout.openDrawer(Gravity.START)
        }
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START)
            return
        }
        super.onBackPressed()
    }
}
