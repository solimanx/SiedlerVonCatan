package protocol.cheats;

import com.google.gson.annotations.SerializedName;

import enums.CheatCode;
// TODO: Auto-generated Javadoc
/**
 * Cheatcode sent by Client to Server
 * @author Roxana
 * 
 *
 */
public class ProtocolCheat {

	@SerializedName("Code")
	private CheatCode cc;

	/**
	 * Instantiates a new protocol cheat.
	 *
	 * @param cc the cc
	 */
	public ProtocolCheat(CheatCode cc) {
		this.cc = cc;
	}

	/**
	 * Gets the cheat code.
	 *
	 * @return the cheat code
	 */
	public CheatCode getCheatCode() {
		return cc;
	}
	
}
