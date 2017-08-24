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

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        float grey = (color.r + color.g + color.b)/3.0;
        float pow = 2.6;
        float add = 90.0/255.0;
		color.r = pow(grey + add, pow);
		color.g = pow(grey + add, pow);
		color.b = pow(grey, 1);
        gl_FragColor = vec4(color.r * red, color.g * gb, color.b * gb, texture2D(u_texture, v_texCoords).a * alpha);
}