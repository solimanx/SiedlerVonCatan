package parsing.objects;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import enums.ResourceType;
import model.Field;

/**
 * 
 * <b>4.1: "Felder"</b>
 * <p>
 * Determines field's ID (location), resource type and chip/dice index.
 * </p>
 * 
 * @version 0.1
 */
public class FieldTypeAdapter extends TypeAdapter<Field> {

	@Override
	public void write(JsonWriter out, Field field) throws IOException {
		// TODO Wrap??
		out.beginObject(); // {
		out.name("Cancer"); // Feld

		out.beginObject();
		out.name("Ort").value(field.getFieldID());
		out.name("Typ").value(serialize(field.getResourceType()));
		out.name("Zahl").value(field.getDiceIndex());
		out.endObject();

		out.endObject();

	}

	// TODO better solution
	private String serialize(ResourceType resourceType) {
		switch (resourceType) {
		case WOOD:
			return "Wald";
		case CLAY:
			return "Hügelland";
		case CORN:
			return "Ackerland";
		case ORE:
			return "Gebirge";
		case SHEEP:
			return "Weideland";
		case NOTHING:
			return "Unbekannt"; // TODO handle in protokol
		}
		return null;
	}

	@Override
	public Field read(JsonReader in) throws IOException {
		System.out.println("Was here");
		final Field field = new Field();
		in.beginObject();
		if (in.nextName() == "Cancer") {
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
				case "Ort":
					field.setFieldID(in.nextString());
					break;
				case "Typ":
					field.setResourceType(deserialize(in.nextString()));
					break;
				case "Zahl":
					field.setDiceIndex(in.nextInt()); // TODO filter bad input
					break;
				}
			}
			in.endObject();
			in.endObject();
			return field;
		}
		
		return null; //TODO loggging

	}

	private ResourceType deserialize(String nextString) {
		switch (nextString) {
		case "Wald":
			return ResourceType.WOOD;
		case "Hügelland":
			return ResourceType.CLAY;
		case "Ackerland":
			return ResourceType.CORN;
		case "Gebirge":
			return ResourceType.ORE;
		case "Weideland":
			return ResourceType.SHEEP;
		}
		return ResourceType.NOTHING;
	}

}
