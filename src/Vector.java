public class Vector {
    public double x, y, z, w;
    Vector(double x, double y, double z,double w){
        this.x=x;
        this.y=y;
        this.z=z;
        this.w=w;
    }
    Vector(double x, double y, double z){
        this(x,y,z,1);
    }
    Vector(){
        this(0,0,0,1);
    }
    Vector(double[] p){
        this(p[0],p[1],p[2],1);
    }
    Vector(Vector v){
        this(v.x,v.y,v.z,v.w);
    }
    
    double dot(Vector b){
        double scalar=x*b.x+y*b.y+z*b.z;
        return scalar;
    }    
    Vector cross(Vector b){
        Vector n=new Vector();
        n.x=y*b.z-z*b.y;
        n.y=z*b.x-x*b.z;
        n.z=x*b.y-y*b.x ;
        return n;
    }    
    
    double length(){
        return Math.sqrt(x*x+y*y+z*z);
    }
    void normalize(){
        double l=length();
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
            w=1;
        }        
    }

    Vector add(Vector b){
        Vector sum=new Vector();
        sum.x=x+b.x;
        sum.y=y+b.y;
        sum.z=z+b.z;
        sum.w=w+b.w;
        return sum;
    }    
    Vector subtract(Vector b){
        Vector diff=new Vector();
        diff.x=x-b.x;
        diff.y=y-b.y;
        diff.z=z-b.z;
        diff.w=w-b.w;
        return diff;
    }    
    Vector divide(double v){
        Vector newV=new Vector();
        if (v!=0){
            newV.x=x/v;
            newV.y=y/v;
            newV.z=z/v;
        } 
        return newV;
    }
    Vector multiply(double v){
        Vector newV=new Vector();
        newV.x=x*v;
        newV.y=y*v;
        newV.z=z*v;
        return newV;
    }
    
    Vector interpolate(Vector b, double alpha){
        Vector AB=new Vector();
        AB.x=(alpha)*(b.x-x)+x;
        AB.y=(alpha)*(b.y-y)+y;
        AB.z=(alpha)*(b.z-z)+z;
        AB.w=(alpha)*(b.w-w)+w;
        return AB;
    }
    
    Vector MatMultiply(Matrix4 multiplier){
        Vector output = new Vector();
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
