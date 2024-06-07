/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author PC NASUA
 */
public class RifleSemiAutomatico implements Arma{
    
    int danio;
    int balas;
    int tiempoRecarga;
    private float tiempoUltimoDisparo;
    private float tiempoUltimaRecarga;
    float tiempoTranscurrido;
    float tiempoEntreDisparo;
    
    Spatial arma;
    
    public RifleSemiAutomatico(AssetManager assetManager,Vector3f posicionJugador, Node armas){
        arma = assetManager.loadModel("Models/AK47.j3o");
        arma.setLocalTranslation(posicionJugador);
        arma.rotate(0, FastMath.PI, 0);
        arma.scale(1.0f);
        armas.attachChild(arma);
        
        this.danio = 30;
        this.tiempoRecarga = 1;
        this.tiempoEntreDisparo = 0.1f;
        this.balas = 20;
        this.tiempoTranscurrido = 0f;
    }

    @Override
    public void disparar(float tpf,Camera cam,Node enemigosNode,float danioExtra) {
        
          if (balas > 0 && tiempoDesdeUltimoDisparo() >= 0.3f) {
            // Realizar el disparo
            balas--;
            tiempoUltimoDisparo = tiempoTranscurrido;
            // Obtener la dirección del vector de la cámara
     CollisionResults results = new CollisionResults();
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        enemigosNode.collideWith(ray, results);

        if (results.size() > 0) {
            // La primera geometría con la que colisionó el rayo
            Geometry hitGeom = results.getClosestCollision().getGeometry();
            Node hitNode = hitGeom.getParent();

            // Verificar si la geometría colisionada es un enemigo
            if ("enemigo".equals(hitNode.getUserData("tipo"))) {
                // Obtener y reducir la vida del enemigo
                Integer vida = hitNode.getUserData("vida");
                if (vida != null) {
                    vida -= Math.round(danio*danioExtra);
                    hitNode.setUserData("vida", vida);
                    System.out.println("¡Objetivo alcanzado! Vida restante del enemigo: " + vida);
                    if (vida <= 0) {
                        enemigosNode.detachChild(hitNode);
                        hitNode.removeFromParent();
                        hitNode.removeControl(AnimControl.class); // Opcional: remover cualquier control asociado al objeto
                        hitNode.removeControl(CharacterControl.class); // Opcional: remover cualquier control asociado al objeto
                        hitNode = null;
                        TestMapa.monedas +=5;
                    }
                }
            }
        }
            
        }else{
              //System.out.println("no disparando" + tpf);
            recargar(tpf);  
        }
    }

    @Override
    public void recargar(float tpf) {
        if (balas == 0 && !recargando() && tiempoDesdeUltimaRecarga() >= 7f) {
            // Iniciar la recarga
            tiempoUltimaRecarga = tiempoTranscurrido; // Actualizar el tiempo de la última recarga
            // Lógica de recarga (restaurar balas, etc.)
            balas = 20;
            System.out.println("recargando");
        }
    }



    @Override
    public void acciones(float tpf,Camera cam,Node enemigosNode,float danioExtra) {
        tiempoTranscurrido += tpf;
        disparar(tpf, cam,enemigosNode,danioExtra);
        //System.out.println(tpf + "aaaaaaaaaaaaaaaaaaaaaaa");
        
    }
    
    private float tiempoDesdeUltimaRecarga(){
        return tiempoTranscurrido - tiempoUltimaRecarga;
    }
    
    
    private float tiempoDesdeUltimoDisparo() {
        return tiempoTranscurrido - tiempoUltimoDisparo;
    }
    
   
    
    public Spatial getArma(){
        return arma;
    }
    
    private boolean recargando() {
        // Verificar si se está recargando
        System.out.println("Estoy recargando");
        return tiempoDesdeUltimaRecarga() < 1f;
    }
    
    @Override
    public int getBalas() {
        return balas;
    }
    
    
}
