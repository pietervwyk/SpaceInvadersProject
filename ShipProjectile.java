public class ShipProjectile extends Projectile{
  // Extended properties
  private double theta;
  //Constructor
  public ShipProjectile(double x, double y, double radius, double theta){
    super(x, y, radius);
    this.theta = theta;
  }
  //API & Modifiers
  public double getTheta(){
    return this.theta;
  }
}
