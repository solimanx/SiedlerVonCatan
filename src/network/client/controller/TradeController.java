package network.client.controller;

import java.util.ArrayList;

/**
 * @author NiedlichePixel Offers GUI trade-related methods
 */
public class TradeController {

	private ClientController clientController;
	private ArrayList<TradeOffer> tradeOffers = new ArrayList<TradeOffer>();

	public TradeController(ClientController clientController) {
		this.clientController = clientController;
	}

	/**
	 * @param viewController
	 */
	public void setViewController(ClientController clientController,ViewController viewController) {
		

	}
	
	public void addTrade(int modelID ,int tradingID,int[] supply,int[] demand){
		TradeOffer tOf = new TradeOffer(modelID,tradingID,supply,demand);
		tradeOffers.add(tOf);		
	}

	public void tradeAccepted(int modelID, int tradingID) {
		TradeOffer currTOf;
		for (int i = 0;i <tradeOffers.size();i++){
			currTOf = tradeOffers.get(i);
			if (currTOf.getTradingID() == tradingID){
				currTOf.acceptingPlayers.add(modelID);
			}
		}
		
	}

	public void tradeFulfilled(int modelID, int partnerID) {
	    //keine ID laut protokoll??
		
	}

	public void tradeCancelled(int modelID, int tradingID) {
		TradeOffer currTOf;
		for (int i = 0;i < tradeOffers.size();i++){
			currTOf = tradeOffers.get(i);
			if (currTOf.getTradingID() == tradingID){
				if (currTOf.getOwnerID() == modelID){
					tradeOffers.remove(i);
				} else {
					for (int j = 0; j <currTOf.acceptingPlayers.size();j++){
						if (currTOf.acceptingPlayers.get(j) == modelID){
							currTOf.acceptingPlayers.remove(j);
						}
					}
				}
				break;
			}
		}
		//tradeViewController.update
	}
	

}
