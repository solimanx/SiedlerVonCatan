package protocol.connection;

import com.google.gson.annotations.SerializedName;

public class ProtocolHello {
    
        @SerializedName("Version")
		public String version;
		@SerializedName("Protokoll")
		public String protokoll;

}