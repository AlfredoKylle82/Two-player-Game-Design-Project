//package finalProject;
/**
 This is the class responsible for generating the breakable and solid bricks and their respective hitboxes.
 @author Navarro(214188)
 @version May 30, 2022
 **/
/*
I have not discussed the Java language code in my program
with anyone other than my instructor or the teaching assistants
assigned to this course.
I have not used Java language code obtained from another student,
or any other unauthorized source, either modified or unmodified.
If any Java language code or documentation used in my program
was obtained from another source, such as a textbook or website,
that has been clearly noted with a proper citation in the comments
of my program.
*/

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class BrickGenerator {


    private Random randNum;


    int bricksXPos[] = {50,350,450,550,50,350,350,450,550,150,150,450,550,
            250,50,100,150,500,250,350,450,250,50,250,350,550,
            50,150,250,300,350,550,50,150,500,350,450,550,500,
            250,350,100};

    int bricksYPos[] = {0,0,0,0,50,50,100,100,150,150,200,200,200,250,
            300,300,300,300,350,350,350,350,400,400,400,400,450,
            450,450,450,450,400,450,500,500,550,500,500,550,550,
            550,550};

    int solidBricksXPos[] = {150,250,250,500,450,300,600,400,350,200,300,300,300};

    int solidBricksYPos[] = {0,0,50,100,150,200,200,250,300,350,500,550,350};

    //switch for the bricks hit on if =1 off if =0
    //if off, bricks wont show
    int brickON[] = new int[42];

    private ImageIcon breakBrickImage;
    private ImageIcon solidBrickImage;

    private BufferedImage buffered_breakBrickImage;
    private BufferedImage buffered_solidBreakImage;

    public int[] getBricksXPos() {
        return bricksXPos;
    }

    //constructor of the class. the images are buffered in order to reduce memory consumption.
    public BrickGenerator()
    {
        try {
            buffered_breakBrickImage = ImageIO.read(getFileFromResourceAsStream("break_brick.jpg"));
            buffered_solidBreakImage = ImageIO.read(getFileFromResourceAsStream("solid_brick.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0; i< brickON.length;i++)
        {
            brickON[i] = 1;
        }
    }

    //responsible for drawing the breakable bricks. It only draws when the brickON[i] = 1.
    public void draw(Component c, Graphics g)
    {
        for(int i=0; i< brickON.length;i++)
        {
            if(brickON[i]==1)
            {
                breakBrickImage = new ImageIcon(buffered_breakBrickImage);
                breakBrickImage.paintIcon(c, g, bricksXPos[i],bricksYPos[i]);
            }
        }
    }
    //responsible for drawing the solid unbreakable bricks. It will always be drawn as it is unbreakable.
    public void drawSolids(Component c, Graphics g)
    {
        for(int i=0; i< solidBricksXPos.length;i++)
        {
            solidBrickImage = new ImageIcon(buffered_solidBreakImage);
            solidBrickImage.paintIcon(c, g, solidBricksXPos[i],solidBricksYPos[i]);
        }
    }
    //responsible for checking the collision of breakable bricks from bullets
    public boolean checkCollisionForBullets(int x, int y)
    {
        boolean collided = false;
        for(int i=0; i< brickON.length;i++)
        {
            if(brickON[i]==1)
            {
                if(new Rectangle(x, y, 10, 10).intersects(new Rectangle(bricksXPos[i], bricksYPos[i], 50, 50)))
                {
                    brickON[i] = 0;
                    collided = true;
                    break;
                }
            }
        }

        return collided;
    }

    //responsible for checking the collision of breakable bricks from tanks
    public boolean checkCollisionForTanks(int x, int y)
    {
        boolean collided = false;
        for(int i=0; i< brickON.length;i++)
        {
            if(brickON[i]==1)
            {
                if(new Rectangle(x, y, 40, 40).intersects(new Rectangle(bricksXPos[i], bricksYPos[i], 50, 50)))
                {
                    collided = true;
                    break;
                }
            }
        }

        return collided;
    }
    //responsible for checking the collision of unbreakable bricks from bullets
    public boolean checkSolidCollisionForBullets(int x, int y)
    {
        boolean collided = false;

        for(int i=0; i< solidBricksXPos.length;i++)
        {
            if(new Rectangle(x, y, 10, 10).intersects(new Rectangle(solidBricksXPos[i], solidBricksYPos[i], 50, 50)))
            {
                collided = true;
                break;
            }
        }

        return collided;
    }
    //responsible for checking the collision of unbreakable bricks from tanks
    public boolean checkSolidCollisionForTanks(int x, int y)
    {
        boolean collided = false;

        for(int i=0; i< solidBricksXPos.length;i++)
        {
            if(new Rectangle(x, y, 40, 40).intersects(new Rectangle(solidBricksXPos[i], solidBricksYPos[i], 50, 50)))
            {
                collided = true;
                break;
            }
        }

        return collided;
    }

    //responsible for getting the brick images.
    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
}
