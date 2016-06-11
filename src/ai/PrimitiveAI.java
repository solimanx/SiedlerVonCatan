package ai;

import model.GameLogic;
import network.client.controller.ClientController;
import settings.DefaultSettings;

/**
 * Primivite AI that can connect to server, play the initial rounds, build two
 * villages and two roadss.
 */
public class PrimitiveAI {
	// 3-part corner coordinate (e.g (0,0,1)
	int[] firstVillageLocation;
	int[] secondVillageLocation;
	// 3-part edge coordinate (e.g (0,1,0)
	int[] firstRoadLocation;
	int[] secondRoadLocation;

	// USELESS GAMELOGIC/BOARD ONLY TO SEE CODE
	GameLogic gl;
	ClientController cc;
	
	public PrimitiveAI(){
		System.out.println("Entered AI");
	}
	/**
	 * Builds a village at the first legal empty location, then attaches a
	 * roads to it, then repeats it.
	 */
	public void initialRound() {
		int radius = DefaultSettings.BOARD_RADIUS;

		// VILLAGE
		// axial y coordinate loop
		outerloop: for (int i = -radius; i <= radius; i++) {
			// axial x coordinate loop
			for (int j = -radius; j <= radius; j++) {
				// corner direction loop
				for (int k = 0; k < 2; k++) {
					// if corner exists and is unoccupied
					if (gl.checkBuildInitialVillage(j, i, k)) {
						if (firstVillageLocation == null) {
							firstVillageLocation = new int[] { j, i, k };
						} else {
							secondVillageLocation = new int[] { j, i, k };
						}
						// TODO cc.requestBuildInitialVillage(j, i, k);
						break outerloop;

					}
				}
			}
		}
		// Build road at first village location
		int x, y, dir;
		if (secondVillageLocation == null) {
			x = firstVillageLocation[0];
			y = firstVillageLocation[1];
			dir = firstVillageLocation[2];

			// find first non null road
			if (dir == 0) {
				if (gl.getBoard().getEdgeAt(x, y - 1, 2) != null) {
					// TODO cc.requestBuildInitialRoad(x,y,dir);
					firstRoadLocation = new int[] { x, y - 1, 2 };
				} else if (gl.getBoard().getEdgeAt(x, y, 1) != null) {
					// TODO cc.requestBuildInitialRoad(x,y,dir);
					firstRoadLocation = new int[] { x, y, 1 };
				} else if (gl.getBoard().getEdgeAt(x, y, dir) != null) {
					// TODO cc.requestBuildInitialRoad(x,y,dir);
					firstRoadLocation = new int[] { x, y, dir };
				} else {
					throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
				}
			} else if (dir == 1) {
				if (gl.getBoard().getEdgeAt(x, y + 1, 0) != null) {
					// TODO cc.requestBuildInitialRoad(x,y+1,0);
					secondRoadLocation = new int[] { x, y + 1, 0 };
				} else if (gl.getBoard().getEdgeAt(x-1, y-1, 2) != null) {
					// TODO cc.requestBuildInitialRoad(x-1,y-1,2);
					secondRoadLocation = new int[] { x-1, y-1, 2 };
				} else if (gl.getBoard().getEdgeAt(x-1, y-1, dir) != null) {
					// TODO cc.requestBuildInitialRoad(x-1,y-1,dir);
					secondRoadLocation = new int[] { x-1, y-1, dir };
				} else {
					throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
				}

			}
			else{
				throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
			}

		}

		// // ROAD
		// Boolean roadBuilt = false;
		// Edge[] projectingEdges;
		// if (secondVillageLocation == null) {
		// projectingEdges =
		// gl.getBoard().getProjectingEdges(firstVillageLocation[0],
		// firstVillageLocation[1],
		// firstVillageLocation[2]);
		// } else {
		// projectingEdges =
		// gl.getBoard().getProjectingEdges(secondVillageLocation[0],
		// secondVillageLocation[1],
		// secondVillageLocation[2]);
		// }
		//
		// if (projectingEdges != null) {
		// for (int i = 0; i < projectingEdges.length; i++) {
		// if (projectingEdges[i] != null && !roadBuilt) {
		//
		// }
		//
		// }
		// }

	}
}
