import java.awt.Font;
import java.util.ArrayList;

public class Game{
  //Global variables
  final static double XRANGE = 10.0;        //scale of the graphical display
  final static double YRANGE = 10.0;
  final static double RADIUS = XRANGE/50;   //enemy radius
  final static int ROWS = 3, COLUMNS = 10;  //rows and columns of enemies
  static double dx = 0.05;           //change in x position every screen refresh
  static double difficuilty= 0.0005;
  static ArrayList<Enemy> eList = new ArrayList<Enemy>(); //all enemies
  static Ship ship = new Ship(XRANGE/2, 2.5*RADIUS, 1.5*RADIUS, RADIUS/2);
  static ArrayList<Projectile> pList = new ArrayList<Projectile>();   //ship projectiles
  static ArrayList<Projectile> epList = new ArrayList<Projectile>();  //enemy projectiles
  static int lives, score;
  static boolean gameOver, auto;

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
    gameOver = false;
    difficuilty = 0.0005;
    epList.clear();
    eList.clear();
    pList.clear();
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
      draw();
    }
    //player lost...
    StdDraw.setPenColor(StdDraw.GREEN);
    while (true){
      deathSeq();
      StdDraw.text(XRANGE/2, YRANGE/3, "TO PLAY AGAIN PRESS 'SPACE'");
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
    StdDraw.text(XRANGE/2, YRANGE-RADIUS, "AUTO: " + auto);
    StdDraw.text(XRANGE/3, YRANGE-RADIUS, "D: " + difficuilty);
    StdDraw.show();
    StdDraw.pause(30);
    StdDraw.clear(StdDraw.BLACK);
  }

  //Create 2D array of enemies and initialise their values
  public static void createEnemies(){
    double posX, posY = YRANGE-4*RADIUS;
    int c = 0;
    for (int i = 0; i < ROWS; i++){
      posX = 3*RADIUS;
      for (int j = 0; j < COLUMNS; j++){
        eList.add(new Enemy(posX, posY, RADIUS));
        posX += 3*RADIUS;
      }
      posY -= 2*RADIUS;
    }
  }
  public static void winSeq(){
    StdDraw.text(XRANGE/2, YRANGE/2, "YOU WIN!");
    StdDraw.text(XRANGE/2, YRANGE/2-3*RADIUS, "SCORE: "+ score);
    gameOver = true;
  }

  public static void deathSeq(){
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
  public static void eProjFunctions(){
    for (int i = 0; i < epList.size(); i++){
      epList.get(i).moveVertical(-Math.abs(dx)*3);
      StdDraw.circle(epList.get(i).getX(), epList.get(i).getY(), epList.get(i).getRadius());
      if (epList.get(i).getY() <= 2*RADIUS){
        epList.remove(i);
        i--;
      }
    }
  }
  public static void projFuntions(){
    for (int i = 0; i < pList.size(); i++){
      pList.get(i).moveVertical(Math.abs(dx)*4);
      StdDraw.circle(pList.get(i).getX(), pList.get(i).getY(), pList.get(i).getRadius());
      if (pList.get(i).getY()>=YRANGE){
        pList.remove(i);
        i--;
      }
    }
  }

  public static void shipFunctions(){
    if (StdDraw.isKeyPressed(65))auto = true;
    if (StdDraw.isKeyPressed(83)) auto = false;
    if(StdDraw.isKeyPressed(38)){
      if (auto) pList.add(new Projectile(ship.getX(), 2*RADIUS, RADIUS/4));
      else if (pList.size() == 0)
        pList.add(new Projectile(ship.getX(), 2*RADIUS, RADIUS/4));
    }
    if(StdDraw.isKeyPressed(37)){
      if (ship.getX() > RADIUS){
        ship.moveLeft(0.2);
      }
    }
    if (StdDraw.isKeyPressed(39)){
      if (ship.getX() < (XRANGE-RADIUS)){
        ship.moveRight(0.2);
      }
    }
    StdDraw.rectangle(ship.getX(), ship.getY(), ship.getHW(), ship.getHH());
    for (int i = 0; i < epList.size(); i++){
      if (checkCollisionRC(ship.getX(), ship.getY(), ship.getHW(), ship.getHH(), epList.get(i).getX(), epList.get(i).getY(), epList.get(i).getRadius())){
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
      if (eList.get(i).getY() <= 2*RADIUS){     //check if reached bottom
        lives = 0;
      }
      //collision detection
      for (int a = 0; a < pList.size(); a++){
        if (i < 0){i = 0;}
        if (a < 0){a = 0;}
        if (checkCollision(eList.get(i).getX(), eList.get(i).getY(), eList.get(i).getRadius(), pList.get(a).getX(),  pList.get(a).getY(),  pList.get(a).getRadius())){
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
          createEnemies();
          break;
        }
      }
    }
  }

  public static void main(String[] args){
    init();
    reset();
    runGame();
  }
}
