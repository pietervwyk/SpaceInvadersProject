abstract class GObject{
  //Properties
  protected double x, y, radius;
  //Constructor
  public GObject(double x, double y, double radius){
    this.x = x;
    this.y = y;
    this.radius = radius;
  }
  //API & Modifiers
  public double getX(){
    return x;
  }
  public double getY(){
    return y;
  }
  public double getRadius(){
    return radius;
  }
  public void shiftX(double dx){
    this.x += dx;
  }
}
