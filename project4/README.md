# Distributed Auction 

## Group Members: 
- Rohit Kathariya
- Datenzing Tamang


## Distributed Auction
This program will simulate a system of multiple auction houses selling items, multiple agents buying items, and a bank to keep track of everyone's funds. The bank will exist on one machine at a static known address, the agents and auction houses will be dynamically created on the other machines. 

## Bank 

- The bank is static at a known address and this program will be started before agents or auction houses. 
- On running the program, it will prompt user for Bank's Host Name and Port Number. Once that is set up, this program will remain running concurrently throughout the simulation. 
- Bank & Agent Relationship: Agent will open bank account with starting balance above 0. When an agent bids on r or is outbid in an auction, the bank will block or unblock the appropriate. When an agent wins an auction, the bank will transfer these blocked funds from the agent to the auction
house account, at the request of the agent.
- Bank & Auction House Relationship: Auction houses provide the bank with their host and port information. The bank provides the agents with the list of the auction houses and their addresses so the agents will be able to connect directly to the auction houses.

## Auction House 

- Each auction house is dynamically created. Upon creation, it registers with the bank, opening an account with zero balance. It also provides the bank with its host and port address,  so the bank can inform the agents of the existence of this auction house.
- It hosts a list of items being auctioned and tracks the current bidding status of each item. Initially, the auction house will offer at least 3 items for sale. As the items are sold, new items will be listed to replace them. 
- Upon request, it shares the list of items being auctioned and the bidding status with agents, including for each item house id, item id, description, minimum bid and current bid.
- The user may terminate the program when no bidding activity is in progress. The program should not allow exit when there are still bids to be resolved. At termination, it de-registers with the bank. An auction house terminating should not break the behavior of any other programs in the system.

## Agent 

- Each agent is dynamically created. Upon creation, it opens a bank account by providing a name and an initial balance, and receives a unique account number.
- The agent gets a list of active auction houses from the bank. In connects to an auction house using the host and port information sent from the bank. The agent receives a list of items being auctioned from the auction house.
- When an agent makes a bid on an item, it receives back one or more status messages as the auction proceeds:
• acceptance – The bid is the current high bid
• rejection – The bid was invalid, too low, insufficient funds in the bank, etc.
• outbid – Some other agent has placed a higher bid
• winner – The auction is over and this agent has won.
- The agent notifies the bank to transfer the blocked funds to the auction house after it wins a bid.
- The program may terminate when no bidding activity is in progress. The program should not allow exit when there are still bids to be resolved. At termination, it de-registers with the bank. An agent terminating should not break the behavior of any other programs in the system.

## Rules

- The auction house receives bids and acknowledges them with a reject or accept
response.
- When a bid is accepted, the bank is requested to block those funds. In fact,
the bid should not be accepted if there are not enough available funds in the
bank.
- When a bid is overtaken, an outbid notification is sent to the agent and the
funds are unblocked.
- A bid is successful if not overtaken in 30 seconds.3 When winning a bid, the
agent receives a winner notification and the auction house waits for the blocked
funds to be transferred into its account.
- If there has been no bid placed on an item, the item remains listed for sale.



