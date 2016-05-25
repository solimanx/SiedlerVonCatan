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

	public void write() throws IOException {
		//TODO wrap
		try (Writer writer = new OutputStreamWriter(new FileOutputStream("FIELDFIELD.json"), "UTF-8")) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(f, writer);
		}

	}

	public static Field read() throws IOException {
		try (Reader reader = new InputStreamReader(Main.class.getResourceAsStream("/Test.json"), "UTF-8")) {
			Gson gson = new GsonBuilder().create();
			Field p = gson.fromJson(reader, Field.class);
			return p;
		}

	}
}
