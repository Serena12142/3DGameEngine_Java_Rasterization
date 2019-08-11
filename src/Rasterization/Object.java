package Rasterization;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.awt.Color;
import java.util.Random;

public class Object {
    public Vector[] vertices;
    public int[][] trigVertices;
    
    public Triangle[] trigs;
    public Matrix4 orientation;
    public Vector position;
    public double rotateSpeed=0.05;
    Object(){
        this.position=new Vector();
        orientation=Matrix4.identity();
    }
    Object(Triangle[] trigs){
        this.trigs=trigs;
    }
    Object(double[][] trigs){
        this.trigs = new Triangle[trigs.length];
        for(int i=0;i<trigs.length;i++){
            this.trigs[i] = new Triangle(trigs[i]);
        }
    }
    Object(String filename, Color color) throws FileNotFoundException{
        File file = new File(filename);
        Scanner s = new Scanner(file); 
        int vs=0, fs=0;
        String c;
        while(s.hasNextLine()){
            c=s.nextLine().split(" ")[0];
            if(c.equals("v"))
                vs++;
            else if(c.equals("f"))
                fs++;            
        }
        vertices=new Vector[vs];
        trigVertices=new int[fs][3];
        trigs=new Triangle[fs];
        System.out.println(filename+": "+fs);
        int vi=0,fi=0;
        String[] line;
        s = new Scanner(file); 
        while(s.hasNextLine()){
            line=s.nextLine().split(" ");
            if(line[0].equals("v")){
                vertices[vi++]=new Vector(Double.parseDouble(line[1]),Double.parseDouble(line[2]),
                        Double.parseDouble(line[3]));
            }else if(line[0].equals("f")){
                String[] v1=line[1].split("/");
                String[] v2=line[2].split("/");
                String[] v3=line[3].split("/");
                trigVertices[fi][0]=Integer.parseInt(v1[0])-1;
                trigVertices[fi][1]=Integer.parseInt(v2[0])-1;
                trigVertices[fi][2]=Integer.parseInt(v3[0])-1;
                trigs[fi++]=new Triangle(vertices[Integer.parseInt(v1[0])-1],
                                         vertices[Integer.parseInt(v2[0])-1],
                                         vertices[Integer.parseInt(v3[0])-1]);
                               
            }
        }
        position=new Vector();
        orientation=Matrix4.identity();
    }
    Object(Object obj){
        int vs=obj.vertices.length;
        int fs=obj.trigVertices.length;
        vertices = new Vector[vs];
        trigVertices = new int[fs][3];
        System.arraycopy(obj.vertices, 0, this.vertices, 0, vs);
        for (int i=0;i<fs;i++){
            System.arraycopy(obj.trigVertices[i], 0, this.trigVertices[i], 0, 3);
        }
        orientation = new Matrix4(obj.orientation);
        position = new Vector(obj.position);
        
        this.trigs = new Triangle[obj.trigs.length];
        System.arraycopy(obj.trigs, 0, this.trigs, 0, obj.trigs.length);
    }
    
    void rotate(int x,int y,int z){
        orientation=(Matrix4.getRotationMatX(x*rotateSpeed)).
                multiply(Matrix4.getRotationMatY(y*rotateSpeed)).
                multiply(Matrix4.getRotationMatZ(z*rotateSpeed)).multiply(orientation);
    }
    
    void move(int x,int y,int z){
        position.x+=x;
        position.y+=y;
        position.z+=z;
    }
    void move(Vector v){
        position.x+=v.x;
        position.y+=v.y;
        position.z+=v.z;
    }
    
    void offset(double maxAmount){
        Random rand = new Random();
        for (int i=0;i<trigs.length;i++){
            double amount = (rand.nextInt((int)(maxAmount*100*2))-20)/(double)100;
            double alpha = 1-(amount/trigs[i].findMidpoint().add(position).length());
            for (int j=0;j<3;j++){
                trigs[i].points[j]=new Vector(0,0,0).interpolate(trigs[i].points[j].add(position),alpha).subtract(position);
            }
        } 
        rotate(rand.nextInt(50)-25,rand.nextInt(50)-25,rand.nextInt(50)-25);
    }
    
    static Object getCube(){
        double[][] cubeTrigs={
            {0,0,0, 0,1,0, 1,1,0},
            {0,0,0, 1,1,0, 1,0,0},
            
            {1,0,0, 1,1,0, 1,1,1},
            {1,0,0, 1,1,1, 1,0,1},
            
            {1,0,1, 1,1,1, 0,1,1},
            {1,0,1, 0,1,1, 0,0,1},
            
            {0,0,1, 0,1,1, 0,1,0},
            {0,0,1, 0,1,0, 0,0,0},
            
            {0,1,0, 0,1,1, 1,1,1},
            {0,1,0, 1,1,1, 1,1,0},
            
            {1,0,1, 0,0,1, 0,0,0},
            {1,0,1, 0,0,0, 1,0,0},
        };
        return new Object(cubeTrigs); 
    }
}
