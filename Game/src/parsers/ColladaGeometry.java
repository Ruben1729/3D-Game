package parsers;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import animation.Animation;

/**
 * Represents a geometry object loaded from a collada file.
 */
public class ColladaGeometry {
	
	/**
	 * The geometry's ID.
	 */
	private String id;
	
	/**
	 * The geometry's name.
	 */
	private String name;
	
	/**
	 * The vertex list as given in the collada file.
	 */
	private ArrayList<Vector3f> vertices;
	
	/**
	 * The normal list as given in the collada file.
	 */
	private ArrayList<Vector3f> normals;
	
	/**
	 * The texture coordinate list as given in the collada file.
	 */
	private ArrayList<Vector2f> texCoords;
	
	/**
	 * Unordered list of weights.
	 */
	private ArrayList<Float> mixedWeights;
	
	/**
	 * Ordered list of vertex weight counts. 
	 * Its size is the same as the size of the vertex array list.
	 */
	private ArrayList<Integer> vcounts;
	
	/**
	 * Ordered list of joint IDs, 3 for each vertex.
	 */
	private ArrayList<Integer> jointIds;
	
	/**
	 * Ordered list of weights, 3 for each vertex.
	 */
	private ArrayList<Float> weights;
	
	/**
	 * The different semantic offsets for triangles defined in the collada file.
	 */
	private HashMap<String, Integer> semanticFaceOffset;
	
	/**
	 * The different semantic offsets for weights defined in the collada file.
	 */
	private HashMap<String, Integer> semanticWeightOffset;
	
	/**
	 * The parsed vertex list.
	 */
	private float[] vertexList;
	
	/**
	 * The parsed normal list.
	 */
	private float[] normalList;
	
	/**
	 * The parsed texture coordinate list.
	 */
	private float[] texList;
	
	/**
	 * The parsed weight list.
	 */
	private float[] weightList;
	
	/**
	 * The parsed joint list.
	 */
	private int[] jointList;
	
	/**
	 * The parsed index list.
	 */
	private int[] indexList;
	
	/**
	 * The geometry's skin.
	 */
	private ColladaSkin skin;
	
	/**
	 * The geometry's animation.
	 */
	private Animation anim;
	
	
	/**
	 * Creates a ColladaGeometry object that retrieves the data from the given node.
	 * @param geometryNode The geometry node from which to extract the data.
	 */
	public ColladaGeometry(Node geometryNode, Node skinNode, Node scenesNodes, Node animationsNode) {
		
		// Create lists.
		vertices = new ArrayList<Vector3f>();
		normals = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		
		mixedWeights = new ArrayList<Float>();
		jointIds = new ArrayList<Integer>();
		vcounts = new ArrayList<Integer>();
		
		weights = new ArrayList<Float>();
		
		// Create hash maps.
		semanticFaceOffset = new HashMap<String, Integer>();
		semanticWeightOffset = new HashMap<String, Integer>();
		
		// Parse the nodes.
		parseColladaGeometry(geometryNode, skinNode);
		
		// Parse the skin information.
		skin = new ColladaSkin(skinNode, scenesNodes);
		
		// Parse the animation.
		anim = AnimationLoader.loadAnim(skin.getJointNames(), animationsNode);
	}
	
	/**
	 * Parses a geometry node with its corresponding skin node (only one mesh per geometry) (meshes must be made out of triangles).
	 * @param geometryNode The geometry node to be parsed.
	 * @param skinNode The skin node corresponding to the geometry node.
	 * @return [void]
	 */
	private void parseColladaGeometry(Node geometryNode, Node skinNode) {
		
		// ID and Name of the geometry.
		this.id = geometryNode.getAttributes().getNamedItem("id").getTextContent();
		this.name = geometryNode.getAttributes().getNamedItem("name").getTextContent();
		
		// Output.
		System.out.println("Parsing geometry " + name + " with id " + id);
		
		// Find first mesh tag.
		Element mesh = null;
		for(int i = 0; i < geometryNode.getChildNodes().getLength(); i++)
			if(geometryNode.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
				mesh = (Element)geometryNode.getChildNodes().item(i);
				break;
			}
		
		Element skin = (Element) skinNode;
		
		// Parse geometry node source tags.
		NodeList sourceGeometryNodes = mesh.getElementsByTagName("source");
		for(int i = 0; i < sourceGeometryNodes.getLength(); i++) {
			
			Element srcElem = (Element) sourceGeometryNodes.item(i);
			String[] strSources = srcElem.getTextContent().trim().split(" ");
			
			if(srcElem.getAttributes().getNamedItem("id").getTextContent().contains("positions")) {
				
				// Vertices.
				for(int j = 0; j < strSources.length / 3; j++)
					vertices.add(new Vector3f(	Float.parseFloat(strSources[(j * 3)]),
												Float.parseFloat(strSources[(j * 3) + 1]),
												Float.parseFloat(strSources[(j * 3) + 2])));
			
			}else if(srcElem.getAttributes().getNamedItem("id").getTextContent().contains("normals")) {
				
				// Normals.	
				for(int j = 0; j < strSources.length / 3; j++)
					normals.add(new Vector3f(	Float.parseFloat(strSources[(j * 3)]),
												Float.parseFloat(strSources[(j * 3) + 1]),
												Float.parseFloat(strSources[(j * 3) + 2])));
			
			}else if(srcElem.getAttributes().getNamedItem("id").getTextContent().contains("map-0")) {
				
				// Texture coordinates.
				for(int j = 0; j < strSources.length / 2; j++)
					texCoords.add(new Vector2f(	Float.parseFloat(strSources[(j * 2)]),
												1.0f - Float.parseFloat(strSources[(j * 2) + 1])));
				
			}
			
		}
		
		// Parse skin node source tags.
		NodeList sourceSkinNodes = ((Element)skinNode).getElementsByTagName("source");
		for(int i = 0; i < sourceSkinNodes.getLength(); i++) {
			
			Element srcElem = (Element) sourceSkinNodes.item(i);
			
			if(srcElem.getAttributes().getNamedItem("id").getTextContent().contains("weights")) {
				
				String[] strSources = srcElem.getTextContent().trim().split(" ");
				for(String str : strSources)
					mixedWeights.add(Float.parseFloat(str));
				
				break;
			}
				
		}
		
		// Vertex weights element.
		Element vertexWeights = ((Element)skin.getElementsByTagName("vertex_weights").item(0));
		
		// Semantic weight offsets.
		NodeList weightSemantics = vertexWeights.getElementsByTagName("input");
		for(int i = 0; i < weightSemantics.getLength(); i++)
			semanticWeightOffset.put(	weightSemantics.item(i).getAttributes().getNamedItem("semantic").getTextContent(), 
										Integer.parseInt(weightSemantics.item(i).getAttributes().getNamedItem("offset").getTextContent()));
		
		// Parse vcounts.
		String[] strVcounts = vertexWeights.getElementsByTagName("vcount").item(0).getTextContent().trim().split(" ");
		for(String str : strVcounts)
			vcounts.add(Integer.parseInt(str));
		
		// Parse 'v' tag.
		ArrayList<Integer> v = new ArrayList<Integer>();
		String[] strV = vertexWeights.getElementsByTagName("v").item(0).getTextContent().trim().split(" ");
		for(String str : strV)
			v.add(Integer.parseInt(str));
		
		// Parse weights and joints.
		int totalCount = 0;
		int semCount = semanticWeightOffset.entrySet().size();
		for(int i = 0; i < vcounts.size(); i++) {
			
			// Get the joint count of the current vertex.
			int currentJointCount = vcounts.get(i);
			
			// Add the weights and joint IDs to the array lists.
			addWeightsAndIds(v, totalCount * semCount, currentJointCount);
			
			// Increment total count.
			totalCount += currentJointCount;
			
		}
		
		// Semantic face offsets.
		NodeList faceSemantics = ((Element)mesh.getElementsByTagName("triangles").item(0)).getElementsByTagName("input");
		for(int i = 0; i < faceSemantics.getLength(); i++)
			semanticFaceOffset.put(	faceSemantics.item(i).getAttributes().getNamedItem("semantic").getTextContent(), 
									Integer.parseInt(faceSemantics.item(i).getAttributes().getNamedItem("offset").getTextContent()));
		
		// List sizes.
		int vSize = vertices.size();
		int nSize = normals.size();
		int tSize = texCoords.size();
		int max = Math.max(vSize, Math.max(nSize, tSize));
		
		// Output.
		System.out.println("Parsed " + vSize + " vertices (" + vSize * 3 + " elements)");
		System.out.println("Parsed " + nSize + " normals (" + nSize * 3 + " elements)");
		System.out.println("Parsed " + tSize + " texture coordinates (" + tSize * 2 + " elements)");
		
		
		// Which list is the largest because that is the one we will arrange the others around.
		int largest = 	max == vSize ? 0 : 
						max == nSize ? 1 : 
							2;
		
		// Output.
		System.out.println("Adjusting indexes around " + (largest == 0 ? "vertex indexes." : 
														  largest == 1 ? "normal indexes." :
														  "texture indexes."));
		
		// Init all the float arrays.
		vertexList = new float[max * 3];
		weightList = new float[max * 3];
		jointList = new int[max * 3];
		normalList = new float[max * 3];
		texList = new float[max * 2];
		indexList = new int[max];
		
		// Parse all the triangles (we assume they are triangles).
		int numSemantics = faceSemantics.getLength();
		String[] strTriangles = ((Element)mesh.getElementsByTagName("triangles").item(0)).getTextContent().trim().split(" ");
		for(int i = 0 ; i < strTriangles.length / numSemantics; i++){
			
			// Group up the indices for a single vertex.
			int[] indices = new int[numSemantics];
			
			// Fill the array of indices.
			for(int j = 0; j < numSemantics; j++)
				indices[j] = Integer.parseInt(strTriangles[(i * numSemantics) + j]);
			
			// Process the vertex.
			processVertex(largest, indices);
			
		}
		
		
	}
	
	/**
	 * Adds the joint IDs and weights for a single vertex to the corresponding lists.
	 * If there are more than 3 weights affecting the same vertex,
	 * we take the 3 most influential weights and normalize them.
	 * Otherwise, we fill up the array with default values so that
	 * each vertex has 3 weights and 3 joint IDs. 
	 * @param v The 'v' list from the collada file.
	 * @param startIndex The beginning index for the set of weights.
	 * @param numWeights The number of weights affecting the vertex.
	 * @return [void]
	 */

	private void addWeightsAndIds(ArrayList<Integer> v, int startIndex, int numWeights) {
		
		int semCount = semanticWeightOffset.entrySet().size();
		
		// We want a maximum of 3 weights per vertex.
		int maxWeights = 3;
		
		if(numWeights <= maxWeights) {
			for(int i = 0; i < numWeights; i++) {
				weights.add(mixedWeights.get(v.get(startIndex + (i * semCount) + semanticWeightOffset.get("WEIGHT"))));
				jointIds.add(v.get(startIndex + (i * semCount) + semanticWeightOffset.get("JOINT")));
			}
			for(int i = 0; i < maxWeights - numWeights; i++) {
				weights.add(0.0f);
				jointIds.add(0);
			}
		}else {
			float sum = 0.0f;
			float[] fWeights = new float[maxWeights];
			int[] joints = new int[maxWeights];
			for(int i = 0; i < maxWeights; i++) {
				fWeights[i] = mixedWeights.get(v.get(startIndex + (i * semCount) + semanticWeightOffset.get("WEIGHT")));
				sum += fWeights[i];
				joints[i] = v.get(startIndex + (i * semCount) + semanticWeightOffset.get("JOINT"));
			}
			for(int i = 0; i < maxWeights; i++) {
				fWeights[i] /= sum;
				weights.add(fWeights[i]);
				jointIds.add(joints[i]);
			}

		}
	}
	
	/**
	 * Processes a single vertex based on the largest data list.
	 * @param largest The list which is originally the most populated (0 = vertices, 1 = normals, 2 = texture coords).
	 * @param indices The array of indices for the vertex being processed.
	 * @return [void]
	 */
	private void processVertex(int largest, int[] indices) {
		
		// Split the indices up.
		int vertex = indices[semanticFaceOffset.get("VERTEX")];
		int normal = indices[semanticFaceOffset.get("NORMAL")];
		int texCoord = indices[semanticFaceOffset.get("TEXCOORD")];
		
		if(largest == 0) {
			
			// Vertex index stays the same.
			vertexList[(vertex * 3)] = vertices.get(vertex).x;
			vertexList[(vertex * 3) + 1] = vertices.get(vertex).y;
			vertexList[(vertex * 3) + 2] = vertices.get(vertex).z;
			
			// Weight index stays the same.
			weightList[(vertex * 3)] = weights.get(vertex * 3);
			weightList[(vertex * 3) + 1] = weights.get((vertex * 3) + 1);
			weightList[(vertex * 3) + 2] = weights.get((vertex * 3) + 2);
			
			// Joint index stays the same.
			jointList[(vertex * 3)] = jointIds.get(vertex * 3);
			jointList[(vertex * 3) + 1] = jointIds.get((vertex * 3) + 1);
			jointList[(vertex * 3) + 2] = jointIds.get((vertex * 3) + 2);
			
			// Normal index is now vertex index so we copy it to the right place in the array.
			normalList[(vertex * 3)] = normals.get(normal).x;
			normalList[(vertex * 3) + 1] = normals.get(normal).y;
			normalList[(vertex * 3) + 2] = normals.get(normal).z;
			
			// UV index is now vertex index so we copy it to the right place in the array.
			texList[(vertex * 2)] = texCoords.get(texCoord).x;
			texList[(vertex * 2) + 1] = texCoords.get(texCoord).y;
			
			// Copy the index.
			indexList[vertex] = vertex;
			
		}else if(largest == 1) {
			
			// Vertex index is now normal index so we copy it to the right place in the array.
			vertexList[(normal * 3)] = vertices.get(vertex).x;
			vertexList[(normal * 3) + 1] = vertices.get(vertex).y;
			vertexList[(normal * 3) + 2] = vertices.get(vertex).z;
			
			// Weight index is now normal index so we copy it to the right place in the array.
			weightList[(normal * 3)] = weights.get(vertex * 3);
			weightList[(normal * 3) + 1] = weights.get((vertex * 3) + 1);
			weightList[(normal * 3) + 2] = weights.get((vertex * 3) + 2);
			
			// Joint index is now normal index so we copy it to the right place in the array.
			jointList[(normal * 3)] = jointIds.get(vertex * 3);
			jointList[(normal * 3) + 1] = jointIds.get((vertex * 3) + 1);
			jointList[(normal * 3) + 2] = jointIds.get((vertex * 3) + 2);
			
			// Normal index stays the same.
			normalList[(normal * 3)] = normals.get(normal).x;
			normalList[(normal * 3) + 1] = normals.get(normal).y;
			normalList[(normal * 3) + 2] = normals.get(normal).z;
			
			// UV index is now normal index so we copy it to the right place in the array.
			texList[(normal * 2)] = texCoords.get(texCoord).x;
			texList[(normal * 2) + 1] = texCoords.get(texCoord).y;
			
			// Copy the index.
			indexList[normal] = normal;
			
		}else if(largest == 2) {
			
			// Vertex index is now uv index so we copy it to the right place in the array.
			vertexList[(texCoord * 3)] = vertices.get(vertex).x;
			vertexList[(texCoord * 3) + 1] = vertices.get(vertex).y;
			vertexList[(texCoord * 3) + 2] = vertices.get(vertex).z;
			
			// Weight index is now uv index so we copy it to the right place in the array.
			weightList[(texCoord * 3)] = weights.get(vertex * 3);
			weightList[(texCoord * 3) + 1] = weights.get((vertex * 3) + 1);
			weightList[(texCoord * 3) + 2] = weights.get((vertex * 3) + 2);
			
			// Joint index is now uv index so we copy it to the right place in the array.
			jointList[(texCoord * 3)] = jointIds.get(vertex * 3);
			jointList[(texCoord * 3) + 1] = jointIds.get((vertex * 3) + 1);
			jointList[(texCoord * 3) + 2] = jointIds.get((vertex * 3) + 2);
			
			// Normal index is now uv index so we copy it to the right place in the array.
			normalList[(texCoord * 3)] = normals.get(normal).x;
			normalList[(texCoord * 3) + 1] = normals.get(normal).y;
			normalList[(texCoord * 3) + 2] = normals.get(normal).z;
			
			// UV index stays the same.
			texList[(texCoord * 2)] = texCoords.get(texCoord).x;
			texList[(texCoord * 2) + 1] = texCoords.get(texCoord).y;
			
			// Copy the index.
			indexList[texCoord] = texCoord;
			
		}
	}
	
	/**
	 * Returns the ID of the geometry.
	 * @return [String] The ID of the geometry as a string.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the name of the geometry.
	 * @return [String] The name of the geometry as a string.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the vertex list of the geometry.
	 * @return [float[]] The list of three-dimensional vertices that make up the mesh of the geometry.
	 */
	public float[] getVertices() {
		return vertexList;
	}

	/**
	 * Returns the normal list of the geometry.
	 * @return [float[]] The list of three-dimensional normals used in the geometry.
	 */
	public float[] getNormals() {
		return normalList;
	}
	
	/**
	 * Returns the texture coordinate list of the geometry.
	 * @return [float[]] The list of two-dimensional texture coordinates used in the geometry.
	 */
	public float[] getTexCoords() {
		return texList;
	}
	
	/**
	 * Returns the weight list of the geometry.
	 * @return [float[]] The list of the three-dimensional weights for each vertex used in the geometry.
	 */
	public float[] getWeights() {
		return weightList;
	}
	
	/**
	 * Returns the joint list of the geometry.
	 * @return [int[]] The list of the three-dimensional joint IDs for each vertex used in the geometry.
	 */
	public int[] getJointIds() {
		return jointList;
	}
	
	/**
	 * Returns the index list of the geometry.
	 * @return [int[]] The list of indices making up the different triangles of the geometry's mesh.
	 */
	public int[] getIndices() {
		return indexList;
	}
	
	/**
	 * Returns the skin corresponding to the geometry.
	 * @return [ColladaSkin] The corresponding skin for the model.
	 */
	public ColladaSkin getSkin() {
		return skin;
	}

	/**
	 * Returns the animation corresponding to the geometry.
	 * @return [Animation] The corresponding animation for the model.
	 */
	public Animation getAnimation() {
		return anim;
	}
	
}
