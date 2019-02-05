package GUI;

import java.io.File;

import org.joml.Vector2f;

import engine.Loader;

import textMeshCreator.FontType;
import textMeshCreator.GUIText;
import textRendering.TextMaster;

public class Button {
	
	GUIText text;
	FontType font;
	
	String message;
	float[] rgb;
	Vector2f position;
	boolean centered;
	float fontSize;
	
	public Button(Loader l, String message, float [] rgb, Vector2f position, boolean centered, FontType font, float fontSize) {
		
		this.message = message;
		this.rgb = rgb;
		this.position = position;
		this.centered = centered;
		this.fontSize = fontSize;
		
		TextMaster.init(l);
		this.font = font;
		this.text = new GUIText(message, fontSize, font, position, 1f, centered);
		text.setColour(rgb[0], rgb[1], rgb[2]);
		TextMaster.loadText(text);
		
	}


	public FontType getFont() {
		return font;
	}

	public void setFont(FontType font) {
		this.font = font;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public float[] getRgb() {
		return rgb;
	}

	public void setRgb(float[] rgb) {
		this.rgb = rgb;
	}
	
	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public boolean isCentered() {
		return centered;
	}

	public void setCentered(boolean centered) {
		this.centered = centered;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public void render() {
		
		TextMaster.render();
		
	}
	
	public void cleanUp() {
		
		TextMaster.cleanUp();
		
	}
	
}
