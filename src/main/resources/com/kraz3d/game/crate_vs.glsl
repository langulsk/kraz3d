#version 330

uniform mat4 projection_matrix;
uniform mat4 view_matrix;
uniform mat4 model_matrix;

in vec4 vertex_position;
in vec4 vertex_color;

out vec4 pass_color;

void main() {
    gl_Position = vertex_position;
    pass_color = vertex_color;
}
