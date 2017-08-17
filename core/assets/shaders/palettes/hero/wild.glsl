#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
		vec3 oldColor = color;
		color.r = oldColor.r * 1.5;
		color.b = (oldColor.b + oldColor.r) / 3;
		color.g = (oldColor.g + oldColor.r) / 1.5;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}