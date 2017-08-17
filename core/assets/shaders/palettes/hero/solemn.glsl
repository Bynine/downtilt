#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        float grey = (color.g + color.b + color.r)/4.0;
		color.g = (color.g + grey) / 3;
		color.b = (color.b + grey) / 2;
		color.r = (color.g + grey) / 2;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}