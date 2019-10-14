package com.barran.gank.app

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.SparseArray
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.barran.gank.R
import com.barran.gank.api.beans.GankDataType

/**
 * @author tanwei
 */
class MainActivity : AppCompatActivity() {

    private lateinit var fragments: SparseArray<Fragment>

    private lateinit var drawerLayout: DrawerLayout

    val typeArray = arrayOf(GankDataType.ANDROID.ordinal, GankDataType.IOS.ordinal, GankDataType.PAST_TIME.ordinal
            , GankDataType.EXPAND_INFORMATION.ordinal, GankDataType.FRONTEND.ordinal, GankDataType.RECOMMEND.ordinal)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragments = SparseArray(GankDataType.values().size)

        val tabLayout = findViewById<TabLayout>(R.id.activity_main_tab_layout)
        val viewPager = findViewById<ViewPager>(R.id.activity_main_tab_viewpager)
        initViewPager(viewPager)

        val toolBar = findViewById<Toolbar>(R.id.activity_main_toolbar)
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

        findViewById<View>(R.id.activity_main_fab).setOnClickListener {
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
                    val infoType =
                            when (typeArray[position]) {
                                GankDataType.ANDROID.ordinal -> GankDataType.ANDROID.ordinal
                                GankDataType.IOS.ordinal -> GankDataType.IOS.ordinal
                                GankDataType.PAST_TIME.ordinal -> GankDataType.PAST_TIME.ordinal
                                GankDataType.EXPAND_INFORMATION.ordinal -> GankDataType.EXPAND_INFORMATION.ordinal
                                GankDataType.FRONTEND.ordinal -> GankDataType.FRONTEND.ordinal
                                GankDataType.RECOMMEND.ordinal -> GankDataType.RECOMMEND.ordinal
                                else -> GankDataType.ANDROID.ordinal
                            }

                    fragment = DataListFragment()
                    val bundle = Bundle()
                    bundle.putInt(EXTRA_INFO_TYPE, infoType)
                    fragment.arguments = bundle
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
                R.id.activity_main_drawer)
        val navigationView = findViewById<NavigationView>(
                R.id.activity_main_navigation_view)
        navigationView.setNavigationItemSelectedListener { item ->

            drawerLayout.closeDrawer(Gravity.START, false)
            when (item.itemId) {
                R.id.menu_navigation_history -> startActivity(Intent(this, ViewedInfoListActivity::class.java))

                R.id.menu_navigation_favorite -> startActivity(Intent(this, FavoriteListActivity::class.java))

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
