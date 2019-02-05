#version 400 core

const int MAX_LIGHTS = 1;
const float ambientLighting = 0.4;

in vec3 pass_Normal;
in vec2 pass_UV;
in vec3 toCameraVector;
in vec3 toLightPositions[MAX_LIGHTS];

out vec4 color;

uniform vec3 lightPositions[MAX_LIGHTS];
uniform vec3 lightColors[MAX_LIGHTS];

uniform sampler2D tex;

void main(void){

	vec3 unitNormal = normalize(pass_Normal);

	vec3 totalDiffuse = vec3(0, 0, 0);

	for(int i = 0; i < MAX_LIGHTS; i++){

		vec3 unitLight = normalize(toLightPositions[i]);

		float nDot = dot(unitLight, unitNormal);
		float brightness = max(ambientLighting, nDot);
		totalDiffuse += brightness * lightColors[i];

	}

	//color = vec4(1, 0, 0, 1) * vec4(totalDiffuse, 1);
	vec4 textureColor = texture(tex, pass_UV);
	color = textureColor * vec4(totalDiffuse, 1);

}
