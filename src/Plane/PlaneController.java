package Plane;

import Panels.Sea;

public class PlaneController implements Runnable{
    private Plane plane;
    private Sea sea;

    public PlaneController(Plane plane, Sea sea) {
        this.plane = plane;
        this.sea = sea;
    }

    @Override
    public void run() {
        while(plane.getMoving()){
            //if the plane exit from map on the right or left
            if (plane.getX() < -plane.getWidth()){
                plane.setX(Sea.SIZE);
            }else if (plane.getX() > Sea.SIZE){
                plane.setX(-plane.getWidth());
            }

            //if the plane exit from map on north or south
            if (plane.getY() < -plane.getHeight()){
                plane.setY(Sea.SIZE);
            }else if (plane.getY() > Sea.SIZE){
                plane.setY(-plane.getHeight());
            }

            switch (plane.getDirection()){
                case Plane.UP -> plane.setY(plane.getY() - 1);
                case Plane.RIGHT -> plane.setX(plane.getX() + 1);
                case Plane.DOWN -> plane.setY(plane.getY() + 1);
                case Plane.LEFT -> plane.setX(plane.getX() - 1);
            }
            try{
                Thread.sleep(7);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
            sea.repaint();
        }
    }
}
