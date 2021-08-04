package auctionHouse;
/**
 * Project 4 - CS351,Fall 2020
 * @version Date 2020-12-07
 * @author Datenzing Tamang, Rohit Kathariya
 */
public class items {

    // item naem
    private String name;

    // minimum bid
    private int minBid;

    // agent with highest bit
    private int agentWithBid;


    public items(String name, int minBid){
        this.name = name;
        this.minBid = minBid;
        this.agentWithBid = -1;

    }

    /**
     * Gets min bid
     *
     * @return  String name, name of item
     *
     */
    public String getName(){

        return name;
    }

    /**
     * Gets min bid of item
     * @return  int minBid
     * minimum money to purchase the item
     *
     */
    public int getMinBid(){

        return minBid;
    }


    public void setMinBid(int amount, int agentID){
        this.minBid = amount;
        this.agentWithBid = agentID;
    }

    /**
     * Returns agent with current highest bid
     * @return  agent id
     *
     */
    public int getAgentWithBid(){

        return agentWithBid;
    }
}
