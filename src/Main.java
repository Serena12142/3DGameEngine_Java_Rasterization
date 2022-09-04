import java.io.FileNotFoundException;
import java.awt.Color;

public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        int width=750, height=750;
        Engine test = new Engine(width,height);    
        //Object obj = Object.getObj(); 
        //Object obj = new Object("donut.obj");
        //Object obj = new Object("texturedCube2.obj");
        //Object obj = new Object("monkey.obj");
        //Object obj = new Object("mountains.obj");
		Object obj = new Object("wolf.obj");
        //test.addObject(obj2);
        //obj.move(-3,-1,5);
        //obj.offset(0.5);
        //obj2.move(3,-1,5);
        //obj.scale(10);
        //Object obj = Object.getCube();
        test.addObject(obj);
        //test.addObject(obj2);
        //test.update();
        while(true){
            test.update();
        }
    }  
}
