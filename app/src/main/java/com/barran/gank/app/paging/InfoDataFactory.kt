package com.barran.gank.app.paging

import androidx.paging.DataSource
import com.barran.gank.api.beans.DataInfo

/**
 * DataSource Factory
 *
 * create by tanwei@bigo.sg
 * on 2019/12/12
 */
class InfoDataFactory(private val type: String) : DataSource.Factory<Int, DataInfo>() {
    override fun create(): DataSource<Int, DataInfo> = InfoDataSource(type)
}