////////////////////////////////////////////////////////////////////////////////
//Class contains all game mechanics
//Includes game loop
//This class is a client of Enemy.java, Ship.java, Projectile.java, Powerup.java,
//ShipProjectile.java and BombProjectile.java
////////////////////////////////////////////////////////////////////////////////

import java.awt.Font;
import java.util.ArrayList;

public class GameState{
  //Global variables
  final static double XRANGE = 10.0;        //scale of the graphical display
  final static double YRANGE = 10.0;
  final static double RADIUS = XRANGE/50;   //enemy radius
  final static int ROWS = 3, COLUMNS = 10;  //rows and columns of enemies
  static double dx = 0.05;           //change in x position every screen refresh
  static double difficuilty= 0.0005;
  static ArrayList<Enemy> eList = new ArrayList<Enemy>(); //all enemies
  static Ship ship = new Ship(XRANGE/2, 2.5*RADIUS, RADIUS);
  static ArrayList<Powerup> puList = new ArrayList<Powerup>();
  static ArrayList<ShipProjectile> pList = new ArrayList<ShipProjectile>();   //ship projectiles
  static ArrayList<Projectile> epList = new ArrayList<Projectile>();  //enemy projectiles
  static ArrayList<BombProjectile> bList = new ArrayList<BombProjectile>(); //bombs
  static int lives, score, bombs, wave;
  static boolean gameOver, auto, immune;
  static long endTime, startTime = System.currentTimeMillis(), autoStartTime = System.currentTimeMillis();
  static long imStartTime = System.currentTimeMillis(), bombStartTime = System.currentTimeMillis();

  //Initial setup of game
  public static void init(){
    Font font = new Font("Arial", Font.BOLD, 20);
    StdDraw.setCanvasSize(720, 720);
    StdDraw.setFont(font);
    StdDraw.setXscale(0, XRANGE);
    StdDraw.setYscale(0, YRANGE);
    StdDraw.setPenRadius(0.005);
    StdDraw.enableDoubleBuffering();
  }

  public static void reset(){
    lives = 3;
    score = 0;
    auto = false;
    immune = false;
    bombs = 0;
    wave = 1;
    gameOver = false;
    difficuilty = 0.0005;
    epList.clear();
    eList.clear();
    pList.clear();
    puList.clear();
    bList.clear();
    createEnemies();
  }

  //Game starts, animation loop, score and lives update
  public static void runGame(){
    //player playing...
    while(!gameOver){
      StdDraw.setPenColor(StdDraw.WHITE);
      enemyFunctions();
      shipFunctions();
      projFuntions();
      powerupFunctions();
      if (epList.size() > 0) eProjFunctions();
      if (Math.random() < eList.size()*difficuilty){  //pseudo random way of generating enemy projectile
        int c = (int)(Math.random() * eList.size());  //find random enemy
        epList.add(new Projectile(eList.get(c).getX(), eList.get(c).getY(), RADIUS/5));
      }
      if (lives <= 0) gameOver = true;
      StdDraw.setPenColor(StdDraw.GREEN);
      if (StdDraw.isKeyPressed(66)){
        System.out.println(RADIUS);
      }
      if (StdDraw.isKeyPressed(81)){
        System.exit(0);
      }
      draw();
    }
    //player lost...
    StdDraw.setPenColor(StdDraw.GREEN);
    while (true){
      deathSeq();
      StdDraw.text(XRANGE/2, YRANGE/3, "TO PLAY AGAIN PRESS 'SPACE'");
      if (StdDraw.isKeyPressed(81)){
        System.exit(0);
      }
      if (StdDraw.isKeyPressed(32)){
        reset();
        runGame();
        break;
      }
      draw();
    }
  }
  public static void draw(){
    StdDraw.setPenRadius(0.005);
    StdDraw.line(0, 2*RADIUS, XRANGE, 2*RADIUS);
    StdDraw.textLeft(0.1, YRANGE-RADIUS, "SCORE: "+ score);
    StdDraw.textLeft(XRANGE/1.3, YRANGE-RADIUS, "LIVES: " + lives);
    StdDraw.text(XRANGE/2, YRANGE-RADIUS, "WAVE: " + wave);
    StdDraw.show();
    StdDraw.pause(30);
    StdDraw.clear();
    StdDraw.picture(XRANGE/2, YRANGE/2, "resources/background.jpg", XRANGE, YRANGE);
  }

  //Create 2D array of enemies and initialise their values
  public static void createEnemies(){
    int c=0;
    double posX, posY = YRANGE-4*RADIUS;
    for (int i = 0; i < ROWS; i++){
      posX = 3*RADIUS;
      for (int j = 0; j < COLUMNS; j++){
        boolean special = false;
        if (Math.random() < 0.04){
          special = true;
        }
        eList.add(new Enemy(posX, posY, RADIUS, special));
        posX += 3*RADIUS;
        c++;
      }
      posY -= 2*RADIUS;
    }
  }
  public static void winSeq(){        //Win sequence
    StdDraw.text(XRANGE/2, YRANGE/2, "YOU WIN!");
    StdDraw.text(XRANGE/2, YRANGE/2-3*RADIUS, "SCORE: "+ score);
    gameOver = true;
  }

  public static void deathSeq(){      //Death sequence
    StdDraw.text(XRANGE/2, YRANGE/2, "GAME OVER");
    StdDraw.text(XRANGE/2, YRANGE/2-3*RADIUS, "SCORE: "+ score);
  }

  public static void moveEnemiesDown(){
    for (int i = 0; i < eList.size(); i++){
        eList.get(i).moveDown();
    }
  }
  //auxillary function
  public static double clamp(double min, double max, double target){
    if (min >= target) return min;
    else if (max <= target) return max;
    else return target;
  }

  //collision detection between a rectangle and a point
  public static boolean checkCollisionRP(double rx, double ry, double rhw, double rhh, double px, double py){
    if ((px >= rx-rhw) && (px <= rx+rhw)){
      if ((py >= ry-rhh) && (py <= ry+rhh)){
        return true;
      }
      else return false;
    }
    else return false;
  }

  //collision detection between a rectangle and a circle        //rhw, rhh: rect half width/height
  public static boolean checkCollisionRC(double rx, double ry, double rhw, double rhh, double cx, double cy, double cr){
    double testX = clamp(rx-rhw, rx+rhw, cx);
    double testY = clamp(ry-rhh, ry+rhh, cy);
    double circleToRect = Math.sqrt((testX-cx)*(testX-cx)+(testY-cy)*(testY-cy));
    if (circleToRect <= cr){
      return true;
    }
    else{
      return false;
    }
  }
  //collision detectin between two circles
  public static boolean checkCollision(double x1, double y1, double r1, double x2, double y2, double r2){
    if (Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))<(r1+r2)) {return true;}
    else {return false;}
  }

  public static void eProjFunctions(){     //Enemy projectiles
    for (int i = 0; i < epList.size(); i++){
      epList.get(i).shiftY(-Math.abs(dx)*3);
      StdDraw.circle(epList.get(i).getX(), epList.get(i).getY(), epList.get(i).getRadius());
      if (epList.get(i).getY() <= 2*RADIUS){
        epList.remove(i);
        i--;
      }
    }
  }

  public static void powerupFunctions(){
    if (System.currentTimeMillis() - autoStartTime > 1000){
      auto = false;
    }
    if (System.currentTimeMillis() - imStartTime > 6000){
      immune = false;
    }
    for (int i = 0; i < puList.size(); i++){
      if (puList.get(i).getY() > 3*RADIUS){
        puList.get(i).shiftY(-Math.abs(dx));
      }
      switch(puList.get(i).getType()) {
        case 0:
          StdDraw.setPenColor(StdDraw.ORANGE);
          break;
        case 1:
          StdDraw.setPenColor(StdDraw.CYAN);
          break;
        case 2:
          StdDraw.setPenColor(StdDraw.RED);
          break;
        default:
          System.out.println("PU type unavailable");
      }
      StdDraw.circle(puList.get(i).getX(), puList.get(i).getY(), puList.get(i).getRadius());
      StdDraw.setPenColor(StdDraw.WHITE);
      if (checkCollisionRC(ship.getX(), ship.getY(), ship.getHW(), ship.getHH(), puList.get(i).getX(), puList.get(i).getY(), puList.get(i).getRadius())){
        switch(puList.get(i).getType()) {
          case 0:
            auto = true;
            autoStartTime = System.currentTimeMillis();
            break;
          case 1:
            immune = true;
            imStartTime = System.currentTimeMillis();
            break;
          case 2:
            bombs++;
            break;
          default:
            System.out.println("PU type unavailable");
        }
        puList.remove(i);
        i--;
      }
    }
  }

  public static void projFuntions(){
    //Ship projectiles
    for (int i = 0; i < pList.size(); i++){
      pList.get(i).shiftY(Math.abs(dx)*4 *Math.sin(pList.get(i).getTheta()));
      pList.get(i).shiftX(Math.abs(dx)*4 *Math.cos(pList.get(i).getTheta()));
      StdDraw.circle(pList.get(i).getX(), pList.get(i).getY(), pList.get(i).getRadius());
      if (pList.get(i).getY()>=YRANGE || pList.get(i).getX()<0 || pList.get(i).getX()>XRANGE){
        pList.remove(i);
        i--;
      }
    }
    //Bombs
    for (int i = 0; i < bList.size(); i++){
      bList.get(i).shiftY(Math.abs(dx)*4 *Math.sin(bList.get(i).getTheta()));
      bList.get(i).shiftX(Math.abs(dx)*4 *Math.cos(bList.get(i).getTheta()));
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.filledCircle(bList.get(i).getX(), bList.get(i).getY(), bList.get(i).getRadius());
      StdDraw.setPenColor(StdDraw.WHITE);
      if (bList.get(i).getY()>=YRANGE || bList.get(i).getX()<0 || bList.get(i).getX()>XRANGE){
        bList.remove(i);
        i--;
      }
    }

  }

  public static void shipFunctions(){
    //shoot
    if(StdDraw.isKeyPressed(87)){
      endTime = System.currentTimeMillis();
      if (endTime - startTime >= 1000)    //delay to shoot
        ship.setFireReady(true);
      if (auto) pList.add(new ShipProjectile(ship.getX(), ship.getY()+ship.getHH(), RADIUS/4, ship.getTheta()));
      else if (ship.getFireReady()){
        if (bombs > 0 && (System.currentTimeMillis() - startTime > 1000)){
          bList.add(new BombProjectile(ship.getX(), ship.getY()+ship.getHH(), RADIUS/4, ship.getTheta(), RADIUS*4));
          bombs--;
          startTime = System.currentTimeMillis();
        }
        else if (System.currentTimeMillis() - startTime > 1000){

          pList.add(new ShipProjectile(ship.getX(), ship.getY()+ship.getHH(), RADIUS/4, ship.getTheta()));
          ship.setFireReady(false);
          startTime = System.currentTimeMillis();
        }
      }
    }
    //End shoot
    if(StdDraw.isKeyPressed(65)){
      if (ship.getX() > ship.getHW()){
        ship.shiftX(-0.2);
      }
    }
    if (StdDraw.isKeyPressed(68)){
      if (ship.getX() < (XRANGE-ship.getHW())){
        ship.shiftX(0.2);
      }
    }
    //Rotator
    if (StdDraw.isKeyPressed(37)){  //comma, rotates anti-clockwise
      if (ship.getTheta() < Math.PI)
        ship.rotate(Math.PI/20);
    }
    if (StdDraw.isKeyPressed(39)){  //full stop, rotates clockwise
      if (ship.getTheta() > 0)
        ship.rotate(-Math.PI/20);
    }
    if (StdDraw.isKeyPressed(38)){  //full stop, rotates clockwise
      ship.setTheta(Math.PI/2);
    }
    StdDraw.line(ship.getX(), ship.getY() + ship.getHH(), 2*ship.getHH() * Math.cos(ship.getTheta()) + ship.getX(), 2*ship.getHH() * Math.sin(ship.getTheta()) + ship.getY()+ship.getHH());
    //End Rotator
    //Powerup Indicator
    StdDraw.setPenRadius(0.01);
    if (immune){
      StdDraw.setPenColor(StdDraw.CYAN);
      StdDraw.line(ship.getX()-0.15, ship.getY()-0.225, ship.getX()+0.15, ship.getY()-0.225);
    }
    if (auto){
      StdDraw.setPenColor(StdDraw.ORANGE);
      StdDraw.line(ship.getX()-0.15, ship.getY()-0.15, ship.getX()+0.15, ship.getY()-0.15);
    }
    if (bombs > 0){
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.line(ship.getX()-0.15, ship.getY()-0.3, ship.getX()+0.15, ship.getY()-0.3);
    }
    StdDraw.setPenColor(StdDraw.WHITE);
    StdDraw.setPenRadius(0.005);
    //End powerup Indicator
    StdDraw.rectangle(ship.getX(), ship.getY(), ship.getHW(), ship.getHH());
    for (int i = 0; i < epList.size(); i++){
      if (checkCollisionRC(ship.getX(), ship.getY(), ship.getHW(), ship.getHH(), epList.get(i).getX(), epList.get(i).getY(), epList.get(i).getRadius())){
        if (!immune)
          lives -= 1;
        epList.remove(i);
        i--;
      }
    }
  }

  //Controls all functions of the enemies, draws enemies aswell
  public static void enemyFunctions(){
    //side collision detection and downwards motion
    for (int i = 0; i < eList.size(); i++){
      if (eList.get(i).getX() <= eList.get(i).getRadius()){
        dx = Math.abs(dx);
        moveEnemiesDown();
        break;
      }
      if (XRANGE-eList.get(i).getX() <= eList.get(i).getRadius()){
        dx = -Math.abs(dx);
        moveEnemiesDown();
        break;
      }
    }
    //more enemy functions
    for (int i = 0; i < eList.size(); i++){
      eList.get(i).shiftX(dx);                  //move enemies sideways
      StdDraw.circle(eList.get(i).getX(), eList.get(i).getY(), eList.get(i).getRadius()); //draw enemies
      if (lives > 0 && eList.get(i).getSpecial() == true){
        //StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.CYAN);
        StdDraw.circle(eList.get(i).getX(), eList.get(i).getY(), eList.get(i).getRadius()/1.25);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.circle(eList.get(i).getX(), eList.get(i).getY(), eList.get(i).getRadius()/1.7);
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.circle(eList.get(i).getX(), eList.get(i).getY(), eList.get(i).getRadius()/2.2);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.filledCircle(eList.get(i).getX(), eList.get(i).getY(), eList.get(i).getRadius()/5);
        StdDraw.setPenColor(StdDraw.WHITE);
        //StdDraw.setPenRadius(0.005);
      }
      if (eList.get(i).getY() <= 2*RADIUS){     //check if reached bottom
        lives = 0;
      }
      //collision detection
      //ship projectiles
      for (int a = 0; a < pList.size(); a++){
        if (i < 0){i = 0;}
        if (a < 0){a = 0;}
        if (checkCollision(eList.get(i).getX(), eList.get(i).getY(), eList.get(i).getRadius(), pList.get(a).getX(),  pList.get(a).getY(),  pList.get(a).getRadius())){
          if (eList.get(i).getSpecial()){
            puList.add(new Powerup(eList.get(i).getX(), eList.get(i).getY(), RADIUS/2, (int)(Math.random()*3)));
          }
          eList.remove(i);
          i--;
          pList.remove(a);
          a--;
          score += 15;
        }
        if (eList.size() == 0){
          difficuilty *=2;
          epList.clear();
          eList.clear();
          pList.clear();
          bList.clear();
          createEnemies();
          wave++;
          break;
        }
      }
      //bombs
      for (int a = 0; a < bList.size(); a++){
        if (i < 0){i = 0;}
        if (a < 0){a = 0;}
        if (checkCollision(eList.get(i).getX(), eList.get(i).getY(), eList.get(i).getRadius(), bList.get(a).getX(),  bList.get(a).getY(),  bList.get(a).getRadius())){
          if (eList.get(i).getSpecial()){
            puList.add(new Powerup(eList.get(i).getX(), eList.get(i).getY(), RADIUS/2, (int)(Math.random()*3)));
          }
          eList.remove(i);
          i--;
          for (int q = eList.size()-1; q > -1; q--){
            if (checkCollision(eList.get(q).getX(), eList.get(q).getY(), eList.get(q).getRadius(), bList.get(a).getX(),  bList.get(a).getY(),  bList.get(a).getERadius())){
              eList.remove(q);
              i--;
            }
          }
          bList.remove(a);
          a--;
          score += 15;
        }
        if (eList.size() == 0){
          difficuilty *=2;
          epList.clear();
          eList.clear();
          pList.clear();
          bList.clear();
          createEnemies();
          wave++;
          break;
        }
      }
      //End bombs
    }
  }
}
