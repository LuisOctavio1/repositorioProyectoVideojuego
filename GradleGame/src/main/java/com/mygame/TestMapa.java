/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.HttpZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import de.lessvoid.nifty.Nifty;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author PC NASUA
 */
public class TestMapa extends SimpleApplication
        implements ActionListener, AnimEventListener {

    private AnimChannel channel;
    private AnimControl control;
    EnemigoBase nuevoEnemigo;
    private Spatial arma;
    Miniom miniom;
    int disparos = 0;
    Node enemigo;
    EnemigoBase enemigoNuevo;
    private Node enemigosNode;
    int vida;
    Vector3f[] posicionesEnemigosNormales;
    int cantidadEnemigosNormales;
    private static ArrayList<Enemigo> enemigosTotales;
    ArrayList<int []>  enemigosOleadas;
    float danioExtra = 1.0f;
    static int monedas;

    private CharacterControl player;
    final private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;

    private final static Trigger TRIGGER_COLOR = new KeyTrigger(MouseInput.BUTTON_LEFT);
    
    private final static Trigger TRIGGER_ROTATE = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);

    //Ya que una accion puede ser activada por mas de un input, se puede agregar otro Trigger
    private final static Trigger TRIGGER_COLOR2 = new KeyTrigger(KeyInput.KEY_C);

    //definimos las constantes que nos ayudaran a identificar las acciones por los triggers
    // Recuerda: Una accion puede tener mas de un trigger que la activa
    private final static String MAPPING_COLOR = "Toggle Color";
    private final static String MAPPING_ROTATE = "Rotate";

    //Temporary vectors used on each frame.
    //They here to avoid instantiating new vectors on each frame
    final private Vector3f camDir = new Vector3f();
    final private Vector3f camLeft = new Vector3f();
    
    private float tiempoTranscurrido = 0f;
    private float tiempoParaSpawn = 10f;
    Enemigo [] enemigosNuevos;
    Arma pistola;
    Node nodoCamara;
    Node armaNode;
    Node armas;
    
    boolean disparando = false;
    
    private Nifty nifty;
    private boolean isShopVisible = false;
    private boolean derrotado = false;

    
    private static int vidaTorre;
    
    private BitmapText mensajeDerrota;
    
     private BitmapText textoVidaTorre;
    private BitmapText textoUsuarioVida;
     private BitmapText textoMonedas;
     private BitmapText textoBalas;
     
     private static int vidaJugador;
     
    private float tiempoEspera = 15.0f; 
    private float tiempoRestante = tiempoEspera;
     private int numeroOleada = 1;
     private Random random = new Random();

    public static int getVidaJugador() {
        return vidaJugador;
    }

    public static void setVidaJugador(int vidaJugador) {
        TestMapa.vidaJugador = vidaJugador;
    }
    
    public static int getVidaTorre() {
        return vidaTorre;
    }

    public static void setVidaTorre(int vidaTorre) {
        TestMapa.vidaTorre = vidaTorre;
    }

    public static void main(String[] args) {
        TestMapa app = new TestMapa();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        monedas = 1000;
        vidaTorre = 1000;
        vidaJugador = 100;
        NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
        assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/shopScreen.xml", "shopScreen", new ShopScreenController());

        // Agregar el display de Nifty GUI al ViewPort
        guiViewPort.addProcessor(niftyDisplay);

        // Desactivar el flyCam para usar Nifty GUI
        flyCam.setDragToRotate(true);
        
        
        
        
        nodoCamara = new Node("nodoCamara");
        rootNode.attachChild(nodoCamara);
        nodoCamara.setLocalTranslation(cam.getLocation());
        nodoCamara.lookAt(cam.getDirection(), Vector3f.UNIT_Y);
        armas = new Node("armas");
        pistola = new Pistola(this.assetManager,new Vector3f(-1.5f, -1.5f, 2.5f),armas);
        //rifle.getArma().rotate(0, FastMath.PI, 0);
        nodoCamara.attachChild(armas);
        armaNode = new Node("ArmaNode");
        armaNode.attachChild(armas);
        nodoCamara.attachChild(armaNode);
        
        /**
         * Set up Physics
         */
         enemigosNode = new Node("Enemigos");
         this.cantidadEnemigosNormales = 12;
         this.posicionesEnemigosNormales = new Vector3f[12];
         rootNode.attachChild(enemigosNode);
        //this.nuevoEnemigo = new EnemigoBase(this.assetManager,new Vector3f(41, 4, -115));
        //arma = assetManager.loadModel("Models/AK47.j3o");
        //arma.setLocalTranslation(new Vector3f(41, 4, -115));
        //rootNode.attachChild(arma);
        Oleadas oleada = new Oleadas();
        int[] numeroDeEnemigos = {7,0,0};
        int[] numeroDeEnemigos2 = {7,0,0};
        this.enemigosOleadas = new ArrayList<int []>();
        enemigosOleadas.add(numeroDeEnemigos);
        enemigosOleadas.add(numeroDeEnemigos2);
        //this.enemigosTotales = oleada.crearOleada(2, 15,enemigosOleadas , assetManager, enemigosNode);
        //this.miniom = new Miniom(this.assetManager,new Vector3f(130, 4, -24));
        //Oleadas oleada = new Oleadas(1);
       // this.enemigosNuevos = oleada.miniOleada(1,1,1, assetManager);
        //miniom.getEnemigo().rotate(0, FastMath.HALF_PI, 0);
        //enemigosNode.attachChild(nuevoEnemigo.getEnemigo());
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        attachCenterMark();
        vida = 200;

        // We re-use the flyby camera for rotation, while positioning is handled by physics
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(100);
        setUpKeys();
        setUpLight();

        // We load the scene from the zip file and adjust its size.
        assetManager.registerLocator(
                "https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/jmonkeyengine/town.zip",
                HttpZipLocator.class);
        Spatial sceneModel = assetManager.loadModel("main.scene");
        sceneModel.setLocalScale(2f);

        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
        CollisionShape sceneShape
                = CollisionShapeFactory.createMeshShape(sceneModel);
        RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);

        // We set up collision detection for the player by creating
        // a capsule collision shape and a CharacterControl.
        // The CharacterControl offers extra settings for
        // size, step height, jumping, falling, and gravity.
        // We also put the player in its starting position.
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(0, 10, 0));
        

        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.
        rootNode.attachChild(sceneModel);
        

        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);
        
        

// Configurar otros parámetros físicos según sea necesario

//rootNode.attachChild(nuevoEnemigo.getEnemigo());
//rootNode.attachChild(miniom.getEnemigo());
//rootNode.attachChild(enemigosNuevos[0].getEnemigo());
rootNode.attachChild(enemigosNode);
        
        
        //codigo del enemigo
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(20, 20, 20).normalizeLocal());
        rootNode.addLight(dl);
        //enemigo = (Node) assetManager.loadModel("Models/Oto/OtoOldAnim.j3o");
        //enemigo.setLocalScale(.75f);
        //enemigo.setLocalTranslation(0, 5, 0);
        //rootNode.attachChild(enemigo);
        //control = enemigo.getControl(AnimControl.class);
        //control.addListener(this);
        //channel = control.createChannel();
        //channel.setAnim("Walk");
        //channel = control.createChannel();
        //channel.setAnim("Walk");
        //channel.setLoopMode(LoopMode.Loop);
        //channel.setSpeed(1f);
        inputManager.addMapping(MAPPING_ROTATE, TRIGGER_ROTATE);
        inputManager.addListener(analogListener,
                new String[]{MAPPING_ROTATE});     
        
        inputManager.addMapping("ToggleShop", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addListener(actionListener, "ToggleShop");
        
        

        BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");

        
        mensajeDerrota = new BitmapText(font, false);
        mensajeDerrota.setSize(font.getCharSet().getRenderedSize() * 2);  
        mensajeDerrota.setColor(ColorRGBA.Red);  
        mensajeDerrota.setText("¡Has sido derrotado!");  
        mensajeDerrota.setLocalTranslation(
                settings.getWidth() / 2 - mensajeDerrota.getLineWidth() / 2,
                settings.getHeight() / 2 + mensajeDerrota.getLineHeight() / 2,
                0);
       
         
         textoVidaTorre = new BitmapText(font, false);
        textoVidaTorre.setSize(font.getCharSet().getRenderedSize());
        textoVidaTorre.setColor(ColorRGBA.Red);  // Color del texto
        textoVidaTorre.setLocalTranslation(10, settings.getHeight() - 10, 0);
        guiNode.attachChild(textoVidaTorre);
        
        // Crear y configurar el texto para la vida del usuario
        textoUsuarioVida = new BitmapText(font, false);
        textoUsuarioVida.setSize(font.getCharSet().getRenderedSize());
        textoUsuarioVida.setColor(ColorRGBA.Red);  // Color del texto
        textoUsuarioVida.setLocalTranslation(10, settings.getHeight() - 30, 0);
        guiNode.attachChild(textoUsuarioVida);
        
        //texto monedas
         textoMonedas = new BitmapText(font, false);
        textoMonedas.setSize(font.getCharSet().getRenderedSize());
        textoMonedas.setColor(ColorRGBA.Red); 
        textoMonedas.setLocalTranslation(10,settings.getHeight()-250, 0);
        guiNode.attachChild(textoMonedas);
        
        //texto balas
        textoBalas = new BitmapText(font, false);
        textoBalas.setSize(font.getCharSet().getRenderedSize());
        textoBalas.setColor(ColorRGBA.Red); 
        textoBalas.setLocalTranslation(10, settings.getHeight()-270, 0);
        guiNode.attachChild(textoBalas);
       
    }

    private void setUpLight() {
        // We add light so we see the scene
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
    }

    /**
     * We over-write some navigational key mappings here, so we can add
     * physics-controlled walking and jumping:
     */
    private void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
        inputManager.addMapping("disparo", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "disparo");
        inputManager.addMapping("comprarDanio", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addListener(this, "comprarDanio");
        inputManager.addMapping("comprarRifleSemi", new KeyTrigger(KeyInput.KEY_X));
        inputManager.addListener(this, "comprarRifleSemi");
        inputManager.addMapping("comprarSniper", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addListener(this, "comprarSniper");
        inputManager.addMapping("comprarVidaTorre", new KeyTrigger(KeyInput.KEY_V));
        inputManager.addListener(this, "comprarVidaTorre");
        inputManager.addMapping("salir", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addListener(this, "salir");
    }

    /**
     * These are our custom actions triggered by key presses. We do not walk
     * yet, we just keep track of the direction the user pressed.
     */
    @Override
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Left")) {
            if (value) {
                left = true;
            } else {
                left = false;
            }
        } else if (binding.equals("Right")) {
            if (value) {
                right = true;
            } else {
                right = false;
            }
        } else if (binding.equals("Up")) {
            if (value) {
                up = true;
            } else {
                up = false;
            }
        } else if (binding.equals("Down")) {
            if (value) {
                down = true;
            } else {
                down = false;
            }
        } else if (binding.equals("Jump")) {
            player.jump();
        }
        
         if (binding.equals("disparo")) {
        // Realizar alguna acción aquí al presionar el clic izquierdo
            if (value && !isShopVisible) {
            // Comienza a disparar cuando se presiona el botón
            disparando = true;
        } else {
            // Deja de disparar cuando se suelta el botón
            disparando = false;
        }
            
        }
         if (binding.equals("comprarDanio")){
             if (value && isShopVisible && monedas >=20) {
                 monedas -=20;
                 danioExtra =  Tienda.incrementoDanio();
                 System.out.println(danioExtra);
                 
             }
         }
         
         if (binding.equals("comprarRifleSemi") && monedas >=30){
             if (value && isShopVisible) {
                 monedas -=50;
                 armas.detachChild(pistola.getArma());
                 pistola = Tienda.comprarRifleSemiAutomatico(this.assetManager,new Vector3f(-1.5f, -1.5f, 2.5f),armas);
                 armas.attachChild(pistola.getArma());
                 
                 //System.out.println(danioExtra);
             }
         }
         if (binding.equals("comprarSniper") && monedas >=65){
             if (value && isShopVisible) {
                 monedas -= 80;
                 armas.detachChild(pistola.getArma());
                 pistola = Tienda.comprarSniper(this.assetManager,new Vector3f(-1.5f, -1.5f, 2.5f),armas);
                 armas.attachChild(pistola.getArma());
             }
         }
         
         if (binding.equals("comprarVidaTorre") && monedas >=100){
             if (value && isShopVisible) {
                 monedas -= 100;
                 Tienda.incrementoVidaTorre();
             }
         }
         
         
         if (binding.equals("salir") ){
             if (value && derrotado) {
                stop();
             }
         }
    }

    /**
     * This is the main event loop--walking happens here. We check in which
     * direction the player is walking by interpreting the camera direction
     * forward (camDir) and to the side (camLeft). The setWalkDirection()
     * command is what lets a physics-controlled player walk. We also make sure
     * here that the camera moves with player.
     */
    @Override
    public void simpleUpdate(float tpf) {
        //esto hace que funcione pero con duplicacos
        nodoCamara.setLocalTranslation(cam.getLocation());
        nodoCamara.setLocalRotation(cam.getRotation());
        //aqui termina
        //armaNode.setLocalTranslation(cam.getLocation().add(0, -0.5f, -1)); 
        //armaNode.setLocalRotation(cam.getRotation());
        //armaNode.setLocalTranslation(player.getPhysicsLocation().add(0, -0.5f, -1)); 
        //nodoCamara.setLocalTranslation(cam.getLocation());
        //nodoCamara.lookAt(cam.getDirection(), Vector3f.UNIT_Y);
        tiempoTranscurrido += tpf;
        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
        //onAnimCycleDone(control, channel, "Walk");
        //movimiento del enemigo
        Random random = new Random();
        Vector3f randomDirection = new Vector3f(random.nextFloat() * 2 - 1, 0, random.nextFloat() * 2 - 1).normalizeLocal();
        float velocidad = .2f; // Ajusta la velocidad según sea necesario
        Vector3f movimiento = randomDirection.mult(velocidad);


        
     
        crearOleada(tpf);
        
        Oleadas.actualizar(tpf,enemigosNode,cam.getLocation());
        if (disparando && pistola != null) {
            pistola.acciones(tpf,cam,rootNode,danioExtra);
        }
        
        if(vidaTorre <=0 || vidaJugador <=0){
            mostrarMensajeDerrota();
            derrotado = true;
        }else{
            guiNode.detachChild(mensajeDerrota);
        }
        
        
        textoVidaTorre.setText("Vida de la Torre: " + vidaTorre);
        textoUsuarioVida.setText("Vida del Usuario: " + vidaJugador);
        
        textoMonedas.setText("Monedas: " + monedas);
        textoBalas.setText("Balas: " + pistola.getBalas() + "\n Presiona E para abrir\n o cerrar la tienda");
    }
    
    


    private void attachCenterMark() {
        Geometry c = myBox("center mark",
                Vector3f.ZERO, ColorRGBA.Red);
        c.scale(4);
        c.setLocalTranslation(settings.getWidth() / 2,
                settings.getHeight() / 2, 0);
        guiNode.attachChild(c); // attach to 2D user interface
    }
    private static Box mesh = new Box(Vector3f.ZERO, 1, 1, 1);

    public Geometry myBox(String name, Vector3f loc, ColorRGBA color) {
        Geometry geom = new Geometry(name, mesh);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);
        geom.setLocalTranslation(loc);

        return geom;
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals("Walk")) {
            channel.setAnim("Walk", 0.50f);
            channel.setLoopMode(LoopMode.Loop);
            channel.setSpeed(1f);
        }
    }

    @Override
    public void onAnimChange(AnimControl ac, AnimChannel ac1, String string) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float intensity, float tpf) {
            // se comprueba que el trigger indentificado corresponda a la accion deseada
            if (name.equals(MAPPING_ROTATE)) {
                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                rootNode.collideWith(ray, results);
                for (int i = 0; i < results.size(); i++) {
                    float dist = results.getCollision(i).getDistance();
                    Vector3f pt = results.getCollision(i).getContactPoint();
                    String target = results.getCollision(i).getGeometry().
                            getName();
                    //System.out.println("Selection: #" + i + ": " + target
                    //        + " at " + pt + ", " + dist + " WU away.");
                    
                }
                
            }
        }
    };


public static void obtenerOleadaEnemigos(ArrayList<Enemigo> enemigosTotales){
    TestMapa.enemigosTotales = enemigosTotales;
}
    
  private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals("ToggleShop") && isPressed) {
                toggleShopScreen();
            }
        }
    };
  
  private void toggleShopScreen() {
        if (isShopVisible) {
            nifty.gotoScreen("empty"); // Oculta la pantalla de la tienda
            flyCam.setEnabled(true); // Habilita el control de la cámara
            flyCam.setDragToRotate(false); // Permite rotar la cámara sin hacer clic
            inputManager.setCursorVisible(false); // Oculta el cursor
        } else {
            nifty.gotoScreen("shopScreen"); // Muestra la pantalla de la tienda
            flyCam.setEnabled(false); // Deshabilita el control de la cámara
            flyCam.setDragToRotate(true); // Requiere hacer clic para rotar la cámara
            inputManager.setCursorVisible(true); // Muestra el cursor
        }
        isShopVisible = !isShopVisible;
    }
  
  private void mostrarMensajeDerrota(){
        mensajeDerrota.setText("¡Has sido derrotado!\n"
                + "Presiona T para salir" );
        mensajeDerrota.setColor(ColorRGBA.Red);
        mensajeDerrota.setLocalTranslation(
                settings.getWidth() / 2 - mensajeDerrota.getLineWidth() / 2,
                settings.getHeight() / 2 + mensajeDerrota.getLineHeight() / 2,
                0);
        guiNode.attachChild(mensajeDerrota);
    }
  
   private void crearOleada(float tpf) {
        // Esperar hasta que el tiempo de espera haya pasado y no haya enemigos en la escena
        if (enemigosNode.getChildren().isEmpty()) {
            tiempoRestante -= tpf;
           
            if (tiempoRestante <= 0) {
                 System.out.println(tiempoRestante + "");
                // Generar configuraciones de enemigos basadas en el número de oleada
                ArrayList<int[]> configuracionesEnemigos = new ArrayList<>();
                if (numeroOleada == 1) {
                    configuracionesEnemigos.add(new int[]{0, 3, 1}); // 3 minioms y un seguidor
                } else if (numeroOleada == 2) {
                    configuracionesEnemigos.add(new int[]{2, 1, 1}); // 2 enemigos normales, un miniom y un seguidor
                } else if (numeroOleada == 3) {
                    configuracionesEnemigos.add(new int[]{4, 0, 2}); // 4 enemigos normales y 2 seguidores
                } else {
                    int normalEnemies = random.nextInt(7) + 6; // Entre 6 y 12 enemigos normales
                    int minioms = random.nextInt(7) + 6; // Entre 6 y 12 minioms
                    int followers = random.nextInt(3) + 2; // Entre 2 y 4 seguidores
                    configuracionesEnemigos.add(new int[]{normalEnemies, minioms, followers});
                }

                // Iniciar la oleada con la configuración generada
                Oleadas.iniciarOleada(configuracionesEnemigos.size(), 15, configuracionesEnemigos, assetManager, enemigosNode);
                
                // Resetear el temporizador y aumentar el número de oleada
                tiempoRestante = tiempoEspera;
                numeroOleada++;
                while(enemigosNode.getChildren().isEmpty()){
                    Oleadas.actualizar(tpf,enemigosNode,new Vector3f(cam.getLocation()));
                }
            }
        }
    }

}
