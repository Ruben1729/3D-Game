package parsers;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import animation.Joint;

public class ColladaSkin {
	
	/**
	 * Root joint of the skeleton.
	 */
	private Joint root;
	
	/**
	 * Joint hash map.
	 */
	private String[] jointNames;
	
	/**
	 * Parse a
	 * @param skinNode
	 * @param visualScenesNode
	 */
	public ColladaSkin(Node skinNode, Node visualScenesNode) {
		//this.skinNode = skinNode;
		
		parseColladaSkin(skinNode, visualScenesNode);
	}
	
	private void parseColladaSkin(Node skinNode, Node visualScenesNode) {
		
		Element array = (Element)((Element)skinNode).getElementsByTagName("Name_array").item(0);
		jointNames = array.getTextContent().trim().split(" ");
		HashMap<String, Integer> joints = new HashMap<String, Integer>();
		for(int i = 0; i < jointNames.length; i++)
			joints.put(jointNames[i], i);
		
		
		NodeList nodes = ((Element)visualScenesNode).getElementsByTagName("node");
		
		Element node = null;
		out:
		for(int i = 0; i < nodes.getLength(); i++) {
			NodeList moreNodes = ((Element)nodes.item(i)).getElementsByTagName("node");
			for(int j = 0; j < moreNodes.getLength(); j++) {
				node = (Element) moreNodes.item(j);
				if(createJoint(null, node, joints) != null)
					break out;
			}
			
		} 
		
		
		parseJoints(this.root, node, joints);
		
	}
	
	private String printJointsToString(Joint parent, boolean printParent, int indent) {
		
		String result = "";
		
		int size = parent.children.size();
		
		if(size != 0 && printParent)
			result += "\n" + createIndent(indent) + parent.name;
		
		
		result += "{";
		for(int i = 0; i < size; i++) {
			result += "\n" + createIndent(indent + 1) + parent.children.get(i).name;
			
			result += printJointsToString(parent.children.get(i), false, indent + 1);
			result += (i == size - 1 ? "" : ",");
		}
		result += size != 0 ? "\n" + createIndent(indent) + "}" : "}";
		
		return result;
	}
	
	private String createIndent(int size) {
		String result = "";
		for(int i = 0; i < size; i++)
			result += "\t";
		return result;
	}
	
	public String[] getJointNames() {
		return jointNames;
	}
	
	/**
	 * Creates a child joint and adds it to the parent joint's list of children.
	 * If the parent joint is null, then the created joint will be the root joint.
	 * @param parent The parent joint.
	 * @param node The node element of the child joint.
	 * @param joints The hash map of joint names and IDs.
	 * @return [Joint] The created joint.
	 */
	private Joint createJoint(Joint parent, Element node, HashMap<String, Integer> joints) {
		
		// Check if node is indeed a joint.
		if(!node.getAttribute("type").equals("JOINT"))
			return null;
		
		// Joint name.
		String jointName = node.getAttribute("sid");
		
		// Parse joint matrix.
		String[] matData = node.getElementsByTagName("matrix").item(0).getTextContent().trim().split(" ");
		
		
		FloatBuffer data = BufferUtils.createFloatBuffer(16);
		data.clear();
		float[] fData = new float[16];
		for(int i = 0; i < 16; i++)
			fData[i] = Float.parseFloat(matData[i]);
		
		data.put(fData);
		data.flip();
		
		Matrix4f mat = new Matrix4f();
		mat.set(data);
		mat.transpose(mat);
		
		// Create the joint.
		Joint result = new Joint(joints.get(jointName), jointName, mat);
		
		// Add the joint to the parent joint.
		if(parent == null)
			this.root = result;
		else
			parent.addChild(result);
		
		// Return the created joint.
		return result;
	}

	/**
	 * Recursively parses a joint's children joints.
	 * @param parent The parent joint.
	 * @param jointNode The parent joint's node element.
	 * @param joints The hash map of joint names and IDs.
	 * @return [void]
	 */
	private void parseJoints(Joint parent, Element jointNode, HashMap<String, Integer> joints) {
		NodeList childNodes = jointNode.getChildNodes();
		for(int i = 0; i < childNodes.getLength(); i++) {
			if(childNodes.item(i).getNodeType() != Node.ELEMENT_NODE) 
				continue;
			
			if(!((Element)childNodes.item(i)).getNodeName().equals("node"))
				continue;
			
			Joint newJoint = createJoint(parent, (Element)childNodes.item(i), joints);
			if(childNodes != null)
				parseJoints(newJoint, (Element)childNodes.item(i), joints);
			
		}
	}
	
	/**
	 * Returns the root joint of the skeleton.
	 * @return [Joint] The root joint that has no parent joint.
	 */
	public Joint getRootJoint(){
		return this.root;
	}

	@Override
	public String toString() {
		return printJointsToString(this.root, true, 0);
	}
}
