package com.barran.gank

import com.barran.gank.utils.FORMAT_YMD_HMS
import com.barran.gank.utils.dateFormat
import kotlinx.coroutines.experimental.*
import org.junit.Test
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.experimental.*

/**
 * 协程测试代码
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
        log("testMain ${Thread.currentThread().name}")
        runBlocking {
            val job = launch(CommonPool) {
                log("coroutine ${Thread.currentThread().name}")
                try {
                    delay(1000L)
                    log("world")
                }catch (ex: Exception){
                    log("catch exception")
                    ex.printStackTrace()
                }finally {
                    log("finally")
                }
            }
            log("hello")
            job.join()
//            job.cancel(IllegalAccessException("cancel")) // cancel 并抛异常
        }
//        testMain main
//        hello
//        coroutine ForkJoinPool.commonPool-worker-1
//        world
//        finally

    }

    @Test
    fun testCancel() {
        runBlocking {
            val job = launch(CommonPool) {
                var count = 0
//                while (true) {
                while (isActive){
                    // something
                    count++
                    if (count % 1000 == 0) {
                        log("count : $count")
                    }
                }
            }
            log("start count")
//            job.join()
            Thread.sleep(10L)
            job.cancel(/*IllegalAccessException("cancel")*/)
            Thread.sleep(2000L)
        }

        // output
        // 当使用while true循环时，会一直输出log到sleep(2s)结束，count到了317091000（累加了2s的结果）
        // 当使用isActive作为循环条件时，count到了880000（累加了10ms的结果）
    }

    @Test
    fun testManyJobs() {
        runBlocking {
            val jobs = List(100000) {
                launch(CommonPool) {
                    delay(10L)
                    log("job $it @thread(${Thread.currentThread().name})")
                }
            }
            jobs.forEach {
                it.join()
//                it::join // 不能使用这种用法，因为it.join是调用suspend方法
            }
        }
        // log  协程开销很小，会管理并复用线程，如果是创建线程则会出错：OutOfMemoryError
//        job 54180 @thread(ForkJoinPool.commonPool-worker-1)
//        job 81191 @thread(ForkJoinPool.commonPool-worker-3)
//        job 81192 @thread(ForkJoinPool.commonPool-worker-2)
    }

    @Test
    fun testDeferred() {
        runBlocking {
            // defer rename to async
//        val deferred = defer(CommonPool){
//            val deferred1 = async(CommonPool) {
            val deferred1 = lazyDefer(CommonPool) {
                log("deferred1 start ${System.currentTimeMillis().dateFormat(FORMAT_YMD_HMS)}")
                delay(1000L)
                log("deferred1 end   ${System.currentTimeMillis().dateFormat(FORMAT_YMD_HMS)}")
                111
            }

//            val deferred2 = async(CommonPool) {
            val deferred2 = lazyDefer(CommonPool) {
                log("deferred2 start ${System.currentTimeMillis().dateFormat(FORMAT_YMD_HMS)}")
                delay(5000L)
                log("deferred2 end   ${System.currentTimeMillis().dateFormat(FORMAT_YMD_HMS)}")
                222
            }
            Thread.sleep(2000L)
            log("start time      ${System.currentTimeMillis().dateFormat(FORMAT_YMD_HMS)}")
            log("result : ${deferred1.await()}   ${deferred2.await()}")
        }
        // log
//        deferred1 start 2017/10/16 16:19:35
//        deferred2 start 2017/10/16 16:19:35
//        deferred1 end   2017/10/16 16:19:36
//        start time      2017/10/16 16:19:37
//        deferred2 end   2017/10/16 16:19:40
//        result : 111   222

        // lazy
//        start time      2017/10/16 16:21:33
//        deferred1 start 2017/10/16 16:21:33
//        deferred1 end   2017/10/16 16:21:34
//        deferred2 start 2017/10/16 16:21:34
//        deferred2 end   2017/10/16 16:21:39
//        result : 111   222
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

    // 测试生成器
    @Test
    fun testGenerator() {
        val fibonacci = buildSequence {
            log("start")
            yield(1) // first Fibonacci number
            var cur = 1
            var next = 1
            while (true) {
                log("loop $cur  $next")
                yield(next) // next Fibonacci number
                val tmp = cur + next
                cur = next
                next = tmp
            }
        }

        for (i in fibonacci) {
            log("$i")
            if (i > 10000) {
                break
            }
        }

//        start
//        1
//        loop 1  1
//        1
//        loop 1  2
//        2
//        loop 2  3
//        3
//        loop 3  5
//        5
//        loop 5  8
//        8
//        loop 8  13
//        13
//        loop 13  21
//        21
//        ......
    }

    // ————————————————下面是基础api————————————————————————

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
    }

}
