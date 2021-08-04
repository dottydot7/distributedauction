package bank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Project 4 - CS351,Fall 2020;
 * @version Date 2020-12-07
 * @author Datenzing Tamang, Rohit Kathariya
 *  Class for creating each bank client thread to handle each unique auction house and agent.
 */


public class BankClientThread extends Thread {

    //socket to communicate with client
    private Socket clientServer;

    //Stores the client number
    private int num;


    //data input stream to read the message coming from the socket

    private DataInputStream input;


    // data output stream to write the message going out to the socket.
    private DataOutputStream output;


    //string to store the client message from the socket.

    private String messageFromClient = "";


    //tring to store the server message to be sent out.

    private String serverMessageOut = "";


    //arraylist to store the auction house object.

    private static List<AuctionHouse> listOfAuctionHouses = new ArrayList<AuctionHouse>();


    //declaring the arraylist to store the agent object.
    private static List<Agent> listOfAgents = new ArrayList<Agent>();

    private AuctionHouse auctionHouse = null;

    private Agent agent = null;


    /**
     * constructor to set client and server number
     * @param num
     * @param clientServer
     */
    public BankClientThread(int num, Socket clientServer) {
        this.clientServer = clientServer;
        this.num = num;
    }

    /**
     * separate method to handle auction client and agent client
     * use client message and write to server
     */
    public void run() {
        try {
            input = new DataInputStream(clientServer.getInputStream());
            output = new DataOutputStream(clientServer.getOutputStream());

            serverMessageOut = num + " Connected to bank server.";
            output.writeUTF(serverMessageOut);
            output.flush();

            messageFromClient = input.readUTF();

            if (messageFromClient.split(" ")[0].equals("a")) {
                agentCommunicate(messageFromClient.split(" ")[1],
                        Integer.parseInt(messageFromClient.split(" ")[2]));
            } else if (messageFromClient.split(" ")[0].equals("h")) {
                auctionHouseCommunication(messageFromClient.split(" ")[1],
                        Integer.parseInt(messageFromClient.split(" ")[2]));
            }

            input.close();
            output.close();
            clientServer.close();

        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            System.out.println("Client: " + num + " exit");
        }
    }

    /**
     * we add new auction object to the auction arraylist.
     * @param auctionHost   of the auction house
     * @param auctionPort of the auction house.
     */
    public void auctionHouseCommunication(String auctionHost, int auctionPort) {

        try {
            auctionHouse = new AuctionHouse(num, auctionHost, auctionPort);
            listOfAuctionHouses.add(auctionHouse);

            output.writeUTF("Auction house is registered.");
            System.out.println(" The auction house host name is " + auctionHost + " and port is" + auctionPort);
            output.flush();

            auctionHouseMessage();

        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }

    /**
     *waits for message from auction house
     * replies to auction house base on message
     * @throws IOException
     */
    public void auctionHouseMessage() throws IOException {
        int agentNumber = -1;
        messageFromClient = input.readUTF();
        int subBalanceAgent = -1;
        int depositAuctionHouse = -1;
        int soldAmount = -1;

        //if the item is sold, item cost is added to the auction house account.
        if (messageFromClient.contains("Sold")) {
            String[] strArr = messageFromClient.split(" ");
            depositAuctionHouse = Integer.parseInt(strArr[2]);
            soldAmount = Integer.parseInt(strArr[3]);
            for (int i = 0; i < listOfAuctionHouses.size(); i++) {
                if (listOfAuctionHouses.get(i).getAuctionHouseID() == depositAuctionHouse) {
                    listOfAuctionHouses.get(i).addAuctionBalance(soldAmount);
                }
            }
        }

        //if the agent puts bid in the item, bank takes the money from agent account.
        if (messageFromClient.contains("Block")) {
            String[] strArr = messageFromClient.split(" ");
            subBalanceAgent = Integer.parseInt(strArr[1]);
            soldAmount = Integer.parseInt(strArr[2]);
            for (int i = 0; i < listOfAgents.size(); i++) {
                if (listOfAgents.get(i).getAgentID() == subBalanceAgent) {
                    listOfAgents.get(i).subtract(soldAmount);
                }
            }
        }

        //if agent gets outbidded, then the bank puts back money to the agents account
        if (messageFromClient.contains("Unblock")) {
            String[] strArr = messageFromClient.split(" ");
            subBalanceAgent = Integer.parseInt(strArr[1]);
            soldAmount = Integer.parseInt(strArr[2]);
            for (int i = 0; i < listOfAgents.size(); i++) {
                if (listOfAgents.get(i).getAgentID() == subBalanceAgent) {
                    listOfAgents.get(i).add(soldAmount);
                }
            }
        }

        //if the auction says terminate, then we remove auction house from the arraylist.
        if (messageFromClient.contains("terminate")) {
            String arr[] = messageFromClient.split(" ");
            int removeFromList = Integer.parseInt(arr[1]);
            for (int i = 0; i < listOfAuctionHouses.size(); i++) {
                if (listOfAuctionHouses.get(i).getAuctionHouseID() == removeFromList) {
                    listOfAuctionHouses.remove(i);
                }
            }
            input.close();
            output.close();
            clientServer.close();
        }

        //checks the amount of the agent.
        if (messageFromClient.contains("checkAgentAmount")) {
            agentNumber = Integer.parseInt(messageFromClient.split(" ")[1]);
            for (int i = 0; i < listOfAgents.size(); i++) {
                if (listOfAgents.get(i).getAgentID() == agentNumber) {
                    output.writeUTF("" + listOfAgents.get(i).getAgentMoney());
                }
            }
        }

        // to check meassge from auction house
        if(messageFromClient.equals("balance")) {
            output.writeUTF("Your balance is " + auctionHouse.getAuctionHouseBalance());
            auctionHouseMessage();

        }

        else if(messageFromClient.equals("terminate")) {
            output.writeUTF("Your program is terminating");
        }

        else {
            auctionHouseMessage();
        }

    }


    /**
     * @param agentName is set for agent name
     * @param money    is set for the money agent has while agent is opening the new account.
     */
    public void agentCommunicate(String agentName, int money) {

        try {
            agent = new Agent(num, money, agentName);
            listOfAgents.add(agent);
            System.out.println("Agent is registered in Bank");

            agentMessage();

        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }

    /**
     * this method waits for agent message
     * and replies back
     * @throws IOException
     */
    public void agentMessage() throws IOException {
        messageFromClient = input.readUTF();

        if(messageFromClient.equals("ListAuctionHouse")){
            output.writeUTF(auctionHouseRegistered());
            output.flush();

            agentMessage();

        }
        if(messageFromClient.equals("CheckBalance")){
            output.writeUTF("Your balance is " + agent.getAgentMoney());

            agentMessage();
        }

        if (messageFromClient.contains("terminate")) {
            for (int i = 0; i < listOfAgents.size(); i++) {
                if (listOfAgents.get(i).getAgentID() == Integer.parseInt(messageFromClient.split(" ")[1])) {
                    listOfAgents.remove(i);
                }
            }
            input.close();
            output.close();
            clientServer.close();
        }
    }

    /**
     * @returns the list of regestered auction house
     */
    public String auctionHouseRegistered() {
        String str = "";
        for (int i = 0; i < listOfAuctionHouses.size(); i++) {
            str += listOfAuctionHouses.get(i).getAuctionHouseID() + " " +
                    listOfAuctionHouses.get(i).getAuctionHost() + " " +
                    listOfAuctionHouses.get(i).getAuctionPort() + " ";
        }
        return str;
    }
}