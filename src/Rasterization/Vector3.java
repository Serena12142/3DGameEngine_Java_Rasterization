package Rasterization;

public class Vector3 {
    public double x, y, z, w;
    Vector3(double x, double y, double z, double w){
        this.x=x;
        this.y=y;
        this.z=z;
        this.w=w;
    }   
    Vector3(double x, double y, double z){
        this(x,y,z,1);
    }  
    Vector3(Vector3 v){
        this(v.x,v.y,v.z,v.w);
    }
    Vector3(){
        this(0,0,0,1);
    }
    
    double dot(Vector3 b){
        return x*b.x+y*b.y+z*b.z;
    }    
    Vector3 cross(Vector3 b){
        Vector3 n=new Vector3();
        n.x=y*b.z-z*b.y;
        n.y=z*b.x-x*b.z;
        n.z=x*b.y-y*b.x ;
        return n;
    }    
    
    Vector3 add(Vector3 b){
        Vector3 sum=new Vector3();
        sum.x=x+b.x;
        sum.y=y+b.y;
        sum.z=z+b.z;
        sum.w=w+b.w;
        return sum;
    }    
    Vector3 subtract(Vector3 b){
        Vector3 diff=new Vector3();
        diff.x=x-b.x;
        diff.y=y-b.y;
        diff.z=z-b.z;
        diff.w=w-b.w;
        return diff;
    }    
    void normalize(){
        double l=Math.sqrt(x*x+y*y+z*z);
        x/=l;
        y/=l;
        z/=l;
        w=1;
    }
    void divide(){
        if (w!=0){
            x/=w;
            y/=w;
            z/=w;
        } 
    }
    
    Vector3 interpolate(Vector3 b, double alpha){
        Vector3 AB=new Vector3();
        AB.x=(alpha)*(b.x-x)+x;
        AB.y=(alpha)*(b.y-y)+y;
        AB.z=(alpha)*(b.z-z)+z;
        AB.w=(alpha)*(b.w-w)+w;
        return AB;
    }   
    Vector3 MatMultiply(Matrix4 multiplier){
        Vector3 output = new Vector3();
        output.x=x*multiplier.matrix[0][0]+y*multiplier.matrix[1][0]+
                 z*multiplier.matrix[2][0]+multiplier.matrix[3][0];
        output.y=x*multiplier.matrix[0][1]+y*multiplier.matrix[1][1]+
                 z*multiplier.matrix[2][1]+multiplier.matrix[3][1];
        output.z=x*multiplier.matrix[0][2]+y*multiplier.matrix[1][2]+
                 z*multiplier.matrix[2][2]+multiplier.matrix[3][2];
        output.w=x*multiplier.matrix[0][3]+y*multiplier.matrix[1][3]+
                 z*multiplier.matrix[2][3]+multiplier.matrix[3][3];
        return output;    
    }
}
