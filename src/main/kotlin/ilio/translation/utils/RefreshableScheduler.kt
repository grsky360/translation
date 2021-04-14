package ilio.translation.utils

import java.util.concurrent.*

@Suppress("UNCHECKED_CAST")
class RefreshableScheduler(poolSize: Int = Int.MAX_VALUE) {
    private val queue = DelayQueue<RefreshableTask.RefreshableTaskJob>()
    private val executor = ThreadPoolExecutor(
        0, poolSize, 0, TimeUnit.MILLISECONDS,
        queue as BlockingQueue<Runnable>
    )

    fun newTask(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, block: () -> Unit): RefreshableTask {
        return RefreshableTask(delay, timeUnit, block)
    }

    inner class RefreshableTask(
        private var delay: Long, private val timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
        val block: () -> Unit
    ) : Refreshable {
        @Volatile
        private var job: RefreshableTaskJob? = null

        override fun refresh() {
            synchronized(this) {
                job?.cancel()
                job = null
                start()
            }
        }

        private fun start() {
            synchronized(this) {
                val job = RefreshableTaskJob()
                this@RefreshableScheduler.executor.execute(job)
                this.job = job
            }
        }

        inner class RefreshableTaskJob : Delayed, Runnable {

            @Volatile
            internal var init = System.currentTimeMillis()

            @Volatile
            private var canceled: Boolean = false

            fun cancel() {
                canceled = true
            }

            override fun compareTo(other: Delayed): Int {
                return delay.compareTo(other.getDelay(timeUnit))
            }

            override fun getDelay(unit: TimeUnit): Long {
                val remain = init + TimeUnit.MILLISECONDS.convert(delay, timeUnit) - System.currentTimeMillis()
                return unit.convert(remain, TimeUnit.MILLISECONDS)
            }

            override fun run() {
                if (!canceled) {
                    block()
                }
            }
        }
    }
}

interface Refreshable {
    fun refresh()
}

