package br.pucpr.cg;

import br.pucpr.mage.*;
import br.pucpr.mage.phong.DirectionalLight;
import br.pucpr.mage.phong.Material;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.File;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_SUBTRACT;
import static org.lwjgl.opengl.GL11.*;


public class Terreno implements Scene {

    public static void main(String[] args) {
        new Window(new Terreno(), "PerlinNoiseTDE", 800, 600).show();
    }

    private Camera camera = new Camera();
    private Keyboard keys = Keyboard.getInstance();

    private NoiseGenerator perlin = new NoiseGenerator();
    private static final String PATH = "bricks_t.jpg";

    private boolean polygon = true;
    float Escala =1;


    private DirectionalLight light = new DirectionalLight(
            new Vector3f( 1.0f, -1.0f, -1.0f), //direction
            new Vector3f( 0.5f,  0.5f,  0.5f), //ambient
            new Vector3f( 1.0f,  1.0f,  0.8f), //diffuse
            new Vector3f( 1.0f,  1.0f,  1.0f));//specular

    private Mesh mesh;
    Material material = new Material(
            new Vector3f(0.5f, 0.5f, 0.5f), //ambient
            new Vector3f(0.5f, 0.5f, 0.5f), //diffuse
            new Vector3f(0.5f, 0.5f, 0.5f), //specular
            100.0f);                    //specular power

    private float angleX = 0.0f;
    private float angleY = 0.5f;

    @Override
    public void init() {
        perlin.run();
        material.setTexture("uTexture", new Texture(PATH));

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        try {
            mesh = MeshFactory.loadTerrain(new File("perlin1.png"), 0.5f,3);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        camera.getPosition().y = 200.0f;
        camera.getPosition().z = 200.0f;
    }

    @Override
    public void update(float secs) {

        if (keys.isDown(GLFW_KEY_A)) {
            angleY += secs;
            camera.rotate(0, (float)Math.toRadians(60)*secs);
        }
        if (keys.isDown(GLFW_KEY_D)) {
            angleY -= secs;
            camera.rotate(0, (float)Math.toRadians(-60)*secs);
        }
        if (keys.isDown(GLFW_KEY_W)) {
            if(angleX< 0.15f){
                angleX += secs;
                camera.rotate((float)Math.toRadians(60)*secs, 0);
            }
        }
        if (keys.isDown(GLFW_KEY_S)) {
            if(angleX > -1.3f){
                angleX -= secs;
                camera.rotate((float)Math.toRadians(-60)*secs, 0);
            }
        }
        if (keys.isDown(GLFW_KEY_SPACE)) {
            angleY = 0;
            angleX = 0;
        }
        if (keys.isDown(GLFW_KEY_UP)) {
            camera.moveFront(60 * secs* 3);
        }
        if (keys.isDown(GLFW_KEY_DOWN)) {
            camera.moveFront(-60 * secs * 3);
        }
        if (keys.isDown(GLFW_KEY_LEFT)) {
            camera.strafeLeft(60 * secs * 3);
        }
        if (keys.isDown(GLFW_KEY_RIGHT)) {
            camera.strafeLeft(-60 * secs * 3);
        }
        if(keys.isDown(GLFW_KEY_KP_ADD)){
            if(Escala<2.95f) Escala += 0.1f;
        }
        if(keys.isDown((GLFW_KEY_KP_SUBTRACT))){
            if(Escala>-0.5) Escala -= 0.1f;
        }
        if(keys.isPressed((GLFW_KEY_P))){
            if(polygon == true){
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                polygon = false;
            }else{
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                polygon = true;
            }
        }

    }

    @Override
    public void draw() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Shader shader = mesh.getShader();
        shader.bind()
                .setUniform("uProjection", camera.getProjectionMatrix())
                .setUniform("uView", camera.getViewMatrix())
                .setUniform("uCameraPosition", camera.getPosition());
        light.apply(shader);
        material.apply(shader);
        shader.unbind();

        mesh.setUniform("uWorld", new Matrix4f());
        mesh.setUniform("uEscala", Escala);
        mesh.draw();
    }
    @Override
    public void deinit() {
    }
}