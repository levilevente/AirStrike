package Plane;

import Panels.Sea;
import Plane.Plane;

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
                plane.setX(640);
            }else if (plane.getX() > 640){
                plane.setX(-plane.getWidth());
            }

            //if the plane exit from map on north or south
            if (plane.getY() < -plane.getHeight()){
                plane.setY(640);
            }else if (plane.getY() > 640){
                plane.setY(-plane.getHeight());
            }


            switch (plane.getDirection()){
                case 2:                 //down
                    plane.setY(plane.getY() + 1);
                    break;
                case 0:                 //forward
                    plane.setY(plane.getY() - 1);
                    break;
                case 1:                 //right
                    plane.setX(plane.getX() + 1);
                    break;
                case 3:                 //left
                    plane.setX(plane.getX() - 1);
                    break;
            }
            try{
                Thread.sleep(7);
            }catch (InterruptedException e){
                e.getMessage();
            }
            sea.repaint();
        }
    }
}
