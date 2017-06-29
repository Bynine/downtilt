#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
		vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
		float grey = (color.g + color.b + color.r) / 3;
		float value = 150.0/255.0;
        color.g = color.g + grey + value;
        color.b = color.b + grey + value;
        color.r = color.r + grey + value;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}