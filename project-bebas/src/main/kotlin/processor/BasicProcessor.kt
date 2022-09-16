package processor

import io.smallrye.mutiny.Uni
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

abstract class BasicProcessor<T> {
    private val queueData = LinkedBlockingQueue<T>()
    private val isProcessing = AtomicBoolean(false)

    open fun putDatas(list: List<T>) {
        try {
            queueData.addAll(list)
            if (isProcessing.get().not()) processData()
        } catch (exc: Exception) {
            println(exc.localizedMessage)
        }
    }

    open fun putData(item: T) {
        try {
            queueData.add(item)
            if( isProcessing.get().not() ) processData()
        } catch (exc: Exception) {
            println(exc.localizedMessage)
        }
    }

    open fun startProcess() {
        //
    }

    open fun stopProcess() {
        //
    }

    private fun processData() {
        isProcessing.set(true)
        Uni.createFrom().item { drainQueue() }
            .onTermination().invoke(Runnable { isProcessing.set(false) })
            .subscribe().with {  }
    }

    protected open fun drainQueue() {
        try {
            val mutableList = mutableListOf<T>()
            while ( queueData.isNotEmpty() ) {
                queueData.drainTo(mutableList)
                processListData(mutableList)
                mutableList.clear()
            }
        } catch (exc: Exception) {
            println(exc.localizedMessage)
        }
    }

    fun clean() {
        queueData.clear()
    }

    protected abstract fun processListData(list: List<T>)
}