package ai.agents;

import java.util.ArrayList;

import ai.AdvancedAI;
import enums.ResourceType;
import model.objects.Corner;
import settings.DefaultSettings;

/**
 * Handles the bandit removing process for the ai.
 */
public class BanditAgent {
	private AdvancedAI aai;
	double[][] robberscale = new double[7][7];
	private OpponentAgent oa;

	public BanditAgent(AdvancedAI aai, OpponentAgent oa) {
		this.aai = aai;
		this.oa = oa;
	}

	/**
	 * Finding best location to move bandit to.
	 */
	protected void moveRobber() {
		int radius = DefaultSettings.BOARD_RADIUS;
		// Check all fields
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				// value of the field
				// (the higher the more likely to move bandit on)
				int buildingsValue = 0;
				// If it exists
				if (aai.getGl().getBoard().getFieldAt(i, j) != null) {
					// and isn't sea or desert
					if (!aai.getGl().getBoard().getFieldAt(i, j).getResourceType().equals(ResourceType.NOTHING)
							&& !aai.getGl().getBoard().getFieldAt(i, j).getResourceType().equals(ResourceType.SEA)) {
						// get corners around it
						Corner[] surroundingCorners = aai.getGl().getBoard().getSurroundingCorners(i, j);
						ArrayList<Integer> differentPlayers = new ArrayList<Integer>();
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
						robberscale[i + radius][j + radius] = buildingsValue / 6;
						if (differentPlayers.size() > 0) {
							robberscale[i + radius][j + radius] += 1 - differentPlayers.size() / 4;
						}
					}
				}
			}
		}

	}

	/**
	 * Return best robber coordinates, by finding the highest robberscale value.
	 * 
	 */
	protected int[] bestNewRobber() {
		int radius = DefaultSettings.BOARD_RADIUS;
		double max = robberscale[0][0];
		int x = 0;
		int y = 0;
		for (int i = 0; i < robberscale.length; i++) {
			for (int j = 0; j < robberscale.length; j++) {
				if (robberscale[i][j] > max) {
					max = robberscale[i][j];
					x = i;
					y = j;
				}

			}
		}
		return new int[] { x - radius, y - radius };
	}

	// TODO communicate with opponent agent which opponent should be targeted
	protected Integer bestVictim() {
		// get victims ids around (bestNewRobber) and target the best one
		// both information is sent to advanced ai and eventually to
		// requestSetBandit in outputhandler
		return null;
	}
}
