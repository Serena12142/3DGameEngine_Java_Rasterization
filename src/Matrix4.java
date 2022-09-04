public class Matrix4 {
    double[][] matrix;
    Matrix4(){
        matrix = new double[4][4];
    }
    Matrix4(Matrix4 m){
        matrix = new double[4][4];
        for (int i=0;i<4;i++){
            System.arraycopy(m.matrix[i], 0, matrix[i], 0, 4);
        }
    }
    
    Matrix4 multiply(Matrix4 mat){
        Matrix4 newMat=new Matrix4();
        for (int i=0;i<4;i++){
        for (int j=0;j<4;j++){
        for (int k=0;k<4;k++){
            newMat.matrix[i][j]+=this.matrix[i][k]*mat.matrix[k][j];
        }}} 
        return newMat;
    }
    Matrix4 inverse(){
        Matrix4 newMat=new Matrix4();
        for (int i=0;i<4;i++){
        for (int j=0;j<4;j++){
            newMat.matrix[i][j]+=this.matrix[j][i];
        }} 
        return newMat;
    }
    
    double determinant(){
        return 0;
    }
    double determinant3(){
        return 0;
    }
    
    double determinant2(double a,double b,double c,double d){
        return 0;
    }
    static Matrix4 getProjectionMat(double a,double fov,double zNear,double zFar){
        Matrix4 projectionMat = new Matrix4();
        double fovRad = fov/180*Math.PI;   
        double tanFov = 1.0/Math.tan(fovRad/2);
        projectionMat.matrix[0][0] = a*tanFov;
        projectionMat.matrix[1][1] = tanFov;
        projectionMat.matrix[2][2] = zFar/(zFar-zNear);
        projectionMat.matrix[3][2] = -zNear*zFar/(zFar-zNear);
        projectionMat.matrix[2][3] = 1;
        return projectionMat;
    }
    static Matrix4 getRotationMatX(double theta){
        Matrix4 rotationMatX = new Matrix4();
        rotationMatX.matrix[0][0]=1;
        rotationMatX.matrix[1][1]=Math.cos(theta);
        rotationMatX.matrix[1][2]=Math.sin(theta);
        rotationMatX.matrix[2][1]=-Math.sin(theta);
        rotationMatX.matrix[2][2]=Math.cos(theta);
        rotationMatX.matrix[3][3]=1;
        return rotationMatX;
    }
    static Matrix4 getRotationMatY(double theta){
        Matrix4 rotationMatY = new Matrix4();
        rotationMatY.matrix[0][0]=Math.cos(theta);
        rotationMatY.matrix[0][2]=-Math.sin(theta);
        rotationMatY.matrix[1][1]=1;
        rotationMatY.matrix[2][0]=Math.sin(theta);
        rotationMatY.matrix[2][2]=Math.cos(theta);
        rotationMatY.matrix[3][3]=1;
        return rotationMatY;
    }
    static Matrix4 getRotationMatZ(double theta){
        Matrix4 rotationMatZ = new Matrix4();
        rotationMatZ.matrix[0][0]=Math.cos(theta);
        rotationMatZ.matrix[0][1]=Math.sin(theta);
        rotationMatZ.matrix[1][0]=-Math.sin(theta);
        rotationMatZ.matrix[1][1]=Math.cos(theta);
        rotationMatZ.matrix[2][2]=1;
        rotationMatZ.matrix[3][3]=1;
        return rotationMatZ;
    }
    static Matrix4 identity(){
        Matrix4 identityMat = new Matrix4();
        identityMat.matrix[0][0]=1;
        identityMat.matrix[1][1]=1;
        identityMat.matrix[2][2]=1;
        identityMat.matrix[3][3]=1;
        return identityMat;
    }
}
