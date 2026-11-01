package Missile;

import Panels.Sea;
import ShipPackage.Ship;
import Util.Images;
import Util.Sound;

import java.util.List;

public class MissileController implements Runnable{
    private static final int EXPLOSION_TIME = 2;    // the missile explodes when 2 seconds are left
    private static final int EXPLOSION_SIZE = 20;

    private Missile missile;
    private Sea sea;

    public MissileController(Missile missile, Sea sea) {
        this.missile = missile;
        this.sea = sea;
    }

    @Override
    public void run() {
        while (missile.getActive() > 0){
            if (missile.getActive() == EXPLOSION_TIME){
                explode();
            }
            missile.setActive(missile.getActive() - 1);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void explode() {
        // the bomb hits at its center
        int xExplosion = missile.getX() + missile.getWidth() / 2;
        int yExplosion = missile.getY() + missile.getHeight() / 2;

        List <Ship> ships = sea.getShips();
        for(int i = 0; i < ships.size(); i++){
            Ship ship = ships.get(i);
            boolean insideShip = xExplosion >= ship.getX() && xExplosion <= ship.getX() + ship.getWidth()
                    && yExplosion >= ship.getY() && yExplosion <= ship.getY() + ship.getHeight();
            if (insideShip && sea.modifyShips(ship)){
                missile.setHit(true);
            }
        }

        if (missile.getHit()){
            missile.setImage(Images.load("Images/EffectImages/Explosion.png"));
            Sound.play("Sound/PositiveExplosion.wav", -10.0f, false);
        }else{
            missile.setImage(Images.load("Images/EffectImages/WaterExplosion.png"));
            Sound.play("Sound/NegativeExplosion.wav", 0, false);
        }
        missile.setWidth(EXPLOSION_SIZE);
        missile.setHeight(EXPLOSION_SIZE);
        missile.setX(xExplosion - EXPLOSION_SIZE / 2);
        missile.setY(yExplosion - EXPLOSION_SIZE / 2);
    }
}
