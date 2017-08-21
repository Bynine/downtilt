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
        vec3 oldColor = color;
        float grey = (color.r + color.g + color.b)/3.0;
        if (color.b < 0.5) color.r = oldColor.b;
        if (oldColor.r < 0.75 || oldColor.g < 0.75) color.b = oldColor.r;
        color.g = (color.g + grey)/2.0;
        if (color.g > 0.3 && color.b < 0.6) color.g = color.g/3.0;
        gl_FragColor = vec4(color.r * red, color.g * gb, color.b * gb, texture2D(u_texture, v_texCoords).a * alpha);
}