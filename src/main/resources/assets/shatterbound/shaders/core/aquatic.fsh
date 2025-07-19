#version 150

uniform sampler2D Sampler0;
uniform float Intensity;
uniform vec3 SkyColor;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor;

    // Mix between original color and dark blue based on intensity
    vec3 darkBlue = vec3(0.05, 0.15, 0.3); // Dark blue color
    fragColor = vec4(mix(color.rgb, SkyColor, Intensity), color.a);
}