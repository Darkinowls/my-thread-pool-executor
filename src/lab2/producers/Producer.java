package lab2.producers;

import java.util.UUID;

public abstract class Producer {
    protected String input;
    private final String uniqueID;
    protected String source;

    public Producer( String input){
        this.input = input;
        uniqueID = UUID.randomUUID().toString();
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getInput() {
        return input;
    }
}
