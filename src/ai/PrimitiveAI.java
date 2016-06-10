package ai;

import model.GameLogic;
import model.objects.Edge;
import network.client.controller.ClientController;
import settings.DefaultSettings;

/**
 * Primivite AI that can connect to server, play the initial rounds, build two
 * villages and two streets.
 */
public class PrimitiveAI {
	// 3-part coordinate (e.g (0,0,1)
	int[] firstVillageLocation;
	int[] secondVillageLocation;

	// USELESS GAMELOGIC/BOARD ONLY TO SEE CODE
	GameLogic gl;
	ClientController cc;

	/**
	 * Builds one village at the first empty location
	 */
	public void initialRound() {
		int radius = DefaultSettings.BOARD_RADIUS;
		Boolean villageBuilt = false;

		// VILLAGE
		// axial y coordinate loop
		for (int i = -radius; i <= radius; i++) {
			// axial x coordinate loop
			for (int j = -radius; j <= radius; j++) {
				// corner direction loop
				for (int k = 0; k < 2; k++) {
					// if corner exists and is unoccupied
					if (gl.checkBuildInitialVillage(j, i, k) && villageBuilt != true) {
						if (firstVillageLocation == null) {
							firstVillageLocation = new int[] { j, i, k };
						} else {
							secondVillageLocation = new int[] { j, i, k };
						}
						// TODO cc.requestBuildInitialVillage(j, i, k);
						villageBuilt = true;

					}
				}
			}
		}

		// STREET
		Boolean streetBuilt = false;
		Edge[] projectingEdges;
		if (secondVillageLocation == null) {
			projectingEdges = gl.getBoard().getProjectingEdges(firstVillageLocation[0], firstVillageLocation[1],
					firstVillageLocation[2]);
		} else {
			projectingEdges = gl.getBoard().getProjectingEdges(secondVillageLocation[0], secondVillageLocation[1],
					secondVillageLocation[2]);
		}

		if (projectingEdges != null) {
			for (int i = 0; i < projectingEdges.length; i++) {
				if(projectingEdges[i]!=null && !streetBuilt){
				
				}

			}
		}

	}
}
