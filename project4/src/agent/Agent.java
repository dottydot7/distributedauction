package agent;

import java.io.*;
import java.net.Socket;

/**
 * Project 4 - CS351,Fall 2020
 * @version Date 2020-12-07
 * @author Datenzing Tamang, Rohit Kathariya
 *
 * This class is for agent where bank server is created and for waiting the bid message to be received from the auction house.
 */

public class Agent {

    //socket to communicate with bank
    private Socket bankSocket = null;

   // read message from bank socket
    private DataInputStream inputBank = null;

   // write message to bank socket
    private DataOutputStream outputBank = null;

    //socket to communicate with auction
    private Socket auctionSocket = null;

    //read message from auction socket
    private DataInputStream auctionInput = null;

    //write message to auction socket
    private DataOutputStream auctionOutput = null;

   // agent number to store agent ID
    private int agentNumber = 0;


    //auction information
    private String[] auctionList;

    Thread wait = null;

    /**
     * start() method prompts for information of the bank host name and port name to communicate with the bank.
     */

    public void start() {
        try {

            BufferedReader buffread = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter Bank Host: ");
            String bankHost = buffread.readLine();

            String bankPort = "";
            boolean isLegal = false;
            while (!isLegal) {
                System.out.println("Enter Bank's port: ");
                bankPort = buffread.readLine();
                if (bank.Bank.isInteger(bankPort)) {
                    isLegal = true;
                }
            }

            bankSocket = new Socket(bankHost, Integer.parseInt(bankPort));
            inputBank = new DataInputStream(bankSocket.getInputStream());
            outputBank = new DataOutputStream(bankSocket.getOutputStream());
            String serverMessage = "";
            System.out.println("Enter agent name : ");
            String agentName = buffread.readLine();

            String money = "";
            boolean isMoney = false;
            while (!isMoney) {
                System.out.println("Enter the money : ");
                money = buffread.readLine();
                if (bank.Bank.isInteger(money) && Integer.parseInt(money) >= 0) {
                    isMoney = true;
                }
            }
            outputBank.writeUTF("a " + agentName + " " + money);
            outputBank.flush();
            serverMessage = inputBank.readUTF();
            System.out.println(serverMessage);
            int i = 0;
            String number = "";

            while (serverMessage.charAt(i) != ' ') {
                number = number + serverMessage.charAt(i);
                i++;
            }

            agentNumber = Integer.parseInt(number);

            home();


        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }

    /**
     * this method guides agent through the menu.
     */
    public void home() throws IOException {
        String in = "";
        BufferedReader buffread = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(
                "Enter 'list' to get list of Auction Houses \n" +
                "Enter 'balance' to check balance \n" +
                "Enter 'exit' to close account");
        try {
            in = buffread.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }


        if(in.equals("list")){
            try {
                    outputBank.writeUTF("ListAuctionHouse");
                    outputBank.flush();
                    String arr = inputBank.readUTF();
                    auctionList = arr.split(" ");

                    auctionHouseMenu();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
                home();
        }
        else if(in.equals("balance")){
            try {
                    outputBank.writeUTF("CheckBalance");
                    outputBank.flush();
                    String bankMessage = inputBank.readUTF();
                    System.out.println(bankMessage);
                    home();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
                home();
        }
        else if(in.equals("close")){
            if (!(wait == null) && wait.isAlive()) {
                    System.out.println("Agent is waiting for message");
                    home();
                } else {
                    try {

                        outputBank.writeUTF("terminate " + agentNumber);
                        outputBank.flush();

                        try {
                            outputBank.close();
                            inputBank.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } catch (IOException e) {
                        System.out.println(e.toString());

                    }
                    System.exit(0);
                }
        }
        else{
            home();
        }

    }

    /**
     * check the user input and displays the request
     */
    public void auctionHouseMenu() {
        BufferedReader buffread = new BufferedReader(new InputStreamReader(System.in));
        String menuInput = "";
        for (int i = 0; i < auctionList.length; i++) {
            if (i % 3 == 0 && auctionList.length > 2) {
                System.out.println("Auction House Number : " + (i / 3));
            }
        }
        if (auctionList.length < 3) {
            System.out.println("There are no currently any auction House. Waiting...");
            return;
        }
        System.out.println("Enter the auction house number");
        boolean isHouse = false;
        int houseNumber = 0;
        while (!isHouse) {
            System.out.println("Enter auction House Number: " +
                    "\nEnter 'b' to go back");
            try {
                menuInput = buffread.readLine();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            if (bank.Bank.isInteger(menuInput)) {
                houseNumber = Integer.parseInt(menuInput);
                if (houseNumber >= 0 && houseNumber < auctionList.length / 3) {
                    isHouse = true;
                }
            } else if (menuInput.equals("b")) {
                return;
            }
        }
        connectToAuctionHouse(auctionList[houseNumber * 3 + 1], Integer.parseInt(auctionList[houseNumber * 3 + 2]));

    }


    /**
     * connects to the auction house.
     * @param hostNumber
     * @param portNumber
     */
    public void connectToAuctionHouse(String hostNumber, int portNumber) {
        try {
            auctionSocket = new Socket(hostNumber, portNumber);
            auctionInput = new DataInputStream(auctionSocket.getInputStream());
            auctionOutput = new DataOutputStream(auctionSocket.getOutputStream());
            String clientMessage = "", serverMessage = "";

            clientMessage = agentNumber + " Agent Present";

            auctionOutput.writeUTF(clientMessage);

            serverMessage = auctionInput.readUTF();

            System.out.println(serverMessage);

            singleAuction();

            auctionInput.close();
            auctionOutput.close();
            auctionSocket.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * this method will provide the list of items in the auction house
     */
    public void singleAuction() {
        String in = "";
        BufferedReader buffread = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter 'list' to get list of items\n" +
                "Enter 'b' to go back");
        try {
            in = buffread.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        if(in.equals("list")){
            try {
                    auctionOutput.writeUTF("ItemList");
                    String[] strArr = auctionInput.readUTF().split(" ");
                    bid(strArr);

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
        }
        if(in.equals("b")){
            try {
                    auctionOutput.writeUTF("Terminate");
                    home();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
        }

    }

    /**agent can select item and bid
     * @param arr
     */
    public void bid(String[] arr) {
        System.out.println("Item list are given below: ");
        for (int i = 0; i < arr.length && arr.length > 1; i += 2) {
            System.out.println((i / 2 + 1) + ". " + arr[i] + " has bid money $ " + arr[i + 1]);
        }
        String itemNumber = "";
        String amountBid = "";

        BufferedReader buffread = new BufferedReader(new InputStreamReader(System.in));

        boolean isValid = false;

        while (!isValid) {
            System.out.println("Enter the Item number to select item:" +
                    "\nOr Type 'b' to go back:");
            try {
                itemNumber = buffread.readLine();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            if (itemNumber.equals("1") || itemNumber.equals("2") || itemNumber.equals("3")) {
                System.out.println("Enter the bid money :" +
                        "\nOr Type 'b' to go back:");
                try {
                    amountBid = buffread.readLine();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
                if (bank.Bank.isInteger(amountBid)) {
                    isValid = true;
                } else if (amountBid.equals("b")) {
                    try {
                        auctionOutput.writeUTF("b");
                        auctionOutput.flush();
                    } catch (IOException e) {
                        System.out.println(e.toString());
                    }
                    bid(arr);
                } else {
                    System.out.println("Please enter the valid input");
                    bid(arr);
                }
            } else if (itemNumber.equals("b")) {
                try {
                    auctionOutput.writeUTF("b");
                    auctionOutput.flush();
                }
                catch (IOException e) {
                    System.out.println(e.toString());
                }
                singleAuction();
            }
        }

        try {
            System.out.println("Bid is placed on item " + itemNumber + " and bid is $ " + amountBid);
            auctionOutput.writeUTF(itemNumber + " " + amountBid);
            auctionOutput.flush();

            message(auctionInput);

            home();

        } catch (IOException e) {
            System.out.print(e.toString());
        }


    }
    public  void message(DataInputStream str) throws IOException {
        try {
            String string = str.readUTF();
            if (string.contains("Congratulations")) {
                string = "Sold";

            }
            if(string.contains("other")){
                string = "out";
            }
            if(string.contains("own")){
                string = "selfout";
            }


            switch (string) {
                case "fail":
                    System.out.println("Your bid was rejected.");
                    break;
                case "pass":
                    System.out.println("Your bid is placed.");
                    message(str);
                    break;
                case "Sold":
                    System.out.println("The item is sold to the agent");
                    break;
                case "out":
                    System.out.println("The item is sold out");
                    return;
                case "selfout":
                    System.out.println("The item is sold out");
                    message(str);
                    break;
                default:
                    System.out.println(string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Agent agent = new Agent();
        agent.start();
    }

}



