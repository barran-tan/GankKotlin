package com.barran.gank.libs.greendao

import com.barran.gank.api.beans.DataInfo
import com.barran.gank.app.App


/**
 * 缓存数据
 *
 * Created by tanwei on 2017/10/17.
 */

class DataCache private constructor() {

    companion object {
        val cache = DataCache()
        const val dbName = "db"
    }

    private val dbName = "test_db"
    private var openHelper: DaoMaster.DevOpenHelper

    init {
        openHelper = DaoMaster.DevOpenHelper(App.appContext, dbName, null)
    }

    fun getFavoriteDataList(): List<DataInfo> {
        val list = ArrayList<DataInfo>()

        val daoMaster = DaoMaster(openHelper.readableDatabase)
        val daoSession = daoMaster.newSession()
        val infoDao = daoSession.dataInfoEntityDao
        val qb = infoDao.queryBuilder()
        qb.orderAsc(DataInfoEntityDao.Properties.PublishTime)
        qb.list().forEach { list.add(DataInfo(it)) }
        daoSession.clear()
        return list
    }

    fun insertHistoryData(data: DataInfo) {
        val daoMaster = DaoMaster(openHelper.writableDatabase)
        val daoSession = daoMaster.newSession()
        val infoDao = daoSession.dataInfoEntityDao
        if (infoDao.queryBuilder().where(DataInfoEntityDao.Properties.InfoId.eq(data._id)).list().isEmpty()) {
            val infoEntity = DataInfoEntity(null, data._id, data.type, data.createAt?.time ?: 0L, data.publishedAt?.time ?: 0L, data.desc, data.url, data.who, data.images?.get(0), data.read)
            infoDao.insert(infoEntity)
        }
        daoSession.clear()
    }

    fun getHistoryDataList(): List<DataInfo> {
        val list = ArrayList<DataInfo>()

        val daoMaster = DaoMaster(openHelper.readableDatabase)
        val daoSession = daoMaster.newSession()
        val infoDao = daoSession.dataInfoEntityDao
        val qb = infoDao.queryBuilder()
        qb.orderAsc(DataInfoEntityDao.Properties.PublishTime)
        qb.list().forEach { list.add(DataInfo(it)) }
        daoSession.clear()
        return list
    }
}
