#version 330

uniform mat4 uProjection;
uniform mat4 uView;
uniform mat4 uWorld;

uniform vec3 uCameraPosition;

uniform float uEscala;

in vec3 aPosition;
in vec3 aNormal;
in vec2 aTexCoord;

out vec3 vNormal;
out vec3 vViewPath;
out vec2 vTexCoord;

out float vertDist;


void main() {
    vNormal = (uWorld * vec4(aNormal, 0.0)).xyz;

    vec4 worldPos = uWorld * vec4(aPosition.x, aPosition.y * uEscala, aPosition.z, 1.0f);
    gl_Position =  uProjection * uView * worldPos;

    vNormal = (uWorld * vec4(aNormal, 0.0)).xyz;
    vViewPath = uCameraPosition - worldPos.xyz;

    vTexCoord = aTexCoord;

    vertDist = distance(uCameraPosition, worldPos.xyz);
}