package ShipPackage;

import java.util.Random;

public class RandomShipGenerator {
    public static final int ZONE_COUNT = 6;
    private static final int ZONE_WIDTH = 106;
    private static final int START_Y = 640;     // ships start under the bottom of the map
    private static final Random random = new Random();

    private int zone;

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
        int zoneStart = (zone - 1) * ZONE_WIDTH;
        // the random range keeps the ship (width + range <= ~106) inside its zone
        return switch (random.nextInt(6)) {
            case 0 -> new BattleShip(random.nextInt(86) + zoneStart, START_Y, 20, 120, zone);
            case 1 -> new CarrierShip(random.nextInt(49) + zoneStart, START_Y, 57, 190, zone);
            case 2 -> new ShipSubMarineHullShip(random.nextInt(71) + zoneStart, START_Y, 35, 142, zone);
            case 3 -> new ShipCruiserHullShip(random.nextInt(83) + zoneStart, START_Y, 23, 128, zone);
            case 4 -> new ShipPatrolHullShip(random.nextInt(93) + zoneStart, START_Y, 13, 55, zone);
            default -> new ShipRescueShip(random.nextInt(89) + zoneStart, START_Y, 17, 100, zone);
        };
    }
}
