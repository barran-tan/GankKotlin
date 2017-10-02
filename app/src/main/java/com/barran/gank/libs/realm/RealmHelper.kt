//package com.barran.gank.libs.realm
//
//import com.barran.gank.app.App
//import io.realm.Realm
//import io.realm.RealmConfiguration
//
//
///**
// * 封装realm操作
// *
// * Created by tanwei on 2017/9/29.
// */
//object RealmHelper {
//
//    private val version = 1L
//    private val name = "data"
//
//    var config: RealmConfiguration
//
//    init {
//        Realm.init(App.appContext)
//        config = RealmConfiguration.Builder().name(name).schemaVersion(version).build()
//    }
//
//    fun get(): Realm {
//        return Realm.getInstance(config)
//    }
//
//}