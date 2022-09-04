import java.awt.Color;

public class Pixel {
    public Color c;
    public double z;
    Pixel(Color c, double z){
        this.c=c;
        this.z=z;
    }
    Pixel(Color c){
        this.c=c;
        this.z=0;
    }
}
