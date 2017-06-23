package br.pucpr.cg;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;

import java.nio.IntBuffer;

import javafx.scene.transform.Rotate;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import javax.swing.text.Position;

public class Camera {
    private Vector3f direction = new Vector3f(0,0,-1);
    private Vector3f position = new Vector3f(0,0,2);
    private Vector3f up = new Vector3f(0, 1, 0);
    private Vector3f target = new Vector3f(0,0,0);

    private float fov = (float)Math.toRadians(60);
    private float near = 0.1f;
    private float far = 1000.0f;
    
    public Vector3f getPosition() {
        return position;
    }
    public Vector3f getUp() {
        return up;
    }
    public Vector3f getTarget() {
        return target;
    }

    public float getFov() {
        return fov;
    }
    public void setFov(float fov) {
        this.fov = fov;
    }
    public float getNear() {
        return near;
    }
    public void setNear(float near) {
        this.near = near;
    }
    public float getFar() {
        return far;
    }
    public void setFar(float far) {
        this.far = far;
    }
    
    private float getAspect() {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        long window = glfwGetCurrentContext();
        glfwGetWindowSize(window, w, h);        
        return w.get() / (float)h.get();
    }
    
    public Matrix4f getViewMatrix() {
        position.add(direction,target);
        return new Matrix4f().lookAt(position, target, up);
    }
    
    public Matrix4f getProjectionMatrix() {
        return new Matrix4f().perspective(fov, getAspect(), near, far);
    }

    public Camera moveFront(float distancia){
        Vector3f aux = new Vector3f();
        direction.mul(distancia, aux);
        position.add(aux);
        return this;
    }

    public Camera strafeLeft(float distancia){
        Vector3f aux = new Vector3f();
        up.cross(direction, aux);
        aux.mul(distancia);
        position.add(aux);
        return this;
    }

    public Camera strafeRight(float distancia){
        Vector3f aux = new Vector3f();
        up.cross(direction, aux);
        if(distancia < 0){
            aux.mul(distancia);
            position.add(aux);
            return this;
        }
        else{
            aux.mul(-distancia);
            position.add(aux);
            return this;
        }

    }
    public Camera rotate(float angleX, float angleY){
        direction.mul(new Matrix3f().rotateY(angleY).rotateX(angleX));
        return this;
    }

}
