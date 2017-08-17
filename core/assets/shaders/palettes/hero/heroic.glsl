#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
		color.r = color.b;
		if (color.b > 0.9) color.b = 0.2;
		if (color.g > 0.9) color.g = 0.12;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}