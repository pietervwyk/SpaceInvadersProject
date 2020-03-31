public class Enemy{
  private double x, y, radius;

  //constructor
  public Enemy(double x, double y, double radius){
    this.x = x;
    this.y = y;
    this.radius = radius;
  }
  //modifiers
  public void shiftX(double dx){
    this.x += dx;
  }
  public void moveDown(){
    this.y -= 2*this.radius;
  }

  //getters
  public double getX(){
    return x;
  }
  public double getY(){
    return y;
  }
  public double getRadius(){
    return radius;
  }
  //setters
  public void setX(double x){
    this.x = x;
  }
  public void setY(double y){
    this.y = y;
  }
  public void setRadius(double radius){
    this.x = radius;
  }
}
