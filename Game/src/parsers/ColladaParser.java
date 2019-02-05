package parsers;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ColladaParser {
	
	public static ColladaGeometry parse(String filepath) {
		
		try {
			File fXmlFile = new File(ColladaParser.class.getResource(filepath).toURI());
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			Node scenes = ((Element) doc.getElementsByTagName("library_visual_scenes").item(0)).getElementsByTagName("visual_scene").item(0);
			Node animation = doc.getElementsByTagName("library_animations").item(0);
			
			NodeList skins = doc.getElementsByTagName("skin");
			NodeList geometries = doc.getElementsByTagName("geometry");
			for(int i = 0; i< skins.getLength(); i++) {
				
				Node skin = skins.item(i);
				
				String id = skin.getAttributes().getNamedItem("source").getTextContent().trim().split("#")[1];
				for(int j = 0; j < geometries.getLength(); j++) 
					if(geometries.item(j).getAttributes().getNamedItem("id").getTextContent().equals(id)) {
						ColladaGeometry cd = new ColladaGeometry(geometries.item(j), skin, scenes, animation);
						return cd;
					}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
