package auctionHouse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Project 4 - CS351,Fall 2020
 * @version Date 2020-12-07
 * @author Datenzing Tamang, Rohit Kathariya
 */

public class AuctionServer implements Runnable{

    // socket for bank
    private Socket bankSocket = null;

    // port number
    private int portNumber;

    // address for server
    private String address;

    // items of suction house
    private List<items> itemList;

    // auction house number
    private int auctionNumber;

    //check current bid
    private static  boolean currentlyBidding = false;


    public AuctionServer(int portNumber, String address,
                         List<items> itemList, Socket bankSocket,
                         int auctionNumber) {
        this.portNumber = portNumber;
        this.address = address;
        this.itemList = itemList;
        this.bankSocket = bankSocket;
        this.auctionNumber = auctionNumber;
    }


    /**
     * Starts server for an agent to communicate with auction house
     *
     */

    @Override
    public void run() {

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);

            //starts new thread for new agent

            while (true) {
                Socket serverClient = serverSocket.accept(); //accept client side
                Thread threadAuctionClientThread = new
                        Thread(new AuctionClientThread(serverClient,
                        itemList, bankSocket, auctionNumber));
                threadAuctionClientThread.start();


            }

        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }



    /**
     * @return boolean, return true if someone is currently bidding
     */

    public static boolean biddingStatus() {

        return AuctionClientThread.biddingStatus();
    }
}