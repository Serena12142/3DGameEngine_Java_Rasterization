package Rasterization;

import java.awt.Color;
import java.util.Random;

public class Triangle {
    public Vector[] points = new Vector[3];
    public Color c;
    public double shade; 
    Triangle(Vector a, Vector b, Vector c, Color color){
        points[0]=new Vector(a);
        points[1]=new Vector(b);
        points[2]=new Vector(c);
        this.c=color;
    }
    Triangle(Vector a, Vector b, Vector c){
        this(a,b,c,null);
    }
    Triangle(Triangle trig){
        this(trig.points[0],trig.points[1],trig.points[2],null);
    }
    Triangle(Vector[] ps, Color color){
        this(ps[0],ps[1],ps[2],color);
    }
    Triangle(){
        this(new Vector(),new Vector(),new Vector(),null);
    }
    Triangle(double[] ps){
        this(new Vector(ps[0],ps[1],ps[2]),
             new Vector(ps[3],ps[4],ps[5]),
             new Vector(ps[6],ps[7],ps[8]),
             null);
    }
    
    boolean seen(){
        if (points[0].x>points[0].w && points[1].x>points[1].w && points[2].x>points[2].w)
            return false;
        if (points[0].x<-points[0].w && points[1].x<-points[1].w && points[2].x<-points[2].w)
            return false;
        if (points[0].y>points[0].w && points[1].y>points[1].w && points[2].y>points[2].w)
            return false;
        if (points[0].y<-points[0].w && points[1].y<-points[1].w && points[2].y<-points[2].w)
            return false;
        if (points[0].z>points[0].w && points[1].z>points[1].w && points[2].z>points[2].w)
            return false;
        if (points[0].z<0 && points[1].z<0 && points[2].z<0)
            return false;
        return true;
    }
    
    Triangle transform(Matrix4 trans){
        Triangle newTrig=new Triangle();
        for (int j=0;j<3;j++){
            newTrig.points[j]=points[j].MatMultiply(trans);              
        }
        newTrig.c=c;
        return newTrig;
    }   
    void divide(){
        for (int j=0;j<3;j++){
            points[j].divide();              
        }
    }

    void move(double x,double y,double z){
        for(int j=0;j<3;j++){
            points[j].x+=x;  
            points[j].y+=y;  
            points[j].z+=z;            
        }
    }
        
    Triangle rotate(Matrix4 rotationMatX,Matrix4 rotationMatY,Matrix4 rotationMatZ){
        Triangle newTrig=transform(rotationMatX).transform(rotationMatY).transform(rotationMatZ);
        return newTrig;
    }
    Triangle rotate(Matrix4 rotationMat){
        Triangle newTrig=transform(rotationMat);
        return newTrig;
    }  
    
    void offset(double n){
        for (int i=0;i<3;i++){
            points[i]=new Vector(0,0,0).interpolate(points[i],n/100+0.85);
        }          
    }
    
    Vector findMidpoint(){
        Vector midpoint=new Vector();
        midpoint.x=(points[0].x+points[1].x+points[2].x)/3;
        midpoint.y=(points[0].y+points[1].y+points[2].y)/3;
        midpoint.z=(points[0].z+points[1].z+points[2].z)/3;
        return midpoint;
    }
    
    //0-1 input, 0 = same, 1 = black
    void darken() {
        int red = (int) Math.round(Math.max(0, c.getRed() - 255 * shade));
        int green = (int) Math.round(Math.max(0, c.getGreen() - 255 * shade));
        int blue = (int) Math.round(Math.max(0, c.getBlue() - 255 * shade));
        c = new Color(red,green,blue);
    }
}
