public class Ship{
  private double x, y, halfW, halfH; //half width, half height
  //constructor
  public Ship(double x, double y, double halfW, double halfH){
    this.x = x;
    this.y = y;
    this.halfW = halfW;
    this.halfH = halfH;
  }
  //setters
  public void moveLeft(double dx){
    this.x -= dx;
  }
  public void moveRight(double dx){
    this.x += dx;
  }
  //getters
  public double getX(){
    return this.x;
  }
  public double getY(){
    return this.y;
  }
  public double getHW(){
    return this.halfW;
  }
  public double getHH(){
    return this.halfH;
  }
}
