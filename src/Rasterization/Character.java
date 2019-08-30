package Rasterization;

public class Character extends Object{
    public Vector heading,headingLR,headingUD;
    public double moveSpeed=0.05,rotatespeed=0.01;
    public Matrix4 headOrientation;
    Character(){
        super();
        heading=new Vector(0,0,1);
        headingLR=new Vector(1,0,0);
        headingUD=new Vector(0,1,0);
        headOrientation=Matrix4.identity();
    }
    
    void rotate(int x,int y,int z){
        orientation=(Matrix4.getRotationMatY(y*rotateSpeed)).multiply(orientation);
        headOrientation=(Matrix4.getRotationMatX(x*rotateSpeed)).
                multiply(Matrix4.getRotationMatY(y*rotateSpeed)).
                multiply(Matrix4.getRotationMatZ(z*rotateSpeed)).multiply(headOrientation);
    }
    void applyRotation(){
        heading=new Vector(0,0,1).MatMultiply(orientation);
        headingLR=new Vector(1,0,0).MatMultiply(orientation);
        headingUD=new Vector(0,1,0).MatMultiply(orientation);
        heading.normalize();
        headingLR.normalize();
        headingUD.normalize();
    }
    
    void position(double x, double y, double z){
        position.x=x;
        position.y=y;
        position.z=z;
    }
    void move(int fb,int lr,int ud){
        frontBack(fb);
        leftRight(lr);
        upDown(ud);
    }
    
    void frontBack(int front){    
        position.x+=heading.x*moveSpeed*front;
        position.y+=heading.y*moveSpeed*front;
        position.z+=heading.z*moveSpeed*front;
    }
    void leftRight(int left){
        position.x+=headingLR.x*moveSpeed*left;
        position.y+=headingLR.y*moveSpeed*left;
        position.z+=headingLR.z*moveSpeed*left;
    }
    void upDown(int up){
        position.x+=headingUD.x*moveSpeed*up;
        position.y+=headingUD.y*moveSpeed*up;
        position.z+=headingUD.z*moveSpeed*up;
    }
}
