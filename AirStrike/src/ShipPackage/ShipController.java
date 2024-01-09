package ShipPackage;

import Panels.Sea;

public class ShipController implements Runnable{

    private Ship ship;
    private Sea sea;
    public ShipController(Ship ship, Sea sea) {
        this.ship = ship;
        this.sea = sea;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    @Override
    public void run() {
        //System.out.println(sea.getTimerModel().getStart());
        while (sea.getTimerModel().getStart() > 0){
            ship.setY(ship.getY() - 1);
            try{
                Thread.sleep(ship.getSpeed());
            }catch (InterruptedException e){
                e.getMessage();
            }
            sea.repaint();
        }
    }
}
