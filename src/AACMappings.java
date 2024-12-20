import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import util.AssociativeArray;

/**
 * Creates a set of mappings of an AAC that has two levels, one for categories
 * and then within each
 * category, it has images that have associated text to be spoken. This class
 * provides the methods
 * for interacting with the categories and updating the set of images that would
 * be shown and
 * handling an interactions.
 * 
 * @author Catie Baker & Sebastian Manza
 *
 */
public class AACMappings implements AACPage {
	private static AACCategory DEFAULT_CAT = new AACCategory("");
	/* The name of the overall AssociativeArray we are working with */
	AssociativeArray<String, AACCategory> mappings;
	/* The name of the last category we were working with */
	AACCategory currentCat = DEFAULT_CAT;

	/**
	 * Creates a set of mappings for the AAC based on the provided file. The file is
	 * read in to create
	 * categories and fill each of the categories with initial items. The file is
	 * formatted as the
	 * text location of the category followed by the text name of the category and
	 * then one line per
	 * item in the category that starts with > and then has the file name and text
	 * of that image
	 * 
	 * for instance: img/food/plate.png food >img/food/icons8-french-fries-96.png
	 * french fries
	 * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png
	 * clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing and food has
	 * french fries and
	 * watermelon and clothing has a collared shirt
	 * 
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) {
		try {
			Scanner eyes = new Scanner(new File(filename));
			String input;
			this.mappings = new AssociativeArray<>();
			while (eyes.hasNext()) {
				input = eyes.nextLine();
				String[] inputs = input.split(" ", 2);
				if (inputs[0].charAt(0) != '>') {
					this.mappings.set(inputs[0], new AACCategory(inputs[1]));
					this.currentCat = this.mappings.get(inputs[0]);
				} else {
					this.currentCat.addItem(inputs[0].substring(1), inputs[1]);
				}
			}
			this.currentCat = DEFAULT_CAT;
			eyes.close();
		} catch (FileNotFoundException e) {
			System.err.println("File does not exist.");
		}
	}

	/**
	 * Given the image location selected, it determines the action to be taken. This
	 * can be updating
	 * the information that should be displayed or returning text to be spoken. If
	 * the image provided
	 * is a category, it updates the AAC's current category to be the category
	 * associated with that
	 * image and returns the empty string. If the AAC is currently in a category and
	 * the image
	 * provided is in that category, it returns the text to be spoken.
	 * 
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise
	 *         it returns the
	 *         empty string
	 * @throws NoSuchElementException if the image provided is not in the current
	 *                                category
	 */
	public String select(String imageLoc) throws NoSuchElementException {
		if ((currentCat != DEFAULT_CAT) && (this.currentCat.hasImage(imageLoc))) {
			return this.currentCat.select(imageLoc);
		} else if (this.mappings.hasKey(imageLoc)) {
			this.currentCat = this.mappings.get(imageLoc);
			return "";
		} else
			throw new NoSuchElementException("image ");
	}

	/**
	 * Provides an array of all the images in the current category
	 * 
	 * @return the array of images in the current category; if there are no images,
	 *         it should return
	 *         an empty array
	 */
	public String[] getImageLocs() {
		if (this.currentCat == DEFAULT_CAT) {
			String[] categoryImages = new String[this.mappings.size()];
			for (int i = 0; i < this.mappings.size(); i++) {
				categoryImages[i] = this.mappings.getKey(i);
			}
			return categoryImages;
		} else {
			return this.currentCat.getImageLocs();
		}
	}

	/**
	 * Resets the current category of the AAC back to the default category
	 */
	public void reset() {
		this.currentCat = DEFAULT_CAT;
	}

	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as the text
	 * location of the
	 * category followed by the text name of the category and then one line per item
	 * in the category
	 * that starts with > and then has the file name and text of that image
	 * 
	 * for instance: img/food/plate.png food >img/food/icons8-french-fries-96.png
	 * french fries
	 * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png
	 * clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing and food has
	 * french fries and
	 * watermelon and clothing has a collared shirt
	 * 
	 * @param filename the name of the file to write the AAC mapping to
	 */
	public void writeToFile(String filename) {
		try {
			FileWriter writer = new FileWriter(filename);
			for (int i = 0; i < this.mappings.size(); i++) {
				String url = this.mappings.getKey(i);
				String catName = this.mappings.get(url).getCategory();
				writer.write(url + " " + catName);
				for (int j = 0; j < this.mappings.get(url).contents.size(); j++) {
					String urlInner = this.mappings.get(url).contents.getKey(i);
					String itemName = this.mappings.get(url).contents.get(urlInner);
					writer.write(">" + urlInner + " " + itemName);
				}
			}
			writer.close();
		} catch (Exception IOException) {
			return;
		}
	}

	/**
	 * Adds the mapping to the current category (or the default category if that is
	 * the current
	 * category)
	 * 
	 * @param imageLoc the location of the image
	 * @param text     the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
		this.currentCat.addItem(imageLoc, text);
	}

	/**
	 * Gets the name of the current category
	 * 
	 * @return returns the current category or the empty string if on the default
	 *         category
	 */
	public String getCategory() {
		return currentCat.getCategory();
	}

	/**
	 * Determines if the provided image is in the set of images that can be
	 * displayed and false
	 * otherwise
	 * 
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that can be displayed, false
	 *         otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return (currentCat.hasImage(imageLoc));
	}
}
