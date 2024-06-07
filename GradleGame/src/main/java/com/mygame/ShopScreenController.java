/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

/**
 *
 * @author PC NASUA
 */
import com.bulletphysics.collision.dispatch.UnionFind.Element;
import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.InputManager;
import com.jme3.input.controls.InputListener;
import com.jme3.input.event.InputEvent;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.input.NiftyInputEvent;

public class ShopScreenController implements ScreenController {
    
    private Nifty nifty;


    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
    }

   

    @Override
    public void onStartScreen() {
        System.out.println("Abierta");
    }

    @Override
    public void onEndScreen() {
        System.out.println("Cerrada");
    }

    
    
    public static  void buyWeapon1() {
        System.out.println("Comprar Arma 1");
        // Lógica para comprar Arma 1
    }

    public void buyWeapon2() {
        System.out.println("Comprar Arma 2");
        // Lógica para comprar Arma 2
    }

    public void buyUpgrade1() {
        System.out.println("Comprar Mejora 1");
        // Lógica para comprar Mejora 1
    }

    public void buyUpgrade2() {
        System.out.println("Comprar Mejora 2");
        // Lógica para comprar Mejora 2
    }

    public void showShop() {
        nifty.gotoScreen("shopScreen");
        //System.out.println("Holaaaaaaaaaaaaaaaaaaa");
    }
   



}
