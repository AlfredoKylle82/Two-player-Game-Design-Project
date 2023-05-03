//package finalProject;
/**
 This is the class responsible for generating and animating the bullet's movement.
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
import java.awt.Color;
import java.awt.Graphics;


public class Player2BulletGenerator {
    private double x, y;

    // setter and getter of the variables x and y
    public int getX()
    {
        return (int)x;
    }
    public int getY()
    {
        return (int)y;
    }

    // constructor of the class
    public Player2BulletGenerator(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    // responsible for updating the x and y values of the bullet
    public void move(String direction)
    {
        if(direction.equals("right"))
            x += 10;
        else if(direction.equals("left"))
            x -= 10;
        else if(direction.equals("up"))
            y -= 10;
        else
            y += 10;
    }

    // for drawing the bullet
    public void draw(Graphics g)
    {
        g.setColor(Color.green);
        g.fillOval((int) x, (int) y, 10, 10);
    }


}
