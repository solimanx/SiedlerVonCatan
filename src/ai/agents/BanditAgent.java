package ai.agents;

import java.util.ArrayList;

import ai.AdvancedAI;
import enums.ResourceType;
import model.objects.Corner;
import network.ModelToProtocol;
import settings.DefaultSettings;

// TODO: Auto-generated Javadoc
/**
 * Handles the bandit removing process for the ai.
 */
public class BanditAgent {
	private AdvancedAI aai;
	double[][] robberscale = new double[7][7];
	int[] bestRobberPosition;
	private OpponentAgent oa;
	private ArrayList<Integer> differentPlayers;
	private Integer myTarget;

	/**
	 * Instantiates a new bandit agent.
	 *
	 * @param aai
	 *            the aai
	 * @param oa
	 *            the oa
	 */
	public BanditAgent(AdvancedAI aai, OpponentAgent oa) {
		this.aai = aai;
		this.oa = oa;
	}

	/**
	 * Finding best location to move bandit to.
	 */
	public void moveRobber() {
		int radius = DefaultSettings.boardRadius;
		// Check all fields
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				// value of the field
				// (the higher the more likely to move bandit on)
				int buildingsValue = 0;
				// If it exists and isn't already the bandit
				if (aai.getGl().getBoard().getFieldAt(i, j) != null
						&& !aai.getGl().getBoard().getBandit().equals(ModelToProtocol.getFieldID(i, j))) {
					// and isn't sea or desert
					if (!aai.getGl().getBoard().getFieldAt(i, j).getResourceType().equals(ResourceType.NOTHING)
							&& !aai.getGl().getBoard().getFieldAt(i, j).getResourceType().equals(ResourceType.SEA)) {
						// get corners around it
						Corner[] surroundingCorners = aai.getGl().getBoard().getSurroundingCorners(i, j);
						differentPlayers = new ArrayList<Integer>();
						innerloop: for (int k = 0; k < surroundingCorners.length; k++) {
							// if it exists
							if (surroundingCorners[k] != null) {
								// if it has an owner
								if (surroundingCorners[k].getOwnerID() != null) {
									// if it is me
									if (surroundingCorners[k].getOwnerID() == aai.getID()) {
										// check next field
										break innerloop;
									}
									// if it isn't me
									else {
										// increase value depending on building
										// type
										switch (surroundingCorners[k].getStatus()) {
										case VILLAGE:
											buildingsValue += 1;
											break;
										case CITY:
											buildingsValue += 2;
											break;
										default:
											break;
										}
										// increase the arraylist size depending
										// on amount of different players
										if (!differentPlayers.contains(surroundingCorners[k].getOwnerID())) {
											differentPlayers.add(surroundingCorners[k].getOwnerID());
										}
									}

								}
							}
						}
						robberscale[i + radius][j + radius] = buildingsValue / 6.0;
						if (differentPlayers.size() > 0) {
							robberscale[i + radius][j + radius] += 1.0 - (double) differentPlayers.size() / 4.0;
						}
					}
				}
			}
		}

	}

	/**
	 * Return best robber coordinates, by finding the highest robberscale value.
	 *
	 * @return the int[]
	 */
	public int[] bestNewRobber() {
		int radius = DefaultSettings.boardRadius;
		double max = robberscale[0][0];
		int x = 0;
		int y = 0;
		for (int i = 0; i < robberscale.length; i++) {
			for (int j = 0; j < robberscale[0].length; j++) {
				if (robberscale[i][j] > max) {
					max = robberscale[i][j];
					x = i;
					y = j;
				}

			}
		}
		bestRobberPosition = new int[] { x - radius, y - radius };
		bestVictim(bestRobberPosition);
		return bestRobberPosition;
	}

	/**
	 * Finding the coordinates of best victim on the specified field.
	 *
	 * @param coords
	 *            the coords
	 */
	// TODO communicate with opponent agent which opponent should be targeted
	protected void bestVictim(int[] coords) {
		// if(differentPlayers.size()==1){
		// FOR NOW
		differentPlayers = new ArrayList<Integer>();
		Corner[] c = aai.getGl().getBoard().getSurroundingCorners(coords[0], coords[1]);
		for (int i = 0; i < c.length; i++) {
			if (c[i].getOwnerID() != null) {
				if (!differentPlayers.contains(c[i].getOwnerID())) {
					differentPlayers.add(c[i].getOwnerID());
				}
			}
		}
		setTarget(differentPlayers.get(0));
		// }
		// else
		// for(int i=0; i<differentPlayers.size(); i++){
		// send differentPlayers.get(i) to newRobber
		// get their value
		// choose highest value
		// combine with bestNewRobber()
		// and back to requestsetBandit in outputhandler
		// }
	}

	/**
	 * Sets the target victim ID.
	 *
	 * @param integer
	 *            the new target
	 */
	private void setTarget(Integer integer) {
		myTarget = integer;
		robberscale = new double[7][7];

	}

	/**
	 * Gets the target victim ID.
	 *
	 * @return the target
	 */
	public Integer getTarget() {
		return myTarget;
	}
}
