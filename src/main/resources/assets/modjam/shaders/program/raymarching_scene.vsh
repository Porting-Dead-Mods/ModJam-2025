#version 150


out vec2 texCoord;

out vec3 rayDirection;
out vec3 rayOrigin;

uniform float Near;
uniform float Far;
uniform vec2 OutSize;
uniform vec2 InSize;

uniform mat4 SceneInvProjMat;

const vec2 POSITIONS[4] =
vec2[4](
vec2(1, -1),
vec2(1, 1),
vec2(-1, 1),
vec2(-1, -1)
);

const vec2 UVS[4] =
vec2[4](
vec2(1, 0),
vec2(1, 1),
vec2(0, 1),
vec2(0, 0)
);


void main(){
    vec2 Position = POSITIONS[gl_VertexID].xy;
    gl_Position = vec4(Position, 0.2, 1.0);
    rayOrigin = (SceneInvProjMat * vec4(Position.xy, -1.0, 1.0) * Near).xyz;
    rayDirection = (SceneInvProjMat * vec4(Position.xy * (Far - Near), Far + Near, Far - Near)).xyz;

    texCoord = UVS[gl_VertexID].xy;
}
