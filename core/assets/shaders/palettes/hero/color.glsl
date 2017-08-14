#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
		if (color.g > 0) color.g = color.g + 0.5;
		if (color.b > 0) color.b = color.b + 0.5;
		if (color.r > 0) color.r = color.r + 0.5;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}