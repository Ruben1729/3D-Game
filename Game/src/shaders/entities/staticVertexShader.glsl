#version 400 core

const int MAX_LIGHTS = 1;

in vec3 pos;
in vec3 normal;
in vec2 uv;

out vec3 pass_Normal;
out vec2 pass_UV;
out vec3 toCameraVector;
out vec3 toLightPositions[MAX_LIGHTS];

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform vec3 lightPositions[MAX_LIGHTS];

void main(void){
	
	vec4 worldPosition = modelMatrix * vec4(pos, 1);

	pass_Normal = (modelMatrix * vec4(normal, 0.0)).xyz;
	pass_UV = uv;

	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

	for(int i = 0; i < MAX_LIGHTS; i++)
		toLightPositions[i] = lightPositions[i] - worldPosition.xyz;

	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	
}
