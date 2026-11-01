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
        // the thread ends when the ship is gone, the game loop then puts a new ship in its zone
        while (sea.getTimerModel().getStart() > 0 && !ship.isGone()){
            ship.setY(ship.getY() - 1);
            try{
                Thread.sleep(ship.getSpeed());
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
            sea.repaint();
        }
    }
}
