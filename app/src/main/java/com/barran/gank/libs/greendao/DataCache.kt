package com.barran.gank.libs.greendao

import com.barran.gank.api.beans.DataInfo
import com.barran.gank.app.App


/**
 * 缓存数据,记录历史浏览以及收藏文章等
 *
 * Created by tanwei on 2017/10/17.
 */

class DataCache private constructor() {

    companion object {
        val cache = DataCache()
        const val dbName = "db"
    }

    private var openHelper: DaoMaster.DevOpenHelper

    init {
        openHelper = DaoMaster.DevOpenHelper(App.appContext, dbName, null)
    }

    fun setFavored(url: String, favored: Boolean) {
        val daoMaster = DaoMaster(openHelper.writableDatabase)
        val daoSession = daoMaster.newSession()
        val infoDao = daoSession.dataInfoEntityDao
        val qb = infoDao.queryBuilder().where(DataInfoEntityDao.Properties.LinkUrl.eq(url), DataInfoEntityDao.Properties.Favored.notEq(favored))
        if (!qb.list().isEmpty()) {
            val entity = qb.list().first()
            entity.favored = favored
            infoDao.update(entity)
        }
        daoSession.clear()
    }

    fun isFavored(url: String): Boolean {

        var favored = false

        val daoMaster = DaoMaster(openHelper.writableDatabase)
        val daoSession = daoMaster.newSession()
        val infoDao = daoSession.dataInfoEntityDao
        val qb = infoDao.queryBuilder().where(DataInfoEntityDao.Properties.LinkUrl.eq(url))
        if (!qb.list().isEmpty()) {
            val entity = qb.list().first()
            favored = entity.favored
        }
        daoSession.clear()

        return favored
    }

    fun getFavoriteDataList(): List<DataInfo> {
        val list = ArrayList<DataInfo>()

        val daoMaster = DaoMaster(openHelper.readableDatabase)
        val daoSession = daoMaster.newSession()
        val infoDao = daoSession.dataInfoEntityDao
        val qb = infoDao.queryBuilder()
        qb.orderDesc(DataInfoEntityDao.Properties.PublishTime)
        qb.where(DataInfoEntityDao.Properties.Favored.eq(true))
        qb.list().forEach { list.add(DataInfo(it)) }
        daoSession.clear()
        return list
    }

    fun insertHistoryData(data: DataInfo) {
        val daoMaster = DaoMaster(openHelper.writableDatabase)
        val daoSession = daoMaster.newSession()
        val infoDao = daoSession.dataInfoEntityDao
        if (infoDao.queryBuilder().where(DataInfoEntityDao.Properties.InfoId.eq(data._id)).list().isEmpty()) {
            val infoEntity = DataInfoEntity(null, data._id, data.type, data.createAt?.time ?: 0L, data.publishedAt?.time ?: 0L, data.desc, data.url, data.who, data.images?.get(0), data.read, data.favored)
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
        qb.orderDesc(DataInfoEntityDao.Properties.PublishTime)
        qb.list().forEach { list.add(DataInfo(it)) }
        daoSession.clear()
        return list
    }
}
