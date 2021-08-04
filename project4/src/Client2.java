import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Project 4 - CS351,Fall 2020
 * @version Date 2020-12-07
 * @author Datenzing Tamang, Rohit Kathariya
 */


public class Client2 {
    public static void main(String[] arg) {
        try {

            Socket socketConnection = new Socket("127.0.0.1", 11111);


            //QUERY PASSING
            DataOutputStream outToServer = new DataOutputStream(socketConnection.getOutputStream());

            String SQL="I am  Client 2";
            outToServer.writeUTF(SQL);


        } catch (Exception e) {System.out.println(e); }
    }
}