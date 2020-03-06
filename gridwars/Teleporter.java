package gridwars;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Teleporter{

	// Positional variables.
	
	public int x;
	public int y;
	
	
	// Linking.
	
	public int id;
	public int toId;
	

	// Constructor
	
	public Teleporter(int posX, int posY) {
		x = posX;
		y = posY;
	}


	// Imaging.
	
	public ImageView image;
	
	public void setImage(Image image) {
		this.image = new ImageView(image);
	}
	
}
