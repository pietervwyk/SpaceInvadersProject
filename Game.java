////////////////////////////////////////////////////////////////////////////////
//
//MAIN GAME CLASS
//COMPUTER SCIENCE E214 PROJECT - Space Invaders
//Project by Pieter van Wyk & Tiaan Botha
//
//~~~~~~~CONTROLS~~~~~~~~
//EITHER HOLD OR TAP KEYS
//Move ship: 'A', 'D'
//Shoot: 'W'
//Rotate turret: 'LEFT_ARROW', 'RIGHT_ARROW'
//Center turret: 'UP_ARROW'
//Quit game: 'Q'
//
//~~~~~~~POWERUPS~~~~~~~~
//AUTOMATIC (YELLOW/ORGANGE)(1 sec): no cool-down on shots fired
//IMMUNITY (CYAN)(6 sec): don't take damage from enemy projectiles
//BOMB (RED)(1 shot): adds a bomb to your magazine that explodes enemies within a certain radius
//
////////////////////////////////////////////////////////////////////////////////

public class Game{
  public static void main(String[] args){
    GameState control = new GameState();
    control.init();
    control.reset();
    control.runGame();
  }
}
