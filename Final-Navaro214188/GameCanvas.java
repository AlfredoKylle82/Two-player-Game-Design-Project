//package finalProject;

/**
 This is the class responsible for generating whole logic of the game. Its also responsible for drawing everything needed for the game. Bullet are generated
 inside the code while image sprites were used for the tank and bricks.
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
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.net.*;
import java.io.*;
import javax.swing.Timer;

public class GameCanvas extends JPanel implements ActionListener
{
    private BrickGenerator br;

    private ImageIcon player1;
    private BufferedImage bufferedP1_up;
    private BufferedImage bufferedP1_down;
    private BufferedImage bufferedP1_left;
    private BufferedImage bufferedP1_right;

    private int player1X = 200;
    private int player1Y = 550;
    private boolean player1right = false;
    private boolean player1left = false;
    private boolean player1down = false;
    private boolean player1up = true;
    private int player1score = 0;
    private int player1lives = 5;
    private boolean player1Shoot = false;
    private String bulletShootDir1 = "";

    private ImageIcon player2;
    private BufferedImage bufferedP2_up;
    private BufferedImage bufferedP2_down;
    private BufferedImage bufferedP2_left;
    private BufferedImage bufferedP2_right;


    private int player2X = 400;
    private int player2Y = 550;
    private boolean player2right = false;
    private boolean player2left = false;
    private boolean player2down = false;
    private boolean player2up = true;
    private int player2score = 0;
    private int player2lives = 5;
    private boolean player2Shoot = false;
    private String bulletShootDir2 = "";

    private Timer timer;
    private int delay = 8;

    private MultiKeyPressListener player1Listener;
    private MultiKeyPressListener player2Listener;

    private Player1BulletGenerator player1Bullet = null;
    private Player2BulletGenerator player2Bullet = null;

    private boolean play = true;

    private Socket socket;
    private int playerID;

    //Constructor class
    public GameCanvas()
    {
        // buffered the images so it wont consume a lot of memory
        try {
            bufferedP1_up = ImageIO.read(getFileFromResourceAsStream("player1_tank_up.png"));
            bufferedP1_down = ImageIO.read(getFileFromResourceAsStream("player1_tank_down.png"));
            bufferedP1_left = ImageIO.read(getFileFromResourceAsStream("player1_tank_left.png"));
            bufferedP1_right = ImageIO.read(getFileFromResourceAsStream("player1_tank_right.png"));

            bufferedP2_up = ImageIO.read(getFileFromResourceAsStream("player2_tank_up.png"));
            bufferedP2_down = ImageIO.read(getFileFromResourceAsStream("player2_tank_down.png"));
            bufferedP2_left = ImageIO.read(getFileFromResourceAsStream("player2_tank_left.png"));
            bufferedP2_right = ImageIO.read(getFileFromResourceAsStream("player2_tank_right.png"));


        } catch (IOException e) {
            e.printStackTrace();
        }


        this.setPreferredSize(new Dimension(650,680));
        br = new BrickGenerator();
        player1Listener = new MultiKeyPressListener();
        player2Listener = new MultiKeyPressListener();
        setFocusable(true);

        addKeyListener(player1Listener);
        addKeyListener(player2Listener);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();
    }
    // paints and repaints the images for the animation
    public void paintComponent(Graphics g)
    {

        super.paintComponent(g);
        // play background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, 650, 600);

        // right side background
        g.setColor(Color.BLACK);
        g.fillRect(0, 610, 650, 60);

//         draw solid bricks
        br.drawSolids(this, g);

//        draw Breakable bricks
        br.draw(this, g);


        // draw player 1, game will not start but the tanks are allowed to move
        if(playerID == 1){
            if (player1up) {
                player1 = new ImageIcon(bufferedP1_up);
            }
            else if (player1down) {
                player1 = new ImageIcon(bufferedP1_down);
            }
            else if (player1right) {
                player1 = new ImageIcon(bufferedP1_right);
            }
            else if (player1left) {
                player1 = new ImageIcon(bufferedP1_left);
            }
            player1.paintIcon(this, g, player1X, player1Y);

            if (player2up) {
                player2 = new ImageIcon(bufferedP2_up);
            }
            else if (player2down) {
                player2 = new ImageIcon(bufferedP2_down);
            }
            else if (player2right) {
                player2 = new ImageIcon(bufferedP2_right);
            }
            else if (player2left) {
                player2 = new ImageIcon(bufferedP2_left);
            }

            play = false;
            g.setColor(Color.white);
            g.drawString("Waiting for Player 2...", 20,20);

        }


        // draw player 2, game will start
        if(playerID == 2){
            if (player1up) {
                player1 = new ImageIcon(bufferedP1_up);
            }
            else if (player1down) {
                player1 = new ImageIcon(bufferedP1_down);
            }
            else if (player1right) {
                player1 = new ImageIcon(bufferedP1_right);
            }
            else if (player1left) {
                player1 = new ImageIcon(bufferedP1_left);
            }
            player1.paintIcon(this, g, player1X, player1Y);

            if (player2up) {
                player2 = new ImageIcon(bufferedP2_up);
            }
            else if (player2down) {
                player2 = new ImageIcon(bufferedP2_down);
            }
            else if (player2right) {
                player2 = new ImageIcon(bufferedP2_right);
            }
            else if (player2left) {
                player2 = new ImageIcon(bufferedP2_left);
            }

            player2.paintIcon(this, g, player2X, player2Y);
            play = true;
        }

        // Will only be able to shoot if play = true
        if(play) {

            // p1 can only shoot one bullet at a time
            if (player1Bullet != null && player1Shoot) {
                // p1 direction drawing
                if (bulletShootDir1.equals("")) {
                    if (player1up) {
                        bulletShootDir1 = "up";
                    } else if (player1down) {
                        bulletShootDir1 = "down";
                    } else if (player1right) {
                        bulletShootDir1 = "right";
                    } else if (player1left) {
                        bulletShootDir1 = "left";
                    }
                } else {
                    player1Bullet.move(bulletShootDir1);
                    player1Bullet.draw(g);
                }

                // p1 bullet hitbox collision with p2
                if (new Rectangle(player1Bullet.getX(), player1Bullet.getY(), 10, 10)
                        .intersects(new Rectangle(player2X, player2Y, 50, 50))) {
                    player1score += 20;
                    player2lives -= 1;
                    player1Bullet = null;
                    player1Shoot = false;
                    bulletShootDir1 = "";
                }

                // p1 bullet disappears when colliding
                if(player1Bullet != null) {
                    for(int i=0; i< br.brickON.length && player1Bullet != null;i++)
                    {
                        if (br.checkCollisionForBullets(player1Bullet.getX(), player1Bullet.getY())
                                || br.checkSolidCollisionForBullets(player1Bullet.getX(), player1Bullet.getY())) {
                            player1Bullet = null;
                            player1Shoot = false;
                            bulletShootDir1 = "";
                            br.brickON[i] = 0;
                        }

                    }

                }
                // p1 bullet disappears when out of bounds
                if (player1Bullet != null) {
                    if (player1Bullet.getY() < 1
                            || player1Bullet.getY() > 580
                            || player1Bullet.getX() < 1
                            || player1Bullet.getX() > 630) {
                        player1Bullet = null;
                        player1Shoot = false;
                        bulletShootDir1 = "";
                    }
                }
            }

            // p2 can only shoot one bullet at a time
            if (player2Bullet != null && player2Shoot) {
                // p2 direction drawing
                if (bulletShootDir2.equals("")) {
                    if (player2up) {
                        bulletShootDir2 = "up";
                    } else if (player2down) {
                        bulletShootDir2 = "down";
                    } else if (player2right) {
                        bulletShootDir2 = "right";
                    } else if (player2left) {
                        bulletShootDir2 = "left";
                    }
                } else {
                    player2Bullet.move(bulletShootDir2);
                    player2Bullet.draw(g);
                }

                // p2 bullet hitbox collision with p1
                if (new Rectangle(player2Bullet.getX(), player2Bullet.getY(), 10, 10)
                        .intersects(new Rectangle(player1X, player1Y, 50, 50))) {
                    player2score += 20;
                    player1lives -= 1;
                    player2Bullet = null;
                    player2Shoot = false;
                    bulletShootDir2 = "";
                }
                // p2 bullet disappears when colliding
                if (player2Bullet != null) {
                    for(int i=0; i< br.brickON.length && player2Bullet != null;i++)
                    {
                        if (br.checkCollisionForBullets(player2Bullet.getX(), player2Bullet.getY())
                                || br.checkSolidCollisionForBullets(player2Bullet.getX(), player2Bullet.getY())) {
                            player2Bullet = null;
                            player2Shoot = false;
                            bulletShootDir2 = "";
                            br.brickON[i] = 0;

                        }

                    }

                    // p2 bullet disappears when out of bounds
                    if (player2Bullet != null) {
                        if (player2Bullet.getY() < 1
                                || player2Bullet.getY() > 580
                                || player2Bullet.getX() < 1
                                || player2Bullet.getX() > 630) {
                            player2Bullet = null;
                            player2Shoot = false;
                            bulletShootDir2 = "";
                        }
                    }
                }
            }
        }



        // the scores
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD, 15));
        g.drawString("Scores", 100,630);
        g.drawString("Player 1:  "+player1score, 100,645);
        g.drawString("Player 2:  "+player2score, 100,660);

        g.drawString("Lives", 500,630);
        g.drawString("Player 1:  "+player1lives, 500,645);
        g.drawString("Player 2:  "+player2lives, 500,660);

        if(player1lives <= 0)
        {
            g.setColor(Color.white);
            g.setFont(new Font("serif",Font.BOLD, 60));
            g.drawString("Game Over", 200,300);
            g.drawString("Player 2 Won", 180,380);
            g.setColor(Color.white);
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("(Space to Restart)", 230,430);
            play = false;

        }
        else if(player2lives <= 0)
        {
            g.setColor(Color.white);
            g.setFont(new Font("serif",Font.BOLD, 60));
            g.drawString("Game Over", 200,300);
            g.drawString("Player 1 Won", 180,380);
            g.setColor(Color.white);
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("(Space to Restart)", 230,430);
            play = false;
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        repaint();
    }

//source: https://stackoverflow.com/questions/2623995/swings-keylistener-and-multiple-keys-pressed-at-the-same-time

    private class MultiKeyPressListener implements KeyListener{
        private final Set<Integer> pressedKeys = new HashSet<>();

        @Override
        public void keyTyped(KeyEvent e) {

        }

        // KeyListeners that allows multiple key inputs from the keyboard simultaneously
        @Override
        public void keyPressed(KeyEvent e) {
            pressedKeys.add(e.getKeyCode());
            if (!pressedKeys.isEmpty()) {
                for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext(); ) {
                    switch (it.next()) {
                        case KeyEvent.VK_W:

//
                            player1right = false;
                            player1left = false;
                            player1down = false;
                            player1up = true;

                            boolean shouldP1MoveUp = true;

                            // checks the tank collision. If collided, it makes the boolean false. same with the other cases.
                            for(int i = 0; i < br.getBricksXPos().length ; i++) {
                                if (br.checkCollisionForTanks(player1X,player1Y) || br.checkSolidCollisionForTanks(player1X,player1Y)) {
                                    shouldP1MoveUp = false;
                                }
                            }

                            if (shouldP1MoveUp){
                                if((player1Y >= 10)){
                                    player1Y -= 10;
                                }
                            }
                            break;

                        case KeyEvent.VK_UP:
                            player2right = false;
                            player2left = false;
                            player2down = false;
                            player2up = true;

                            boolean shouldP2MoveUp = true;
                            for(int i = 0; i < br.getBricksXPos().length ; i++) {
                                if (br.checkCollisionForTanks(player2X,player2Y) || br.checkSolidCollisionForTanks(player2X,player2Y)) {
                                    shouldP2MoveUp = false;
                                }
                            }
                            if (shouldP2MoveUp){
                                if(player2Y >= 10){
                                    player2Y -= 10;
                                }
                            }
                            break;
                        case KeyEvent.VK_A:
                            player1right = false;
                            player1left = true;
                            player1down = false;
                            player1up = false;

                            boolean shouldP1MoveLeft = true;
                            for(int i = 0; i < br.getBricksXPos().length ; i++) {
                                if (br.checkCollisionForTanks(player1X,player1Y) || br.checkSolidCollisionForTanks(player1X,player1Y)) {
                                    shouldP1MoveLeft = false;
                                }
                            }
                            if (shouldP1MoveLeft){
                                if(player1X >= 10){
                                    player1X -= 10;
                                }
                            }

                            break;
                        case KeyEvent.VK_LEFT:
                            player2right = false;
                            player2left = true;
                            player2down = false;
                            player2up = false;

                            boolean shouldP2MoveLeft = true;
                            for(int i = 0; i < br.getBricksXPos().length ; i++) {
                                if (br.checkCollisionForTanks(player2X,player2Y) || br.checkSolidCollisionForTanks(player2X,player2Y)) {
                                    shouldP2MoveLeft = false;
                                }
                            }
                            if (shouldP2MoveLeft){
                                if(player2X >= 10){
                                    player2X -= 10;
                                }
                            }
                            break;

                        case KeyEvent.VK_S:
                            player1right = false;
                            player1left = false;
                            player1down = true;
                            player1up = false;

                            boolean shouldP1MoveDown = true;
                            for(int i = 0; i < br.getBricksXPos().length ; i++) {
                                if (br.checkCollisionForTanks(player1X,player1Y) || br.checkSolidCollisionForTanks(player1X,player1Y)) {
                                    shouldP1MoveDown = false;
                                }
                            }
                            if (shouldP1MoveDown){
                                if(player1Y <= 550){
                                    player1Y += 10;
                                }
                            }

                            break;
                        case KeyEvent.VK_DOWN:
                            player2right = false;
                            player2left = false;
                            player2down = true;
                            player2up = false;

                            boolean shouldP2MoveDown = true;
                            for(int i = 0; i < br.getBricksXPos().length ; i++) {
                                if (br.checkCollisionForTanks(player2X,player2Y) || br.checkSolidCollisionForTanks(player2X,player2Y)) {
                                    shouldP2MoveDown = false;
                                }
                            }
                            if (shouldP2MoveDown){
                                if(player2Y <= 550){
                                    player2Y += 10;
                                }
                            }
                            break;
                        case KeyEvent.VK_D:
                            player1right = true;
                            player1left = false;
                            player1down = false;
                            player1up = false;

                            boolean shouldP1MoveRight = true;
                            for(int i = 0; i < br.getBricksXPos().length ; i++) {
                                if (br.checkCollisionForTanks(player1X,player1Y) || br.checkSolidCollisionForTanks(player1X,player1Y)) {
                                    shouldP1MoveRight = false;
                                }
                            }
                            if (shouldP1MoveRight){
                                if(player1X <= 600){
                                    player1X += 10;
                                }
                            }

                            break;
                        case KeyEvent.VK_RIGHT:
                            player2right = true;
                            player2left = false;
                            player2down = false;
                            player2up = false;

                            boolean shouldP2MoveRight = true;
                            for(int i = 0; i < br.getBricksXPos().length ; i++) {
                                if (br.checkCollisionForTanks(player2X,player2Y) || br.checkSolidCollisionForTanks(player2X,player2Y)) {
                                    shouldP2MoveRight = false;
                                }
                            }
                            if (shouldP2MoveRight){
                                if(player2X <= 600){
                                    player2X += 10;
                                }
                            }
                            break;
                        case KeyEvent.VK_U:
                            // generates the bullet depending on what the tank is currently facing.
                            if(!player1Shoot)
                            {
                                if(player1up)
                                {
                                    player1Bullet = new Player1BulletGenerator(player1X + 20, player1Y);
                                }
                                else if(player1down)
                                {
                                    player1Bullet = new Player1BulletGenerator(player1X + 20, player1Y + 40);
                                }
                                else if(player1right)
                                {
                                    player1Bullet = new Player1BulletGenerator(player1X + 40, player1Y + 20);
                                }
                                else if(player1left)
                                {
                                    player1Bullet = new Player1BulletGenerator(player1X, player1Y + 20);
                                }

                                player1Shoot = true;
                            }
                            break;
                        case KeyEvent.VK_M:
                            if(!player2Shoot)
                            {
                                if(player2up)
                                {
                                    player2Bullet = new Player2BulletGenerator(player2X + 20, player2Y);
                                }
                                else if(player2down)
                                {
                                    player2Bullet = new Player2BulletGenerator(player2X + 20, player2Y + 40);
                                }
                                else if(player2right)
                                {
                                    player2Bullet = new Player2BulletGenerator(player2X + 40, player2Y + 20);
                                }
                                else if(player2left)
                                {
                                    player2Bullet = new Player2BulletGenerator(player2X, player2Y + 20);
                                }

                                player2Shoot = true;
                            }
                            break;
                        case KeyEvent.VK_SPACE:
                            // Restarts the game once its over.
                            if(player1lives <= 0 || player2lives <= 0){
                                br = new BrickGenerator();
                                player1X = 200;
                                player1Y = 550;
                                player1right = false;
                                player1left = false;
                                player1down = false;
                                player1up = true;

                                player2X = 400;
                                player2Y = 550;
                                player2right = false;
                                player2left = false;
                                player2down = false;
                                player2up = true;

                                player1score = 0;
                                player1lives = 5;
                                player2score = 0;
                                player2lives = 5;
                                play = true;
                                repaint();
                            }
                    }
                }
            }
        }

        // mainly for returning the tanks to previous position upon collision
        @Override
        public void keyReleased(KeyEvent e) {
            if (!pressedKeys.isEmpty()) {
                for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext(); ) {
                    switch (it.next()) {
                        case KeyEvent.VK_A:

                            // responsible for returning the tanks 15x away from the intersection.
                            if (br.checkCollisionForTanks(player1X,player1Y) || br.checkSolidCollisionForTanks(player1X,player1Y)) {
                                player1X += 15;
                            }

                            break;
                        case KeyEvent.VK_LEFT:


                            for(int i = 0; i < br.getBricksXPos().length ; i++) {
                                if (br.checkCollisionForTanks(player2X,player2Y) || br.checkSolidCollisionForTanks(player2X,player2Y)) {
                                    player2X += 15;
                                }
                            }
                            break;
                        case KeyEvent.VK_W:
                            // responsible for returning the tanks 15y away from the intersection.
                            for(int i = 0; i < br.getBricksXPos().length ; i++) {
                                if (br.checkCollisionForTanks(player1X,player1Y) || br.checkSolidCollisionForTanks(player1X,player1Y)) {
                                    player1Y += 15;
                                }
                            }
                            break;
                        case KeyEvent.VK_UP:

                            for(int i = 0; i < br.getBricksXPos().length ; i++) {
                                if (br.checkCollisionForTanks(player2X,player2Y) || br.checkSolidCollisionForTanks(player2X,player2Y)) {
                                    player2Y += 15;
                                }
                            }
                            break;
                        case KeyEvent.VK_S:

                            for(int i = 0; i < br.getBricksXPos().length ; i++) {
                                if (br.checkCollisionForTanks(player1X,player1Y) || br.checkSolidCollisionForTanks(player1X,player1Y)) {
                                    player1Y -= 15;
                                }
                            }
                            break;
                        case KeyEvent.VK_DOWN:

                            for(int i = 0; i < br.getBricksXPos().length ; i++) {
                                if (br.checkCollisionForTanks(player2X,player2Y) || br.checkSolidCollisionForTanks(player2X,player2Y)) {
                                    player2Y -= 15;
                                }
                            }
                            break;

                        case KeyEvent.VK_RIGHT:

                            for (int i = 0; i < br.getBricksXPos().length; i++) {
                                if (br.checkCollisionForTanks(player2X,player2Y) || br.checkSolidCollisionForTanks(player2X,player2Y)) {
                                    player2X -= 15;
                                }
                            }
                            break;

                        case KeyEvent.VK_D:

                            for (int i = 0; i < br.getBricksXPos().length; i++) {
                                if (br.checkCollisionForTanks(player1X,player1Y) || br.checkSolidCollisionForTanks(player1X,player1Y)) {
                                    player1X -= 15;
                                }
                            }
                            break;
                    }
                }
            }

            pressedKeys.remove(e.getKeyCode());
        }


    }




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

    // responsible for connecting to local server
    public void connectToServer(){
        try{
            socket = new Socket("localhost", 45371);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt();
            System.out.println("You are player# " + playerID);
            if(playerID==1){
                System.out.println(("Waiting for Player #2 to connect..."));
            }
        } catch(IOException ex){
            System.out.println("IOException from connectToServer()");
        }
    }

}
