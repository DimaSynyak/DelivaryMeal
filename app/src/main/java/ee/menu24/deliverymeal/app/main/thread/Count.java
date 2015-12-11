package ee.menu24.deliverymeal.app.main.thread;

/**
 * Created by 1 on 10.11.2015.
 */
public class Count {
    public static final int TIME = 100;
    public static final int FAST_TIME = 10;

    private int level;
    private int data;           //percent
    private boolean stateData;
    private boolean stateLoadFragment;

    public Count(int level) {
        data = 0;
        this.level = 100/level;
    }

    public synchronized void complete(){
        data += level ;
    }

    public int getLevel() {
        return level;
    }

    public synchronized int getData() {
        return data;
    }

    public synchronized boolean isStateData() {
        return stateData;
    }

    public synchronized void setStateData(boolean stateData) {
        this.stateData = stateData;
    }

    public synchronized boolean isStateLoadFragment() {
        return stateLoadFragment;
    }

    public synchronized void setStateLoadFragment(boolean stateLoadFragment) {
        this.stateLoadFragment = stateLoadFragment;
    }
}
