package lab2.workers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class WorkerThread extends Thread {
    public final BlockingQueue<WorkItem> queue;

    public WorkerThread(BlockingQueue<WorkItem> queue) {
        this.queue = queue;
    }

    @Override
    public synchronized void run() {
        while (true) {
            WorkItem workItem = queue.poll();
            if (workItem == null) {
                notifyAll();
                return;
            }
            workItem.apply();

        }
    }
}
