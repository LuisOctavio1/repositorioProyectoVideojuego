/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.math.FastMath;

/**
 *
 * @author PC NASUA
 */
public class EnemigoBase implements Enemigo{
    int vida;
    private Node enemigo;

    
    private AnimChannel channel;
    private AnimControl control;
    float velocidad;
    float tiempoDesdeUltimoAtaque;
    int da�o;
    Vector3f posicionInicial;
    Vector3f posicionFinal;
    
    
    public EnemigoBase(AssetManager assetManager,Vector3f posicionInicial,Vector3f posicionFinal){
        vida = 150;
        velocidad = (float) 4;
         DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(20, 20, 20).normalizeLocal());
        enemigo = (Node) assetManager.loadModel("Models/Oto/OtoOldAnim.j3o");
        enemigo.setLocalScale(.75f);
        enemigo.setLocalTranslation(posicionInicial);
        this.posicionFinal = posicionFinal;
        control = enemigo.getControl(AnimControl.class);
        channel = control.createChannel();
        da�o = 20;
        enemigo.setUserData("tipo", "enemigo");
        enemigo.setUserData("vida", 150); 
    }

    @Override
    public void avanzar(Vector3f posicionObjetivo, float tpf) {
        Vector3f direccion = posicionObjetivo.subtract(enemigo.getLocalTranslation()).normalize();
        Vector3f nuevaPosicion = enemigo.getLocalTranslation().add(direccion.mult(velocidad * tpf));
        enemigo.setLocalTranslation(nuevaPosicion);
        if (!"Walk".equals(channel.getAnimationName())) {
            
            channel.setAnim("Walk");
            channel.setLoopMode(LoopMode.Loop);
            channel.setSpeed(1f);
        }
        
    }

    @Override
    public int atacar() {
        if (!"push".equals(channel.getAnimationName())) {
            channel.setAnim("push");
            channel.setLoopMode(LoopMode.Loop);
            channel.setSpeed(1f);
        }
        return da�o;
    }

    @Override
    public void detenerse() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void cambiarObjetivo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public Node getEnemigo() {
        return enemigo;
    }
    
    @Override
    public int acciones(float tpf){
        tiempoDesdeUltimoAtaque += tpf;
    if (enemigo != null && enemigo.getLocalTranslation().distance(posicionFinal) > 1.0f) {
            avanzar(posicionFinal, tpf);   
            return 0;
        }else{
            if (tiempoDesdeUltimoAtaque >= 5.0f) { // Comprueba si ha pasado un segundo desde el �ltimo ataque
                Integer vidaActual = enemigo.getUserData("vida");
            if( vidaActual>= 1){
            atacar();
            tiempoDesdeUltimoAtaque = 0f; // Reinicia el temporizador despu�s de atacar
            return da�o;
            }
            tiempoDesdeUltimoAtaque = 0f; // Reinicia el temporizador despu�s de atacar
            return 0;
        }
        }
    return 0;
    }
    @Override
    public void setVida(int vida){
        this.vida = vida;
    }
    @Override
    public int getVida(){
        return vida;
    }
    
    
    
}
