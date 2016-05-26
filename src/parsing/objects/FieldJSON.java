package parsing.objects;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.Main;
import model.Field;

public class FieldJSON {
	Field f;

	public FieldJSON(Field f) {
		this.f = f;

	}

	public String write() {
		Gson gson = new GsonBuilder().create();
		String result = gson.toJson(f);
		return result;

	}

	public static Field read() throws IOException {
		try (Reader reader = new InputStreamReader(Main.class.getResourceAsStream("/Test.json"), "UTF-8")) {
			Gson gson = new GsonBuilder().create();
			/* String p = gson.fromJson(reader, String.class);
			 * parse p nach erstem vorkommenden String;
			 * unterscheide zwischen 7.1 bis 7.4 
			 * Beispiel: Statusupdate
			 * StatusUpdateNetMessage m = gson.fromJson(reader alles was nach Statusupdate kommt, StatusUpdateNetMessage.class);
			 * handleStatusUpdateNetMessage(m) diese Methode ergänzt in m um alle anderen Variablen
			 * 
			 * return m
			 */
			Field p = gson.fromJson(reader, Field.class);
			return p;
		}

	}
}
