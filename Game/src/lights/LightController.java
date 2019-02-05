package lights;

import java.util.ArrayList;

import org.joml.Vector3f;

public class LightController {
	
	private ArrayList<Light> lights = new ArrayList<Light>();
	
	public void removeLight(Light l) {
		lights.remove(l);
	}
	
	public void addLight(Light l) {
		lights.add(l);
	}
	
	public Light[] getLights(){
		Light[] lightList = new Light[lights.size()];
		for(int i = 0; i < lightList.length; i++)
			lightList[i] = lights.get(i);
		return lightList;
	}
	
	public Light getClosestLight(Vector3f pos) {
		int index = -1;
		float dist = Float.MAX_VALUE;
		Vector3f temp = new Vector3f(0);
		for(int i = 0; i < lights.size(); i++) {
			lights.get(i).getPosition().sub(pos, temp);
			if(temp.length() < dist) {
				dist = temp.length();
				index = i;
			}
		}
		try {
			return lights.get(index);
		}catch(Exception e) {
			return null;
		}
	}


}

