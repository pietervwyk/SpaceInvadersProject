public class Projectile extends GObject{
  //Constructor
  public Projectile(double x, double y, double radius){
    super(x, y, radius);
  }
  //API & Modifiers
  public void shiftY(double dy){
    this.y += dy;
  }
  public void setY(double y){
    this.y = y;
  }
  public void setX(double x){
    this.x = x;
  }
}
