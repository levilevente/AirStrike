package Timer;

public class TimerModel {
    // read by several threads (game loop, ships, painting), so the values must be volatile
    private volatile int start;
    private volatile int stop;

    public TimerModel(int start, int stop) {
        this.start = start;
        this.stop = stop;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public int getStart() {
        return start;
    }

    public int getStop() {
        return stop;
    }
}
