package auctionHouse;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Integer.parseInt;

/**
 * Project 4 - CS351,Fall 2020
 * @version Date 2020-12-07
 * @author Datenzing Tamang, Rohit Kathariya
 */

public class AuctionHouse {

    //list of items
    private List<items> itemList = new ArrayList<items>();

    //InputStream to get data
    private  DataInputStream inputStream = null;

    //OutputStream to send data
    private DataOutputStream outputStream = null;

    //Socket for communication
    private Socket socket = null;

    //Auction House balance
    private int balance = 0;

    //boolean to check the current bidding
    private boolean isCurrentlyBidding = false;

    // auction house number
    private int auctionNumber = -1;




    /**
     * Starts the connection with bank and opens server for connecting
     * with auction house
     */

    public void start() {
        try {

            BufferedReader buffread = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter Bank Host: ");
            String bankHost = buffread.readLine();
            setItem();


            String bankPort = "";
            boolean isLegal = false;
            while(!isLegal){
                System.out.println("Enter Bank port: ");
                bankPort = buffread.readLine();
                if(bank.Bank.isInteger(bankPort)){
                    isLegal = true;
                }
            }


            System.out.println("Enter auctionHouse Host: ");
            String auctionHost = buffread.readLine();


            String auctionPort = "";
            boolean isLegalPort = false;
            while(!isLegalPort){
                System.out.println("Enter auctionHouse port: ");
                auctionPort = buffread.readLine();
                if(bank.Bank.isInteger(auctionPort)){
                    isLegalPort = true;
                }
            }


            //connections with bank
            socket = new Socket(bankHost, parseInt(bankPort));
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            int a = 0;
            String message = inputStream.readUTF();

            String number = "";

            while (message.charAt(a) != ' ' ) {
                number = number + message.charAt(a);
                a++;
            }


            auctionNumber = Integer.parseInt(number);



            Thread threadServer = new Thread(new AuctionServer
                    (parseInt(auctionPort),auctionHost
                            , itemList, socket, auctionNumber));
            threadServer.start();

            String serverMessage = "";//



            outputStream.writeUTF("h " + auctionHost +
                    " "+ auctionPort);
            outputStream.flush();

            serverMessage = inputStream.readUTF();
            //reads the input from the agent to place bid

            auctionChecks();



        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }



    /**
     * Menu for the auction house to check balance or terminate the program
     */

    public void auctionChecks() throws IOException {

        String in = "";
        BufferedReader buffread = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter 'b' to check balance \n"
                +"Enter 't' to terminate the program");

        try {
            in = buffread.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        if(in.equals("b")){
            try {
                outputStream.writeUTF("balance");
                outputStream.flush();
                System.out.println(inputStream.readUTF());
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            auctionChecks();
        }
        else if(in.equals("t")){
            if(AuctionServer.biddingStatus()) {
                System.out.println("Bid is in process at the moment");
                auctionChecks();
            }

            else {
                try {
                     outputStream.writeUTF("terminate "+auctionNumber);
                    outputStream.flush();
                    try {
                        outputStream.close();
                        inputStream.close();
                    } catch (IOException ex) {
                        System.out.println(ex.toString());
                    }
                } catch (IOException e) {
                    System.out.println(e.toString());

                }
                try {
                    socket.close();
                    inputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
                System.exit(0);
            }
        }
        else{
            auctionChecks();
        }

    }



    /**
     * Sets item in the list of items
     */
    public void setItem(){
        itemList.add(new items("LGTV", 100));
        itemList.add(new items("macbook", 700));
        itemList.add(new items("JBL", 50));
        itemList.add(new items("airpod", 100));
        itemList.add(new items("HP", 300));
        itemList.add(new items("Fifa2021", 10));
        itemList.add(new items("Dellmonitor", 120));
        itemList.add(new items("MLbook", 5));
    }



    public static void main(String[] args) {

        AuctionHouse auctionHouse = new AuctionHouse();

        auctionHouse.start();
    }

}