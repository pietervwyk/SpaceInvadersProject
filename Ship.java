public class Ship extends GObject{
  //Extended properties
  private double theta;
  private boolean fireReady;
  //Constructor
  public Ship(double x, double y, double radius){
    super(x, y, radius);
    this.theta = Math.PI/2;
    this.fireReady = true;
  }
  //API & Modifiers
  public void setTheta(double theta){
    this.theta = theta;
  }
  public void rotate(double theta){
    this.theta = this.theta + theta;
  }
  public void setFireReady(boolean fireReady){
    this.fireReady = fireReady;
  }
  public double getHW(){
    return 1.5 * this.radius; //half width of ship for drawing
  }
  public double getHH(){
    return this.radius / 2;   //half height of ship for drawing
  }
  public double getTheta(){
    return this.theta;
  }
  public boolean getFireReady(){
    return this.fireReady;
  }
}
