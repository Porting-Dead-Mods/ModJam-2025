#version 150
//https://iquilezles.org/
uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

in vec2 texCoord;

uniform float Near;
uniform float Far;

out vec4 fragColor;

const int MAX_STEPS = 100;

uniform float[] ThingPosition;

const float STEP_DISTANCE = 1;

const float EPSILON = 0.01;

uniform float GameTime;

in vec3 rayDirection;
in vec3 rayOrigin;

uniform mat4 InvViewMat;

float sdBox(vec3 p, vec3 b)
{
    vec3 q = abs(p) - b;
    return length(max(q, 0.0)) + min(max(q.x, max(q.y, q.z)), 0.0);
}

float sdSphere( vec3 p, float s )
{
    return length(p)-s;
}

float sdRoundedCylinder(vec3 p, float ra, float rb, float h)
{
    vec2 d = vec2(length(p.xz)-2.0*ra+rb, abs(p.y) - h);
    return min(max(d.x, d.y), 0.0) + length(max(d, 0.0)) - rb;
}

float smin(float a, float b, float k)
{
    k *= log(2.0);
    float x = b-a;
    return a + x/(1.0-exp2(x/k));
}

mat4 translate(mat4 mat, vec3 translation)
{
    mat[3].xyz += translation;
    return mat;
}

vec3 transformRay(mat4 mat, vec3 ray)
{
    return (mat * vec4(ray,1)).xyz;
}

float scene(vec3 p)
{
    vec3 firstThing = vec3(ThingPosition[0], ThingPosition[1], ThingPosition[2]);
    vec3 secondThing = vec3(ThingPosition[3], ThingPosition[4], ThingPosition[5]);

    //wasteful
    mat4 boxMat = translate(InvViewMat, firstThing);
    mat4 sphereMax = translate(InvViewMat, secondThing);

    return smin(sdBox(transformRay(boxMat, p).xyz, vec3(10)), sdSphere(transformRay(sphereMax, p), 5), 0.2);
}

float rayDepth(float z)
{

    float a = (Far+Near)/(Far-Near);
    float b = 2.0*Far*Near/(Far-Near);
    return ((a + b/z) + 1) / 2;
}

vec3 start;

float linearizeDepth(float depth)
{
    return Near * Far / (Far + depth * (Near - Far));
}

void main(){
    vec4 diffuseColor = texture(DiffuseSampler, texCoord);

    start = rayOrigin;

    vec3 ray = vec3(rayOrigin);

    vec3 rd = normalize(rayDirection);

    vec3 outColor = diffuseColor.rgb;

    float depth = texture(DepthSampler, texCoord).r;

    float dist;

    for (int i = 0; i < MAX_STEPS; i++)
    {
        //  ray += normalize(rayDirection) * STEP_DISTANCE;
        float sdf = scene(ray);

        if (rayDepth(ray.z) > depth)
        {
            break;
        }
        if (sdf < EPSILON)
        {
            outColor = vec3(0.3, 0.1, 0.8);
            break;
        }

        ray += sdf * rd;

    }

    fragColor = vec4(outColor.rgb, 1.0);
}
