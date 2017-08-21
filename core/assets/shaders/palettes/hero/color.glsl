#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;
uniform float alpha = 1;
uniform float red = 1;
uniform float gb = 1;

float setColor(float x){
	float divide = 0.2;
	if (x < divide) {
		x = x / 2.0;
	}
	if (x > divide) {
		x = x * 2.0;
	}
	if (x > divide) {
		x = x / 2.0;
	}
    return x;
}

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        float max = color.r + color.g + color.b;
        color.r = setColor(color.r + color.g) * 0.8;
		color.g = setColor(color.g) * 1.1;
		color.b = setColor(color.b) * 0.6;
		if (max > 2.0 && max < 2.8) {
			color.r = color.r - 0.5;
			color.g = color.g - 0.5;
		}
        gl_FragColor = vec4(color.r * red, color.g * gb, color.b * gb, texture2D(u_texture, v_texCoords).a * alpha);
}