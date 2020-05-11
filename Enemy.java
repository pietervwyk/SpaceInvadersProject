public class Enemy extends GObject{
  //Extended properties
  private boolean special;  //whether or not it contains a powerup
  //Constructor
  public Enemy(double x, double y, double radius, boolean special){
    super(x, y, radius);
    this.special = special;
  }
  //API & Modifiers
  public void moveDown(){
    this.y -= 2*this.radius;
  }
  public boolean getSpecial(){
    return this.special;
  }
}
