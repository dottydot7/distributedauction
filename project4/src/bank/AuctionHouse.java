package bank;
/**
 * Project 4 - CS351,Fall 2020
 * @version Date 2020-12-07
 * @author Datenzing Tamang, Rohit Kathariya
 * Class for auction house which hold the properties of the individual auction house
 */

public class AuctionHouse {

    // auction host name
    private String auctionHost;

    // auction port for communication
    private int auctionPort;

    // balance of auction house
    private int auctionHouseBalance = 0;

    // auction house ID
    private  int auctionHouseID;

    /**
     * constructor to set the id, host address and the port of the auction house.
     * @param auctionHouseID
     * @param auctionHost
     * @param auctionPort
     */
    public AuctionHouse(int auctionHouseID, String auctionHost, int auctionPort){
        this.auctionHost = auctionHost;
        this.auctionPort = auctionPort;
        this.auctionHouseID = auctionHouseID;
    }


    /**
     *
     * @returns the auction house host
     */
    public String getAuctionHost(){

        return auctionHost;
    }

    /**
     *
     * @returns returns the auction house port
     */
    public int getAuctionPort(){

        return auctionPort;
    }

    /**
     *
     * @returns the auction house balance
     */
    public int getAuctionHouseBalance(){

        return auctionHouseBalance;
    }

    /**
     *
     * @returns auction house unique id
     */
    public int getAuctionHouseID(){

        return auctionHouseID;
    }

    /**
     *
     * @param add adds the auction house balance
     */
    public void addAuctionBalance(int add){

        auctionHouseBalance += add;
    }
}