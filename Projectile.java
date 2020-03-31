public class Projectile{
  //properties
  private double x, y, radius;
  private boolean alive;

  //methods
  //constructor
  public Projectile(double x, double y, double radius){
    this.alive = true;
    this.x = x;
    this.y = y;
    this.radius = radius;
  }
  //setters
  public void moveVertical(double dy){
    this.y += dy;
  }
  public void moveHorisontal(double dx){
    this.x += dx;
  }
  //getters
  public String display(){
    return ("x: " +this.x+"\ny: "+this.y);
  }
  public double getY(){
    return this.y;
  }
  public double getX(){
    return this.x;
  }
  public double getRadius(){
    return this.radius;
  }
  public boolean getAlive(){
    return this.alive;
  }
}
