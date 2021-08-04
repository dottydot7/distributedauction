package bank;
/**
 * Project 4 - CS351,Fall 2020
 * @version Date 2020-12-07
 * @author Datenzing Tamang, Rohit Kathariya
 */
public class Agent {

    // agent id
    private int agentID;

    // agent balance or money
    private int agentMoney;

    //agent name
    private String agentName;

    /**
     * constructor to set the properties of the agent
     * @param agentID of the agent
     * @param agentMoney sets the bank balance of the agent
     * @param agentName sets the name of the agent
     */
    public Agent(int agentID, int agentMoney, String agentName) {

        this.agentID = agentID;

        this.agentMoney = agentMoney;

        this.agentName = agentName;
    }

    /**
     *
     * @param money subtracts money from agents account
     */
    public void subtract(int money){

        agentMoney = agentMoney - money;
    }

    /**
     *
     * @param money adds agents money
     */
    public void add(int money){

        agentMoney = agentMoney + money;
    }

    /**
     *
     * @return money in agents account
     */
    public int getAgentMoney(){

        return agentMoney;
    }

    /**
     *
     * @return the unique agent id
     */
    public int getAgentID(){

        return agentID;
    }

}