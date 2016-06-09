package protocol3.object;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Since;
import protocol.object.ProtocolResource;

@Since(0.3)
public class ProtocolInventionCard {
	@SerializedName("Rohstoffe")
	ProtocolResource resource;
	
	@SerializedName("Rohstoffe")
	ProtocolResource resource2;


	public ProtocolInventionCard (ProtocolResource resource) {
		this.resource = resource;
	}

	public ProtocolResource getResource() {
		return resource;
	}
}
