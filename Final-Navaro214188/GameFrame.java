//package finalProject;
/**
 This is the class responsible for generating frame of the game with its respective bounds. It also calls the server here.
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
import javax.swing.*;
import java.awt.*;
import java.net.*;

public class GameFrame extends JFrame{

    GameFrame(){

        GameCanvas gameCanvas = new GameCanvas();
        this.setBounds(0, 0, 800, 680);
        this.setTitle("Final Project - Navarro - 214188");
        this.setBackground(Color.gray);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(gameCanvas);
        this.setVisible(true);
        this.pack();

        gameCanvas.connectToServer();
    }
}
