package Rasterization;

import java.io.FileNotFoundException;

import java.awt.Color;

public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        int width=1000, height=750;
        Engine test = new Engine(width,height);    
        //Object obj = Object.getCube(); 
        //Object obj = new Object("donut.obj");
        Object obj = new Object("bulbasaur.obj",Color.ORANGE);
        Object obj2 = new Object("wolf.obj",Color.ORANGE);
        //Object obj = new Object("mountains.obj");
        //test.addObject(obj2);
        obj.move(-3,-1,5);
        //obj.offset();
        obj2.move(3,-1,25);
        test.addObject(obj);
        test.addObject(obj2);
        //test.update();
        while(true){
            test.update();
        }
    }  
}
