package network.client.view;

import java.util.HashMap;

import enums.HarbourStatus;
import enums.ResourceType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class ViewFactory {
	private String folder = "default/";
	

	private HashMap<enums.ResourceType, Color> fieldColors = new HashMap<enums.ResourceType, Color>(6);
	private HashMap<ResourceType, ImagePattern> resourceImages = new HashMap<ResourceType, ImagePattern>(6);
	private HashMap<HarbourStatus, ImagePattern> harbourImages = new HashMap<HarbourStatus, ImagePattern>(6);
	private HashMap<enums.ResourceType, ImagePattern> imagePatterns = new HashMap<enums.ResourceType, ImagePattern>(6);

	/**
	 * Constructor of ViewFactory.
	 * @param mode 2 is alternative Theme
	 */
	public ViewFactory(String theme) {
		folder = theme + "/";
		makeTheme();

	}

	public HashMap<enums.ResourceType, Color> getFieldColors() {
		return fieldColors;
	}

	public HashMap<ResourceType, ImagePattern> getResourceImages() {
		return resourceImages;
	}

	public HashMap<HarbourStatus, ImagePattern> getHarbourImages() {
		return harbourImages;
	}

	public HashMap<enums.ResourceType, ImagePattern> getImagePatterns() {
		return imagePatterns;
	}

	private void makeTheme() {
        fieldColors.put(ResourceType.CLAY, Color.web("#A1887F"));
        fieldColors.put(ResourceType.CORN, Color.web("#FFEE58"));
        fieldColors.put(ResourceType.NOTHING, Color.web("#FAFAFA"));
        fieldColors.put(ResourceType.ORE, Color.web("#9E9E9E"));
        fieldColors.put(ResourceType.SHEEP, Color.web("#9CCC65"));
        fieldColors.put(ResourceType.WOOD, Color.web("#26A69A"));
        fieldColors.put(ResourceType.SEA, Color.web("#81D4FA"));

        ImagePattern woodPattern = new ImagePattern(new Image("/textures/"+ folder + "wood.jpg"));
        ImagePattern clayPattern = new ImagePattern(new Image("/textures/"+ folder + "clay.jpg"));
        ImagePattern woolPattern = new ImagePattern(new Image("/textures/"+ folder + "sheep.jpg"));
        ImagePattern cornPattern = new ImagePattern(new Image("/textures/"+ folder + "corn.jpg"));
        ImagePattern orePattern = new ImagePattern(new Image("/textures/"+ folder + "ore.jpg"));
        ImagePattern desertPattern = new ImagePattern(new Image("/textures/"+ folder + "desert.jpg"));
        ImagePattern seaPattern = new ImagePattern(new Image("/textures/"+ folder + "sea.jpg"));

        imagePatterns.put(ResourceType.WOOD, woodPattern);
        imagePatterns.put(ResourceType.CLAY, clayPattern);
        imagePatterns.put(ResourceType.SHEEP, woolPattern);
        imagePatterns.put(ResourceType.CORN, cornPattern);
        imagePatterns.put(ResourceType.ORE, orePattern);
        imagePatterns.put(ResourceType.NOTHING, desertPattern);
        imagePatterns.put(ResourceType.SEA, seaPattern);

        ImagePattern woodHarbour = new ImagePattern(new Image("/textures/"+ folder + "woodHarbour.jpg"));
        ImagePattern clayHarbour = new ImagePattern(new Image("/textures/"+ folder + "clayHarbour.jpg"));
        ImagePattern woolHarbour = new ImagePattern(new Image("/textures/"+ folder+  "sheepHarbour.jpg"));
        ImagePattern cornHarbour = new ImagePattern(new Image("/textures/"+ folder + "cornHarbour.jpg"));
        ImagePattern oreHarbour = new ImagePattern(new Image("/textures/"+ folder + "oreHarbour.jpg"));
        ImagePattern genericHarbour = new ImagePattern(new Image("/textures/"+ folder+ "genericHarbour.jpg"));

        harbourImages.put(HarbourStatus.WOOD, woodHarbour);
        harbourImages.put(HarbourStatus.CLAY, clayHarbour);
        harbourImages.put(HarbourStatus.SHEEP, woolHarbour);
        harbourImages.put(HarbourStatus.CORN, cornHarbour);
        harbourImages.put(HarbourStatus.ORE, oreHarbour);
        harbourImages.put(HarbourStatus.THREE_TO_ONE, genericHarbour);
	}

}
