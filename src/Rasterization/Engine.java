package Rasterization;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;

public class Engine {
    JFrame frame;
    Drawing draw;
    int width, height;

    Character camera;
    Vector light, lightViewed;
    Matrix4 projectionMat; 
    Object[] objects;
    Object object;
    Pixel[][] zBuffer;
    
    boolean UP,DOWN,LEFT,RIGHT;
    boolean[] alphabet=new boolean[26];
    
    Engine(int width, int height){      
        this.width=width;
        this.height=height;
        //projection matrix inputs: aspect ratio, field of view, z near, z far
        projectionMat=Matrix4.getProjectionMat((double)height/width,90,1,100);     
        //z-buffer
        zBuffer=new Pixel[width][height];
        //object, camera and light
        objects=new Object[10]; 
        object=null;
        camera=new Character();
        light=new Vector(0,0,0);
        lightViewed=light.subtract(camera.position);  
        //conditions
        UP=false;
        DOWN=false;
        LEFT=false;
        RIGHT=false; 
        //JFrame
        frame = new JFrame("Testing");
        frame.setSize(width,height);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        draw = new Drawing();
        frame.add(draw); 
        frame.addKeyListener(new KeyboardListener());
        frame.addMouseListener(new MouseButtonListener());
    }
    
    void addObject(Object object){
        for (int i=0;i<objects.length;i++){
            if (objects[i]==null){
                objects[i]=object;
                break;
            }     
        }
        //this.object=object;
    }
    void moveCharacter(Character c){ 
        // camera movement
        int frontBack=0,leftRight=0,upDown=0;
        if(UP){
            frontBack=1;
        }else if(DOWN){
            frontBack=-1;
        }
        if(RIGHT){
            leftRight=1;
        }else if(LEFT){
            leftRight=-1;
        }  
        /*
        if(alphabet['N'-65]){
            upDown=1;
        }else if(alphabet['M'-65]){
            upDown=-1;
        }
        */
        
        // camera rotation
        int horizontal=0,vertical=0,turn=0;
        if(alphabet['D'-65]){
            horizontal=1;
        }else if(alphabet['A'-65]){
            horizontal=-1;
        }
        if(alphabet['S'-65]){
            vertical=1;
        }else if(alphabet['W'-65]){
            vertical=-1;
        }     
        /*
        if(alphabet['E'-65]){
            turn=-1;
        }else if(alphabet['Q'-65]){
            turn=1;
        }*/
        
        c.rotate(vertical,horizontal,turn);    
        c.applyRotation(); 
        c.move(frontBack,leftRight,upDown);
    }
    
    void moveObject(Object obj){ 
        // object rotation
        int horizontal=0,vertical=0,turn=0;
        if(alphabet['T'-65]){
            vertical=1;
        }else if(alphabet['G'-65]){
            vertical=-1;
        }
        if(alphabet['F'-65]){
            horizontal=1;
        }else if(alphabet['H'-65]){
            horizontal=-1;
        }
        if(alphabet['R'-65]){
            turn=1;
        }else if(alphabet['Y'-65]){
            turn=-1;
        }
        obj.rotate(vertical,horizontal,turn);
    }   
    
    public boolean update(){
        try {
            Thread.sleep(17);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        moveCharacter(camera);
        moveObject(objects[0]);        
        
        //position light to match camera view
        //lightView=light.subtract(camera.position);            
        //rotate light to match camera view
        //lightView.MatMultiply(camera.orientation.inverse())
        
        draw.repaint();
        return true;
    }
     
    class Drawing extends JComponent{
        public void paint(Graphics g){
            zBuffer=new Pixel[width][height];

            //iteration
            
            for (int i=0;i<objects.length;i++){ 
                if (objects[i]!=null){
                    trianglePreProcessor(objects[i]);
                }
            }
            /*
            if (object!=null){
                vertexProcessor(object);
            }
            */
            for (int i=0;i<width;i++){
                for (int j=0;j<height;j++){
                    if(zBuffer[i][j]!=null){
                        g.setColor(zBuffer[i][j].c);
                    }else{
                        /*
                        if (j<=height/3){
                            g.setColor(Color.CYAN);
                        }else{
                            g.setColor(Color.GREEN);
                        }
                        */
                        g.setColor(Color.BLACK);
                    }
                    g.drawLine(i,j,i,j);
                }
            }
        } 
    }  
    
    //pipeline
    void vertexProcessor(Object object){
        Object newObj=new Object(object);
        for (int i=0;i<newObj.vertices.length;i++){
            //rotate point from origin
            newObj.vertices[i]=newObj.vertices[i].MatMultiply(newObj.orientation);
            //position point in world space
            newObj.vertices[i]=newObj.vertices[i].add(newObj.position);
            //position point to match camera view
            newObj.vertices[i]=newObj.vertices[i].subtract(camera.position);
            //rotate point to match camera view 
            newObj.vertices[i]=newObj.vertices[i].MatMultiply(camera.headOrientation.inverse());
        }
        triangleAssembler(newObj);
    }
    void triangleAssembler(Object object){
        Triangle triangle;
        for (int i=0;i<object.trigVertices.length;i++){
            Vector v1=new Vector(object.vertices[object.trigVertices[i][0]]);
            Vector v2=new Vector(object.vertices[object.trigVertices[i][1]]);
            Vector v3=new Vector(object.vertices[object.trigVertices[i][2]]);
            triangle=new Triangle(v1,v2,v3,Color.ORANGE);
            TriangleProcessor(triangle);
        }                
    }
    void trianglePreProcessor(Object object){
        for (int i=0;i<object.trigs.length;i++){
            Triangle triangle=new Triangle(object.trigs[i]);
            for (int j=0;j<3;j++){
                triangle.points[j]=triangle.points[j].MatMultiply(object.orientation);
                triangle.points[j]=triangle.points[j].add(object.position);
                triangle.points[j]=triangle.points[j].subtract(camera.position);
                triangle.points[j]=triangle.points[j].MatMultiply(camera.headOrientation.inverse());
                triangle.c=Color.ORANGE;
                TriangleProcessor(triangle);
            }
        }
    }
    void TriangleProcessor(Triangle triangle){    
        //backface culling
        //find normal
        Vector a = triangle.points[1].subtract(triangle.points[0]);
        Vector b = triangle.points[2].subtract(triangle.points[0]);
        Vector normal = a.cross(b);
        normal.normalize();
        //find view to midpoint
        Vector sight = triangle.findMidpoint();
        sight.normalize();
        if (sight.dot(normal)<0) { //if facing camera
            //project triangle into NDC space
            triangle=triangle.transform(projectionMat);                        
            //clip triangles that are completly out of the NDC box
            if(triangle.seen()){
                //shade triangle based on amount of light shined
                //double lightShined=-sight.subtract(lightViewed).dot(normal);
                double lightShined=-sight.dot(normal); //light comes from character
                triangle.shade=(1-lightShined);
                //apply clipping to zNear
                clip(triangle);
            }
        }

    }
    void clip(Triangle triangle){
        //if the triangle is completly in front of the near plane
        if(triangle.points[0].z>=0 && triangle.points[1].z>=0 && triangle.points[2].z>=0){
            trianglePostProcesser(triangle);
        }else{
            //one point out of bound: return two triangles
            if (triangle.points[0].z<0 && triangle.points[1].z>=0 && triangle.points[2].z>=0){
                clip2(triangle,0,1,2);
            }else if (triangle.points[1].z<0 && triangle.points[0].z>=0 && triangle.points[2].z>=0){
                clip2(triangle,1,2,0);
            }else if (triangle.points[2].z<0 && triangle.points[1].z>=0 && triangle.points[0].z>=0){
                clip2(triangle,2,0,1);
            //two points out of bound: return one triangle
            }else if (triangle.points[0].z<0 && triangle.points[1].z<0 && triangle.points[2].z>=0){
                clip1(triangle,2,0,1);
            }else if (triangle.points[0].z<0 && triangle.points[2].z<0 && triangle.points[1].z>=0){
                clip1(triangle,1,2,0);
            }else if (triangle.points[2].z<0 && triangle.points[1].z<0 && triangle.points[0].z>=0){
                clip1(triangle,0,1,2);
            }
        }        
    } 
    //first point is in range
    void clip1 (Triangle triangle, int pi1, int pi2, int pi3){
        double alpha1=-triangle.points[pi1].z/(triangle.points[pi2].z-triangle.points[pi1].z);  
        double alpha2=-triangle.points[pi1].z/(triangle.points[pi3].z-triangle.points[pi1].z);    
        triangle.points[pi2]=triangle.points[pi1].interpolate(triangle.points[pi2],alpha1);
        triangle.points[pi3]=triangle.points[pi1].interpolate(triangle.points[pi3],alpha2);
        trianglePostProcesser(triangle);
    }
    //first point is out of range
    void clip2 (Triangle triangle, int pi1, int pi2, int pi3){
        double alpha1=-triangle.points[pi1].z/(triangle.points[pi2].z-triangle.points[pi1].z);  
        double alpha2=-triangle.points[pi1].z/(triangle.points[pi3].z-triangle.points[pi1].z);    
        Vector p12=triangle.points[pi1].interpolate(triangle.points[pi2],alpha1);
        Vector p13=triangle.points[pi1].interpolate(triangle.points[pi3],alpha2);
        trianglePostProcesser(new Triangle(p12,triangle.points[pi2],triangle.points[pi3],triangle.c));
        trianglePostProcesser(new Triangle(p13,p12,triangle.points[pi3],triangle.c));
    }   
    void trianglePostProcesser(Triangle triangle){
        //perspective division
        triangle.divide();    
        //screen transform
        triangle.points[0].x = (double)width/2  + (triangle.points[0].x*width/2);
        triangle.points[0].y = (double)height/2 - (triangle.points[0].y*height/2);
        triangle.points[1].x = (double)width/2  + (triangle.points[1].x*width/2);
        triangle.points[1].y = (double)height/2 - (triangle.points[1].y*height/2);
        triangle.points[2].x = (double)width/2  + (triangle.points[2].x*width/2);
        triangle.points[2].y = (double)height/2 - (triangle.points[2].y*height/2);
        //sort by y then x
        Arrays.sort(triangle.points, (a, b) -> a.y==b.y?Double.compare(a.x, b.x):Double.compare(a.y, b.y));
        triangle.darken();
        drawFillTriangle(triangle.points,triangle.c);
    }
    //fill with scanlines
    void drawFillTriangle(Vector[] points, Color c){     
        if(points[0].y==points[1].y){ //flat top triangle
            flatTopTriangle(points[0],points[1],points[2],c);
        }else if(points[1].y==points[2].y){ //flat bottom triangle
            flatBottomTriangle(points[0],points[1],points[2],c);
        }else{ //irregular triangle, split
            double alpha=(points[1].y-points[0].y)/(points[2].y-points[0].y);
            Vector middle=points[0].interpolate(points[2], alpha);
            if (middle.x<points[1].x){ //middle on the left
                flatBottomTriangle(points[0],middle,points[1],c);
                flatTopTriangle(middle,points[1],points[2],c);
            }else{ //middle on the right
                flatBottomTriangle(points[0],points[1],middle,c);
                flatTopTriangle(points[1],middle,points[2],c);
            }
        }
    }   
    //inputs are sorted by y then x
    void flatTopTriangle(Vector p0,Vector p1,Vector p2, Color c){
        //total change in y
        double deltaY=p2.y-p0.y;
        //alpha value of how much left Vector changes per y
        Vector a1=p2.subtract(p0).divide(deltaY);
        //alpha value of how much right Vector changes per y
        Vector a2=p2.subtract(p1).divide(deltaY);
        drawFlatTriangle(p0, p1, p2, p1, a1, a2, c);
    }
    //inputs are sorted by y then x
    void flatBottomTriangle(Vector p0,Vector p1,Vector p2, Color c){
        //total change in y
        double deltaY=p2.y-p0.y;
        //alpha value of how much left Vector changes per y
        Vector a1=p1.subtract(p0).divide(deltaY);
        //alpha value of how much right Vector changes per y
        Vector a2=p2.subtract(p0).divide(deltaY);
        drawFlatTriangle(p0, p1, p2, p0, a1, a2, c);
    }
    //inputs are sorted by y then x
    void drawFlatTriangle(Vector p0,Vector p1,Vector p2,Vector rightEnd, Vector a1,Vector a2, Color c){
        //clipping on y
        //yStart and yEnd are always the first point and the last point of the triangle
        //apply rasterization rules on which pixel belongs to the triangle
        int yStart=(int)Math.max(0,Math.ceil(p0.y-0.5));
        int yEnd=(int)Math.min(height,Math.ceil(p2.y-0.5));
        //interpolate the first xStart and xEnd value based on yStart
        Vector aStartL=a1.multiply(yStart+0.5-p0.y);
        Vector aStartR=a2.multiply(yStart+0.5-p0.y);
        Vector startL=aStartL.add(p0);
        Vector startR=aStartR.add(rightEnd);
        for (int y=yStart;y<yEnd;y++,startL=startL.add(a1),startR=startR.add(a2)){ 
            //clipping on x
            int xStartPixel=(int)Math.max(0,Math.ceil(startL.x-0.5));
            int xEndPixel=(int)Math.min(width,Math.ceil(startR.x-0.5));
            //total change in x
            double deltaX=startR.x-startL.x;
            //alpha value of how much the scanline Vector changes per x
            Vector alpha=startR.subtract(startL).divide(deltaX);
            Vector point=alpha.multiply(xStartPixel+0.5-startL.x).add(startL);
            //draw the scanline
            for (int x=xStartPixel;x<xEndPixel;x++,point=point.add(alpha)){
                double z=1/point.z;
                putPixel(x, y, z, c);
            }
        }
    }
    void putPixel(int x, int y, double z, Color c){
        //test for z
        if(zBuffer[x][y]==null || zBuffer[x][y].z<z )
            zBuffer[x][y]=new Pixel(c,z);
    }
    
    
    
    
    
    class KeyboardListener implements KeyListener{       
        public void keyPressed(KeyEvent e) {          
            int keyCode = e.getKeyCode();
            if(keyCode==KeyEvent.VK_UP) 
                UP=true;
            else if(keyCode==KeyEvent.VK_DOWN) 
                DOWN=true;   
            else if(keyCode==KeyEvent.VK_LEFT) 
                LEFT=true;
            else if(keyCode==KeyEvent.VK_RIGHT) 
                RIGHT=true;
            else if(keyCode>=65 && keyCode<=90){
                alphabet[keyCode-65]=true;
            }
        }
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if(keyCode==KeyEvent.VK_UP) 
                UP=false;
            else if(keyCode==KeyEvent.VK_DOWN) 
                DOWN=false;
            else if(keyCode==KeyEvent.VK_LEFT) 
                LEFT=false;
            else if(keyCode==KeyEvent.VK_RIGHT) 
                RIGHT=false;
            else if(keyCode>=65 && keyCode<=90){
                alphabet[keyCode-65]=false;
            }
        }
        public void keyTyped(KeyEvent e){}
    }
    class MouseButtonListener implements MouseListener{       
        public void mouseClicked(MouseEvent e) {  
        }  
        public void mouseEntered(MouseEvent e) {  
        }  
        public void mouseExited(MouseEvent e) {  
        }  
        public void mousePressed(MouseEvent e) {  
        }  
        public void mouseReleased(MouseEvent e) {  
        }  
    }
}
