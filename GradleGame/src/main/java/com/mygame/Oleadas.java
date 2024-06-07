/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author PC NASUA
 */
public class Oleadas {
    

    
    private static ArrayList<Enemigo> listaEnemigos = new ArrayList<>();
    private static ArrayList<int[]> numeroDeEnemigos = new ArrayList<>();
    private static int miniOleadaActual = 0;
    private static float tiempoAcumulado = 0;
    private static int tiempoEntreMiniOleadas;
    private static AssetManager assetManager;
    //private static Node nodoEnemigos;

    public static void iniciarOleada(int numeroMiniOleadas, int tiempoEntreMiniOleadas, ArrayList<int[]> numeroDeEnemigos, AssetManager assetManager, Node nodoEnemigos) {
        Oleadas.numeroDeEnemigos = numeroDeEnemigos;
        Oleadas.miniOleadaActual = 0;
        Oleadas.tiempoEntreMiniOleadas = tiempoEntreMiniOleadas;
        Oleadas.assetManager = assetManager;
        //Oleadas.nodoEnemigos = nodoEnemigos;
        Oleadas.listaEnemigos.clear();
        //System.out.println("Aqui estoyyyy");
        if (numeroMiniOleadas == numeroDeEnemigos.size()) {
            // Iniciar el temporizador para la primera mini oleada
            Oleadas.tiempoAcumulado = 0;
        } else {
            System.out.println("El n�mero de mini oleadas no coincide con el tama�o de la lista de n�mero de enemigos");
        }
    }

    public static void actualizar(float tpf,Node nodoEnemigos,Vector3f posicionJugador) {
        //System.out.println(miniOleadaActual < numeroDeEnemigos.size());
        if (miniOleadaActual < numeroDeEnemigos.size()) {
            tiempoAcumulado += tpf;
            //System.out.println( tiempoAcumulado);
            if (tiempoAcumulado >= tiempoEntreMiniOleadas) {
                int[] enemigos = numeroDeEnemigos.get(miniOleadaActual);
                if (enemigos.length != 3) {
                    System.out.println("Configuraci�n de enemigos inv�lida para la mini oleada " + miniOleadaActual);
                   
                }
                if ((enemigos[0] < 0 || enemigos[0] > 12) || (enemigos[1] < 0 || enemigos[1] > 12) || (enemigos[2] < 0 || enemigos[2] > 12)) {
                    System.out.println("Valores de enemigos fuera de rango para la mini oleada " + miniOleadaActual);
                    
                } 
                System.out.println(enemigos[0]);
                listaEnemigos.addAll(miniOleada(enemigos[0], enemigos[1], enemigos[2], assetManager, nodoEnemigos,posicionJugador));
                
                System.out.println(enemigos[0]);

                miniOleadaActual++;
                tiempoAcumulado = 0; // Reiniciar el temporizador para la siguiente mini oleada
            }
        }
            for(Enemigo enemigosOleada: listaEnemigos){
                    //System.out.println("Holaaaaaaaaaa34535342");
                    if(enemigosOleada instanceof EnemigoSeguidor){
                       
                        ((EnemigoSeguidor) (enemigosOleada)).actualizarPosicionJugador(posicionJugador);
                        TestMapa.setVidaJugador(TestMapa.getVidaJugador() - enemigosOleada.acciones(tpf));
                        
                    }else{
                        TestMapa.setVidaTorre(TestMapa.getVidaTorre()-enemigosOleada.acciones(tpf));
                    }
                    
                }
    }

    public static ArrayList<Enemigo> getListaEnemigos() {
        return listaEnemigos;
    }

    private static ArrayList<Enemigo> miniOleada(int numEnemigosTipo1, int numEnemigosTipo2, int numEnemigosTipo3, AssetManager assetManager, Node nodoEnemigos,Vector3f posicionJugador) {
        ArrayList<Enemigo> enemigos = new ArrayList<>();
        // Implementar la l�gica para crear enemigos aqu�
        //int cantidadEnemigosNorma = 9;
        //int cantidadMiniomMaximos = 9;
        //int contadadEnemigosSeguidoresw = 9;
        //ArrayList<Enemigo> enemigosNormales = new ArrayList<Enemigo>();
        for(int i = 0;  i < numEnemigosTipo1; i ++){
           
            EnemigoBase enemigo = new EnemigoBase(assetManager,Constantes.POSICIONES_ENEMIGOS_NORMALES[i],Constantes.OBJETIVOS_ENEMIGOS_NORMALES[i]);
            nodoEnemigos.attachChild(enemigo.getEnemigo());
            enemigos.add(enemigo);
            if(i >= 3 && i < 6){
                enemigo.getEnemigo().rotate(0, FastMath.HALF_PI, 0);
            }
            if(i >= 6 && i < 9){
                
                enemigo.getEnemigo().rotate(0, FastMath.PI, 0);
            }
            if(i >= 9 && i < 12){
                enemigo.getEnemigo().rotate(0, -FastMath.HALF_PI, 0);
            }
            
        }
        for(int i = 0;  i < numEnemigosTipo2; i ++){
          
            Miniom enemigo = new Miniom(assetManager,Constantes.POSICIONES_MINIOMS[i],Constantes.OBJETIVOS_MINIOMS[i]);
            nodoEnemigos.attachChild(enemigo.getEnemigo());
            enemigos.add(enemigo);
            if(i >= 3 && i < 6){
                enemigo.getEnemigo().rotate(0, FastMath.HALF_PI, 0);
            }
            if(i >= 6 && i < 9){
                
                enemigo.getEnemigo().rotate(0, FastMath.PI, 0);
            }
            if(i >= 9 && i < 12){
                enemigo.getEnemigo().rotate(0, -FastMath.HALF_PI, 0);
            }
            
        }
        
        for(int i = 0;  i < numEnemigosTipo3; i ++){
          
            EnemigoSeguidor enemigo = new EnemigoSeguidor(assetManager,Constantes.POSICIONES_ENEMIGOS_NORMALES[i],posicionJugador);
            nodoEnemigos.attachChild(enemigo.getEnemigo());
            enemigos.add(enemigo);
            if(i >= 3 && i < 6){
                enemigo.getEnemigo().rotate(0, FastMath.HALF_PI, 0);
            }
            if(i >= 6 && i < 9){
                
                enemigo.getEnemigo().rotate(0, FastMath.PI, 0);
            }
            if(i >= 9 && i < 12){
                enemigo.getEnemigo().rotate(0, -FastMath.HALF_PI, 0);
            }
            
        }
        //return enemigosNormales;
        return enemigos;
    }
    
}
