package ShipPackage;

import java.util.Random;

public class RandomShipGenerator {
    private int zone;

    private final Random random = new Random();
    public RandomShipGenerator(int zone) {
        this.zone = zone;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public Ship getRandomShip(){
        return switch (random.nextInt(6) + 1) {
            case 1 ->  //battleship
                    new BattleShip(random.nextInt(86) + ((zone - 1) * 106), 640, 20, 120, zone);
            case 2 ->  //carrier
                    new CarrierShip(random.nextInt(49) + ((zone - 1) * 106), 640, 57, 190, zone);
            case 3 ->  //submarine
                    new ShipSubMarineHullShip(random.nextInt(71)+ ((zone - 1) * 106), 640, 35, 142, zone);
            case 4 ->  //cruiser
                    new ShipCruiserHullShip(random.nextInt(83) + ((zone - 1) * 106), 640, 23, 128, zone);
            case 5 -> //patrol
                    new ShipPatrolHullShip(random.nextInt(93) + ((zone - 1) * 106), 640, 13, 55, zone);
            case 6 -> //rescue
                    new ShipRescueShip(random.nextInt(89) + ((zone - 1) * 106), 640, 17, 100, zone);
            default -> null;
        };
    }
}
