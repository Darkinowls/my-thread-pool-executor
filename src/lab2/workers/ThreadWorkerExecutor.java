package lab2.workers;

import lab2.Result;
import lab2.producers.Producer;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ThreadWorkerExecutor {
    private final BlockingQueue<WorkerThread> threads;
    private final BlockingQueue<WorkItem> queue;

    public ThreadWorkerExecutor(int threadNum) {
        this.threads = new ArrayBlockingQueue<>(threadNum);
        queue = new LinkedBlockingQueue<>();
        IntStream.range(0, threadNum).forEach(num -> threads.add(new WorkerThread(queue)));
    }

    private WorkerThread getThread() {
        for (WorkerThread thread : threads) {
            if (thread.getState() == Thread.State.NEW) {
                return thread;
            }
        }
        while (true) {
            for (WorkerThread thread : threads) {
                if (thread.getState() == Thread.State.TERMINATED) {
                    threads.remove(thread);
                    WorkerThread workerThread = new WorkerThread(queue);
                    threads.add(workerThread);
                    return workerThread;
                }
            }
        }
    }

    public Result execute(Function<Producer, Integer> func, Producer item) {
        WorkItem workItem = new WorkItem(item, func);
        queue.add(workItem);
        getThread().start();
        return workItem.getResult();
    }

    public List<Result> map(Function<Producer, Integer> func, List<Producer> items) {
        return items.stream().map(item -> this.execute(func, item)).collect(Collectors.toList());
    }

    public void shutdown() throws InterruptedException {
        for (WorkerThread thread : threads) {
            thread.join();
            threads.remove(thread);
        }
    }

}
