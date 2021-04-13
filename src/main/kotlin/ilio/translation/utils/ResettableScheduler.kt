package ilio.translation.utils

import java.util.concurrent.Future
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
class ResettableScheduler {
    internal val scheduler = ScheduledThreadPoolExecutor(Int.MAX_VALUE)

    fun newTask(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, block: () -> Unit): ResettableTask {
        return this.ResettableTask(delay, timeUnit, block)
    }

    inner class ResettableTask internal constructor(
        private var delay: Long, private val timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
        private val block: () -> Unit
    ) {
        @Volatile
        private var task: FutureTask? = null

        fun reset() {
            task?.future?.cancel(true)
            scheduler.queue.remove(task?.future)
            start()
        }

        fun start() {
            val task = FutureTask(block)
            val future = this@ResettableScheduler.scheduler.schedule(task, delay, timeUnit)
            task.future = future
            this.task = task
        }
    }
}

data class FutureTask(private val block: () -> Unit) : Runnable {
    lateinit var future: Future<*>
    override fun run() {
        if (!future.isCancelled) {
            block()
        }
    }
}
