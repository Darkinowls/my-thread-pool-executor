package lab2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Result {
    private Integer result;
    private final Lock lock;
    private final Condition condition;

    public Result() {
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public Integer getResult() {
        this.lock.lock();
        if (this.result == null) {
            try {
                this.condition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public void setResult(Integer result) {
        this.lock.lock();
        this.result = result;
        this.condition.signal();
        this.lock.unlock();
    }

}
