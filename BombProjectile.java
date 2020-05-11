public class BombProjectile extends ShipProjectile{
  //Extended Properties
  private double eRadius;   //Explosion radius
  //Constructor
  public BombProjectile(double x, double y, double radius, double theta, double eRadius){
    super(x, y, radius, theta);
    this.eRadius = eRadius;
  }
  //API & Modifiers
  public double getERadius(){
    return this.eRadius;
  }
}
