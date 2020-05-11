public class Powerup extends Projectile{
  //Extended properties
  private int type;
  //Constructor
  public Powerup(double x, double y, double radius, int type){
    super(x, y, radius);
    this.type = type;
  }
  //API & Modifiers
  public int getType(){
    return this.type;
  }
}
