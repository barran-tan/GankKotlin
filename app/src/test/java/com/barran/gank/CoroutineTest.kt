package com.barran.gank

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.experimental.*

/**
 * 携程测试代码
 *
 * Created by tanwei on 2017/10/14.
 */
class CoroutineTest {

    private val logs = ArrayList<String>()

    @Test
    fun test(){
//        logs.clear()
        launch(CommonPool) {
            delay(1000L)
            log("world")
        }
        log("hello")

        Thread.sleep(2000L)// TODO 添加sleep
    }

    @Test
    fun testMain() {
        runBlocking {
            launch(CommonPool) {
                delay(1000L)
                log("world")
            }
            log("hello")
        }
    }

    @Test
    fun testApi() {
//        logs.clear()
        log("before coroutine")
        asyncCalculateMD5("test.txt") {
            log("in coroutine, before suspend")
            val result: String = suspendCoroutine { continuation ->
                log("in suspend block")
                // 暂停线程，执行耗时操作
                continuation.resume(calculateMD5(continuation.context[FilePath]!!.path))
                log("after resume")
            }
            log("in coroutine, after suspend. result = $result")
        }
        log("after coroutine")

        // log
//        before coroutine
//        in coroutine, before suspend
//        in suspend block
//        calculateMD5 of test.txt @main
//        after resume
//        in coroutine, after suspend. result = 1507968204326
//        resume kotlin.Unit
//        after coroutine
    }

    @Test
    fun testApiAsync() {
//        logs.clear()
        log("before coroutine")
        val executor = Executors.newSingleThreadExecutor {
            Thread(it, "scheduler")
        }
        asyncCalculateMD5("test.txt") {
            log("in coroutine, before suspend")
            val result: String = suspendCoroutine { continuation ->
                log("in suspend block")
                // 暂停线程，执行耗时操作
                executor.submit {
                    continuation.resume(calculateMD5(continuation.context[FilePath]!!.path))
                    log("after resume")
                }
            }
            log("in coroutine, after suspend. result = $result")
            executor.shutdown()
        }
        log("after coroutine")
        Thread.sleep(3000)// TODO 添加sleep

        // log
//        before coroutine
//        in coroutine, before suspend
//        in suspend block
//        after coroutine
        // 下面这句log有时打印不出来
//        calculateMD5 of test.txt @scheduler
        // TODO 其他子线程的log也没看到
        // 通过测试，发现是由于主线程先于子线程结束了引起的，去掉子线程的sleep或者给主线程添加sleep都可以看到完整log
    }

    private fun asyncCalculateMD5(filePath: String, block: suspend () -> Unit) {
        val continuation = object : Continuation<Unit> {
            override val context: CoroutineContext
                get() = FilePath(filePath)

            override fun resume(value: Unit) {
                log("resume $value")
            }

            override fun resumeWithException(exception: Throwable) {
                log("exception : $exception")
            }
        }

        block.startCoroutine(continuation)
    }

    // 上下文，存放需要的信息，可自定义
    class FilePath(val path: String) : AbstractCoroutineContextElement(FilePath) {
        companion object Key : CoroutineContext.Key<FilePath>
    }

    private fun calculateMD5(filePath: String): String {
        log("calculateMD5 of $filePath @${Thread.currentThread().name}")
        // 模拟耗时操作
        Thread.sleep(1000)// TODO 去掉sleep
        return System.currentTimeMillis().toString()
    }

    fun log(log: String) {
        println(log)
//        logs.add(log)
//        println(logs)
//    val file = File("E:\\demo\\github\\log.txt")
//    if(file.exists()){
//        val stream = FileOutputStream(file)
//        stream.write(log.toByteArray())
//        stream.write("\n".toByteArray())
//        stream.flush()
//    }
    }

}
