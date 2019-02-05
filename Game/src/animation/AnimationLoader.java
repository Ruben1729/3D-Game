package animation;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AnimationLoader {
	
	public static Animation loadAnim(String[] joints, Node animationNode) {
		Element animElem = (Element) animationNode;
		
		ArrayList<Float> timestamps = new ArrayList<Float>();
		ArrayList<Matrix4f> transforms = new ArrayList<Matrix4f>();
		
		NodeList animNodes = animElem.getElementsByTagName("animation");
		for(int i = 0; i < animNodes.getLength(); i++) {
			Element anim = (Element) animNodes.item(i);
			
			NodeList nSrc = anim.getElementsByTagName("source");
		
			for(int j = 0; j < nSrc.getLength(); j++) {
				
				String[] strSources = null;
				Element e = (Element) nSrc.item(j);
				
				if(e.getAttribute("id").contains("input") || e.getAttribute("id").contains("output"))
					strSources = e.getTextContent().trim().split(" ");
				
				if(e.getAttribute("id").contains("input") && i == 0) {
					
					for(String str: strSources)
						timestamps.add(Float.parseFloat(str));
					
				}else if(e.getAttribute("id").contains("output")) {
					
					for(int k = 0; k < strSources.length / 16; k++) {
						FloatBuffer data = BufferUtils.createFloatBuffer(16);
						for(int l = 0; l < 16; l++)
							data.put(Float.parseFloat(strSources[k * 16 + l]));
						data.flip();
						
						Matrix4f mat = new Matrix4f();
						mat.set(data);
						mat.transpose(mat);
						
						transforms.add(mat);
						if(i == 7)
						System.out.println(mat);
					}
					
				}
			}
		}
		
		KeyFrame[] keyFrames = new KeyFrame[timestamps.size()];
		for(int i = 0; i < timestamps.size(); i++) {
			
			float time = timestamps.get(i);
			
			int numJoints = transforms.size() / timestamps.size();
			HashMap<String, JointTransform> jointTransforms = new HashMap<String, JointTransform>();
			for(int j = 0; j < numJoints; j++) {
				
				Matrix4f mat = transforms.get(j * timestamps.size() + i);
				Vector3f pos = new Vector3f(mat.m30(), mat.m31(), mat.m32());
				
				Quaternion q = Quaternion.fromMatrix(mat);
				
				jointTransforms.put(joints[j], new JointTransform(pos, q));
				
			}
			
			keyFrames[i] = new KeyFrame(time, jointTransforms);
			
		}
		
		return new Animation(0.8333333f, keyFrames);
	}
	
}
