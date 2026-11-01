package Timer;

public class TimerController implements Runnable{
    private TimerModel model;
    private TimerView view;


    public TimerController(TimerModel model, TimerView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void run(){
        while (model.getStart() > model.getStop()){
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
            model.setStart(model.getStart() - 1);
            view.repaint();
        }
    }
}
