package bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Project 4 - CS351,Fall 2020
 * @version Date 2020-12-07
 * @author Datenzing Tamang, Rohit Kathariya
 */

public class Bank {

    // counter to tract clients
    private int counter = 0;

    /**
     * this method creates bank server
     * waits for new client and creates
     * a new thread is
     */
    public void server() {
        try {
            BufferedReader buffread = new BufferedReader(new InputStreamReader(System.in));
            String bankPort = "";
            boolean legalPort = false;
            while(!legalPort){
                System.out.println("Enter Bank port number: ");
                bankPort = buffread.readLine();
                if(isInteger(bankPort)){
                    legalPort = true;
                }
            }

            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(bankPort));

            System.out.println("Bank Server Started...");

            while (true) {
                counter++;
                Socket serverClient = serverSocket.accept(); //accept client side
                System.out.println("Client: " + counter + " started");
                BankClientThread bankClientThread = new BankClientThread(counter,serverClient);
                bankClientThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param str is checked if it is an integer.
     * @return true if the string passed is an integer.
     */
    public static boolean isInteger(String str){
        try{
            Integer.parseInt(str);
        } catch(NumberFormatException e){
            return false;
        } catch (NullPointerException e){
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.server();
    }
}