/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author PC NASUA
 */
public interface Arma {
    void disparar(float tpf,Camera cam,Node enemigosNode,float danioExtra);
    
    void recargar(float tpf);
    
    void acciones(float tpf,Camera cam,Node enemigosNode,float danioExtra);
    
    Spatial getArma();
    
    int getBalas();
}
