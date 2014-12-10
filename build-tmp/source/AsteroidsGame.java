import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class AsteroidsGame extends PApplet {

int level = 1;

int starlen = (int)(Math.random()*1000);
int astlen = 9;


Asteroid[] heart = new Asteroid[astlen+15];
Star[] shine = new Star[starlen];

ArrayList <SpaceShip> vroom = new ArrayList <SpaceShip> ();
ArrayList <Asteroid> Belt = new ArrayList <Asteroid> ();
ArrayList <Bullet> shot = new ArrayList <Bullet>();

boolean gameStart = false;
boolean aPressed = false;
boolean wPressed = false;
boolean dPressed = false;
int invinValue = 300;

public void setup()
{
  size(600, 600);
  rectMode(CENTER);
  textAlign(CENTER);
  noStroke();
  for (int i = 0; i < starlen; ++i) 
  {
    shine[i] = new Star();  
  }
  for (int i = 0; i < astlen+3*level; ++i) 
  {
    heart[i] = new Asteroid();
    Belt.add(heart[i]);  
  }
  for(int i = 0; i < 3; i++)
  {
    vroom.add(new SpaceShip(i));
  }
}
public void draw() 
{
  background(0);
  for (int i = 0; i < starlen; ++i) 
  {
    shine[i].show(); 
  }
  if(!gameStart)
  {
    textSize(50);
    fill(255);
    text("Level " + level, 300,300);
    
  }
  else
  {
    textSize(16);
    fill(255);
    text("Asteroids: "+Belt.size(), 540, 580);
    text("Lives: "+vroom.size(), 520, 560);
    if(vroom.size() > 0)
    {

      if(wPressed)
      {
        vroom.get(0).accelerate(0.1f);
        vroom.get(0).movedTrue();
      }
      if(aPressed){vroom.get(0).rotate(-5);}
      if(dPressed){vroom.get(0).rotate(5);}
      for(int i = 0; i < vroom.size(); i++)
      {
        vroom.get(i).show();
      }
      vroom.get(0).move();
      if(vroom.get(0).getInvin() > invinValue)
      {
       vroom.get(0).check();
     }
     else
     {
      vroom.get(0).addInvin();
    }
    for(int i = 0; i < shot.size(); i++)
    {
      shot.get(i).move();
      shot.get(i).show();
      if(shot.get(i).check(i) == 0)
      {
        i--;
      }
    }
    for (int i = 0; i < Belt.size(); ++i) 
    {
      Belt.get(i).move();
      Belt.get(i).show();
      if(Belt.get(i).check(i) == 0)
      {
        i--;
      }
    }
    if(Belt.size() == 0)
    {
      level++;
      if(level >= 4)
      {
        textSize(72);
        background(0);
        fill(0,140,255);
        text("You win!", 300,300);
      }
      else
      {   
        gameStart = false;
        for (int i = 0; i < astlen+3*level; ++i) 
        {
          heart[i] = new Asteroid();
          Belt.add(heart[i]);  
        }
        vroom.get(0).reset();
      }
    }
  }
  else 
  {
    textSize(72);
    background(0);
    fill(255,0,0);
    text("you lose...", 300,300);
  }  
}  
}

public void keyPressed()
{
  if(key == ' ')
  {
    gameStart = true;
  }
  if(vroom.size() > 0)
  {
    if(key == 'w')
    {
      wPressed = true;
    }
    if(key == 'd')
    {
      dPressed = true;
    }
    if(key == 'a')
    {
      aPressed = true;
    }
    if(key == '0')
    {
      vroom.get(0).setX((int)(Math.random()*600));
      vroom.get(0).setY((int)(Math.random()*600));
      vroom.get(0).setPointDirection((int)(Math.random()*360));
      vroom.get(0).setDirectionX(0);
      vroom.get(0).setDirectionY(0);
    }
    if(key == '1')
    {
      if(shot.size() < 10 && (vroom.get(0).getMoved() || vroom.get(0).getInvin() > invinValue))
      {
        shot.add(new Bullet(vroom.get(0)));
      }
    }
    if(key == '4'){vroom.get(0).setPointDirection(180);}
    if(key == '6'){vroom.get(0).setPointDirection(0);}
    if(key == '8'){vroom.get(0).setPointDirection(270);}
    if(key == '2'){vroom.get(0).setPointDirection(90);}
  }
}

public void keyReleased()
{
  if(vroom.size() > 0)
  {
    if(key == 'w')
    {
      wPressed = false;
    }
    if(key == 'd')
    {
      dPressed = false;
    }
    if(key == 'a')
    {
      aPressed = false;
    }
  }
}

abstract class Floater //Do NOT modify the Floater class! Make changes in the SpaceShip class 
{   
  protected int corners;  //the number of corners, a triangular floater has 3   
  protected int[] xCorners;   
  protected int[] yCorners;   
  protected int myColor;   
  protected double myCenterX, myCenterY; //holds center coordinates   
  protected double myDirectionX, myDirectionY; //holds x and y coordinates of the vector for direction of travel   
  protected double myPointDirection; //holds current direction the ship is pointing in degrees    
  abstract public void setX(int x);
  abstract public int getX();
  abstract public void setY(int y);  
  abstract public int getY();
  abstract public void setDirectionX(double x);
  abstract public double getDirectionX();
  abstract public void setDirectionY(double y);
  abstract public double getDirectionY();
  abstract public void setPointDirection(int degrees);
  abstract public double getPointDirection();

  //Accelerates the floater in the direction it is pointing (myPointDirection)   
  public void accelerate (double dAmount)   
  {          
    //convert the current direction the floater is pointing to radians    
    double dRadians =myPointDirection*(Math.PI/180);     
    //change coordinates of direction of travel    
    myDirectionX += ((dAmount) * Math.cos(dRadians));    
    myDirectionY += ((dAmount) * Math.sin(dRadians));       
  }   
  public void rotate (int nDegreesOfRotation)   
  {     
    //rotates the floater by a given number of degrees    
    myPointDirection+=nDegreesOfRotation;   
  }   
  public void move ()   //move the floater in the current direction of travel
  {      
    //change the x and y coordinates by myDirectionX and myDirectionY       
    myCenterX += myDirectionX;    
    myCenterY += myDirectionY;     

    //wrap around screen    
    if(myCenterX >width)
    {     
      myCenterX = 0;    
    }    
    else if (myCenterX<0)
    {     
      myCenterX = width;    
    }    
    if(myCenterY >height)
    {    
      myCenterY = 0;    
    }   
    else if (myCenterY < 0)
    {     
      myCenterY = height;    
    }   
  }   
  public void show ()  //Draws the floater at the current position  
  {             
    fill(myColor);   
    //convert degrees to radians for sin and cos         
    double dRadians = myPointDirection*(Math.PI/180);                 
    int xRotatedTranslated, yRotatedTranslated;    
    beginShape();         
    for(int nI = 0; nI < corners; nI++)    
    {     
      //rotate and translate the coordinates of the floater using current direction 
      xRotatedTranslated = (int)((xCorners[nI]* Math.cos(dRadians)) - (yCorners[nI] * Math.sin(dRadians))+myCenterX);     
      yRotatedTranslated = (int)((xCorners[nI]* Math.sin(dRadians)) + (yCorners[nI] * Math.cos(dRadians))+myCenterY);      
      vertex(xRotatedTranslated,yRotatedTranslated);    
    }   
    endShape(CLOSE);  
  }   
} 

class Star
{
  private float x, y;
  private int col;
  public Star()
  {
    x = (float)Math.random()*600;
    y = (float)Math.random()*600;
    col = 255;
  }
  public void show()
  {
    fill(col);
    rect(x,y,1,1);
  }
}

class Asteroid extends Floater
{
  private int rotSpeed;
  public Asteroid()
  {
    rotSpeed = (int)(Math.random()*30)-15;
    corners = 6;
    xCorners = new int[corners];
    yCorners = new int[corners];
    xCorners[0] = 10;
    yCorners[0] = 0;
    xCorners[1] = 4;
    yCorners[1] = 8;
    xCorners[2] = -4;
    yCorners[2] = 8;
    xCorners[3] = -10;
    yCorners[3] = 0;
    xCorners[4] = -4;
    yCorners[4] = -8;
    xCorners[5] = 4;
    yCorners[5] = -8;
    myColor = 140;
    myCenterX = (int)(Math.random()*600);
    myCenterY = (int)(Math.random()*600);
    myDirectionX = (int)(Math.random()*10)-5;
    myDirectionY = (int)(Math.random()*10)-5;
    myPointDirection = 0;

  }
  public void setX(int x){myCenterX = x;}
  public int getX(){return (int)myCenterX;}
  public void setY(int y){myCenterY = y;}
  public int getY(){return (int)myCenterY;}
  public void setDirectionX(double x){myDirectionX = x;}
  public double getDirectionX(){return myDirectionX;}
  public void setDirectionY(double y){myDirectionY = y;}
  public double getDirectionY(){return myDirectionY;}
  public void setPointDirection(int degrees){myPointDirection = degrees;}
  public double getPointDirection(){return myPointDirection;}
  public void move()
  {
    rotate(rotSpeed);
    super.move();
  }
  public int check(int a)
  {
    for(Bullet shell : shot)
    {
      if(dist((float)shell.getX(), (float)shell.getY(), (float)myCenterX, (float)myCenterY) < 10)
      {
        Belt.remove(a);
        return 0;
      }
    }
    return 1;
  }
}

class SpaceShip extends Floater  
{   
  int count;
  int invin;
  boolean moved;
  public SpaceShip(int a)
  {
    moved = false;
    count = a;
    invin = 0;
    corners = 6;

    xCorners = new int[corners];
    yCorners = new int[corners];
    xCorners[0] = 16;
    yCorners[0] = 0;
    xCorners[1] = 0;
    yCorners[1] = 10;
    xCorners[2] = -16;
    yCorners[2] = 8;
    xCorners[3] = -2;
    yCorners[3] = 0;
    xCorners[4] = -16;
    yCorners[4] = -8;
    xCorners[5] = 0;
    yCorners[5] = -10;
    myColor = 255;
    myCenterX = 30*count +20;
    myCenterY = 580;
    myDirectionX = 0;
    myDirectionY = 0;
    myPointDirection = 270;
  }
  public void reset()
  {
    myCenterX = 30*count+20;
    myCenterY = 580;
    myDirectionX = 0;
    myDirectionY = 0;
    myPointDirection = 270;
    invin = 0;
    moved = false;
  }
  public boolean getMoved(){return moved;}
  public void movedTrue(){moved = true;}
  public void movedFalse(){moved = false;}
  public int getInvin(){return invin;}
  public void setInvin(int a){invin = a;}
  public void addInvin(){invin++;}
  public void setCount(int a){count = a;}
  public int getCount(){return count;}
  public void setX(int x){myCenterX = x;}
  public int getX(){return (int)myCenterX;}
  public void setY(int y){myCenterY = y;}
  public int getY(){return (int)myCenterY;}
  public void setDirectionX(double x){myDirectionX = x;}
  public double getDirectionX(){return myDirectionX;}
  public void setDirectionY(double y){myDirectionY = y;}
  public double getDirectionY(){return myDirectionY;}
  public void setPointDirection(int degrees){myPointDirection = degrees;}
  public double getPointDirection(){return myPointDirection;}
  public void check()
  {
    for(Asteroid rock : Belt)
    {
      if(dist((float)myCenterX, (float)myCenterY, rock.getX(), rock.getY()) < 20)
      {
        vroom.remove(0);
      }
    }
  }
  public void show()
  {
    if(invin < invinValue)
    {
      myColor = 50;
    }
    else
    {
      myColor = 255;
    }
    super.show();
  }
}


class Bullet extends Floater
{
  public Bullet(SpaceShip theShip)
  {
    myPointDirection = theShip.myPointDirection;
    double dRadians =myPointDirection*(Math.PI/180);
    myCenterX = theShip.myCenterX;
    myCenterY = theShip.myCenterY;
    myDirectionX = 5 * Math.cos(dRadians) + theShip.myDirectionX;
    myDirectionY = 5 * Math.sin(dRadians) + theShip.myDirectionY;

  }
  public void setX(int x){myCenterX = x;}
  public int getX(){return (int)myCenterX;}
  public void setY(int y){myCenterY = y;}
  public int getY(){return (int)myCenterY;}
  public void setDirectionX(double x){myDirectionX = x;}
  public double getDirectionX(){return myDirectionX;}
  public void setDirectionY(double y){myDirectionY = y;}
  public double getDirectionY(){return myDirectionY;}
  public void setPointDirection(int degrees){myPointDirection = degrees;}
  public double getPointDirection(){return myPointDirection;}
  public void show()
  {
    noStroke();
    fill(0,100,255);
    ellipse((float)myCenterX, (float)myCenterY, 5.0f, 5.0f);
  }
  public void move()
  {
    myCenterX += myDirectionX;    
    myCenterY += myDirectionY; 
  }
  public int check(int a)
  {
    if(myCenterX < 0 || myCenterX > 600 || myCenterY < 0 || myCenterY > 600)
    {
      shot.remove(a);
      return 0;
    }
    return 1;
  }

}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "AsteroidsGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
