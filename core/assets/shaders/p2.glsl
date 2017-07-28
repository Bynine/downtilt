#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

vec3 setColor(vec3 colorVec, float r, float g, float b){
	colorVec.r = (colorVec.r + r/130.0)/3.0;
	colorVec.g = (colorVec.g + g/130.0)/3.0;
	colorVec.b = (colorVec.b + b/130.0)/3.0;
	return colorVec;
}

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        float oldG = color.g;
        color.g = color.b;
        color.b = oldG;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}