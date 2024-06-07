/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author PC NASUA
 */
public class Tienda {
    private static int nivelDanio = 0;
    private static float aumentoDanio = 1f;
    public static  void buyWeapon1() {
        System.out.println("comprado");
        // Lógica para comprar Arma 1
    }
    
    public static float incrementoDanio(){
        if(nivelDanio <5){
            nivelDanio++;
            return aumentoDanio+=.20f;
        }else{
            return aumentoDanio;
        }
    }
    
    public static void incrementoVidaTorre(){
        TestMapa.setVidaTorre(TestMapa.getVidaTorre()+250);
    }
    
    public static Arma comprarRifleSemiAutomatico(AssetManager assetManager,Vector3f posicionJugador, Node armas){
        return new RifleSemiAutomatico(assetManager,posicionJugador,armas);
    }
    
    
    public static Arma comprarSniper(AssetManager assetManager,Vector3f posicionJugador, Node armas){
        return new Sniper(assetManager,posicionJugador,armas);
    }
    
    
    
    
}
