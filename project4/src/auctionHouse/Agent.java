
package auctionHouse;

import java.net.Socket;

/**
 * Project 4 - CS351,Fall 2020
 * @version Date 2020-12-07
 * @author Datenzing Tamang, Rohit Kathariya
 *
 */


public class Agent {

    // socket to communicate with agent
    private Socket agentClient = null;

    // agent id
    private int agentId;


    public Agent(Socket socket, int agentId){
        this.agentClient = socket;
        this.agentId = agentId;
    }

    /**
     *
     * @return socket to communicate with agent
     */

    public Socket getAgentClient() {

        return agentClient;
    }

    /**
     * @return agent id
     */

    public int getAgentId() {

        return agentId;
    }

}


