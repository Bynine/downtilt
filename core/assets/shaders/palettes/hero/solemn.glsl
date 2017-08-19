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
        if (color.b < 0.5) color.r = oldColor.b;
        color.b = oldColor.r;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}