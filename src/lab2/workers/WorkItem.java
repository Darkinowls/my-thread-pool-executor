package lab2.workers;

import lab2.Result;
import lab2.producers.Producer;

import java.util.function.Function;

public class WorkItem {
    private final Producer item;
    private final Function<Producer, Integer> func;
    private final Result result;


    public WorkItem(Producer item, Function<Producer, Integer> func) {
        this.item = item;
        this.func = func;
        result = new Result();
    }

    public void apply() {
        result.setResult(func.apply(item));
    }

    public Result getResult() {
        return result;
    }

}
