package Timer;

public class TimerModel {
    private Integer start, stop;

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

    public Integer getStart() {
        return start;
    }

    public Integer getStop() {
        return stop;
    }
}
