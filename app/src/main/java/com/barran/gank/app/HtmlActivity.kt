package com.barran.gank.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import com.barran.gank.R
import com.barran.gank.libs.greendao.DataCache

class HtmlActivity : AppCompatActivity() {

    private var url: String? = null
    private var favored = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_html)
        setSupportActionBar(findViewById<Toolbar>(R.id.activity_html_toolbar))

        val web = findViewById<WebView>(R.id.activity_html_web)
        if (intent?.hasExtra(EXTRA_URL) == true) {
            url = intent.getStringExtra(EXTRA_URL)
            web.loadUrl(url)
        }

        if (url == null) {
            finish()
        }

        favored = DataCache.cache.isFavored(url!!)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_html, menu)
        val item = menu?.findItem(R.id.menu_favorite)
        if (item != null) {
            item.icon = resources.getDrawable(if (favored) R.mipmap.favored else R.mipmap.favor)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_favorite -> {
                favored = !favored
                DataCache.cache.setFavored(url!!, favored)
                item.icon = resources.getDrawable(if (favored) R.mipmap.favored else R.mipmap.favor)
            }
            android.R.id.home -> onBackPressed()
            R.id.menu_open_in_web ->
                viewInWeb(this, url)
        }
        return true
    }
}
