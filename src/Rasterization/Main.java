package Rasterization;

import java.io.FileNotFoundException;

import java.awt.Color;

public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        int width=1000, height=750;
        Engine test = new Engine(width,height);    
        //Object obj = Object.getCube(); 
        //Object obj = new Object("donut.obj");
        Object obj = new Object("monkey.obj",Color.ORANGE);
        Object obj2 = new Object("monkey.obj",Color.ORANGE);
        //Object obj = new Object("mountains.obj");
        //test.addObject(obj2);
        obj.move(-3,-1,5);
        obj.offset(0.5);
        obj2.move(3,-1,5);
        test.addObject(obj);
        test.addObject(obj2);
        //test.update();
        while(true){
            test.update();
        }
    }  
}
