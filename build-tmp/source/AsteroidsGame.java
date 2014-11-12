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

int starlen = (int)(Math.random()*1000);
int astlen = (int)(Math.random()*50)+3;


Asteroid[] heart = new Asteroid[astlen];
SpaceShip vroom = new SpaceShip();
Star[] shine = new Star[starlen];

ArrayList <Asteroid> Belt = new ArrayList <Asteroid> ();


boolean aPressed = false;
boolean wPressed = false;
boolean dPressed = false;

public void setup() 
{
  size(600, 600);
  rectMode(CENTER);
  noStroke();

  for (int i = 0; i < starlen; ++i) 
  {
    shine[i] = new Star();  
  }
  for (int i = 0; i < astlen; ++i) 
  {
    heart[i] = new Asteroid();
    Belt.add(heart[i]);  
  }
}
public void draw() 
{
  background(0);
  if(wPressed){vroom.accelerate(0.1f);}
  if(aPressed){vroom.rotate(-5);}
  if(dPressed){vroom.rotate(5);}
  vroom.move();
  vroom.show();
  for (int i = 0; i < starlen; ++i) 
  {
    shine[i].show(); 
  }
  for (int i = 0; i < Belt.size(); ++i) 
  {
    if(dist(vroom.getX(), vroom.getY(), Belt.get(i).getX(), Belt.get(i).getY()) < 20)
    {
      Belt.remove(i);
      continue;
    }
    Belt.get(i).move();
    Belt.get(i).show();
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
}

class SpaceShip extends Floater  
{   
  public SpaceShip()
  {
    corners = 4;

    xCorners = new int[corners];
    yCorners = new int[corners];
    xCorners[0] = -8;
    yCorners[0] = -8;
    xCorners[1] = 16;
    yCorners[1] = 0;
    xCorners[2] = -8;
    yCorners[2] = 8;
    xCorners[3] = -2;
    yCorners[3] = 0;
    myColor = 255;
    myCenterX = 300;
    myCenterY = 300;
    myDirectionX = 0;
    myDirectionY = 0;
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

public void keyReleased()
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
public void keyPressed()
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
    vroom.setX((int)(Math.random()*600));
    vroom.setY((int)(Math.random()*600));
    vroom.setPointDirection((int)(Math.random()*360));
    vroom.setDirectionX(0);
    vroom.setDirectionY(0);
  }
  if(key == '4'){vroom.setPointDirection(180);}
  if(key == '6'){vroom.setPointDirection(0);}
  if(key == '8'){vroom.setPointDirection(270);}
  if(key == '2'){vroom.setPointDirection(90);}
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
    stroke(myColor);    
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

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "AsteroidsGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
