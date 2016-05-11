package de.mknaub.mapfx.utils;

/**
 *
 * @author maka
 */
public class CycleCountHelper {

    private int counter;

    public CycleCountHelper(int cycleCount) {
        this.counter = cycleCount;
    }
    
    public synchronized void raise(){
        ++counter;
    }
    
    public synchronized int getCurrentCycleCount(){
        return counter;
    }

}
