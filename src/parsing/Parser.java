package parsing;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Parser {
	Response response;
	Gson gson = new GsonBuilder().create();
	Map<String,Object> map;

	public Parser() {
		map = new HashMap<String, Object>();
	}

	@SuppressWarnings("unchecked")
	public <T> T parseString(String string) {

		response = gson.fromJson(string, Response.class);

		refreshMap();
	 
		for ( Map.Entry<String, Object> entry : map.entrySet() ) {
			if(entry.getValue()!=null){
				return (T) entry.getValue();
			}
		   
		}
		return null;
	}

	private void refreshMap() {
		map.put("ProtocolHello", response.pHello);
		//and so on
		map.put("ChatSend", response.pChatSend);
		map.put("ChatReceive", response.pChatReceive);
		//and so on
	}

	public String createString(Object fromObject) {
		String response = gson.toJson(fromObject, fromObject.getClass());
		return response;
	}
}
