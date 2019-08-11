package Rasterization;

public class Vector2 {
    public double u, v;
    Vector2(double u, double v){
        this.u=u;
        this.v=v;
    }
    Vector2(Vector2 v){
        this(v.u,v.v);
    }
    Vector2(){
        this(0,0);
    }
    
    double dot(Vector2 b){
        double scalar=u*b.u+v*b.v;
        return scalar;
    }    
    void normalize(){
        double l=Math.sqrt(u*u+v*v);
        u/=l;
        v/=l;
    }  
    Vector2 subtract(Vector2 b){
        Vector2 diff=new Vector2();
        diff.u=u-b.u;
        diff.v=v-b.v;
        return diff;
    }    
    Vector2 interpolate(Vector2 b, double alpha){
        Vector2 AB=new Vector2();
        AB.u=(alpha)*(b.u-u)+u;
        AB.v=(alpha)*(b.v-v)+v;
        return AB;
    }
}
