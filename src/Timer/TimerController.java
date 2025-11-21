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
            model.setStart(model.getStart() - 1);
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                System.out.println("Hiba a szal vegrehajtasaban.");
            }
            view.repaint();
        }
    }
}
