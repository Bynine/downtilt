#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

float newColor(float col, float grey){
    return ( (col + grey * 3) / 9.0 ) - 40.0/255.0;
}

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        float grey = (color.r + color.g + color.b)/3.0;
		color.r = newColor(color.r, grey);
		color.g = newColor(color.g, grey);
		color.b = newColor(color.b, grey) + 20.0/255.0;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}