//package finalProject;
/**
 This is the class responsible for creating the game server in which the game connects to.
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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class GameServer {

    private ServerSocket ss;
    private int numPlayers;
    private int maxPlayers;


    public GameServer(){
        System.out.println("==== Game Server ====");
        numPlayers = 0;
        maxPlayers = 2;

        try{
            ss = new ServerSocket(45371);
        } catch(IOException ex){
            System.out.println("IOException from GameServer Constructor");
        }
    }

    public void acceptConnections(){
        try{
            System.out.println("Waiting for connections...");

            while(numPlayers < maxPlayers){
                Socket s = ss.accept();
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                numPlayers++;
                out.writeInt(numPlayers);
                System.out.println("Player #" + numPlayers + " has connected.");
            }

            System.out.println("No longer accepting connections");
        } catch(IOException ex){
            System.out.println("IOException from acceptConnections()");
        }
    }


    public static void main(String[] args){
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}
