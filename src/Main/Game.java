package Main;

import Universe.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

public class Game extends Canvas implements Runnable,
        KeyListener, MouseInputListener, MouseWheelListener {

    public static final int SCALE = 3;

    public static int Width = 240 * 3, Height = 160 * 3;
    public static int MaxFrames = 60;
    public BufferedImage Image_Frame = new BufferedImage(Width, Height, 2);
    public BufferedImage Image_Ui = new BufferedImage(Width, Height, 2);
    public BufferedImage Image_BackGround = new BufferedImage(Width, Height, 1);
    public BufferedImage image = new BufferedImage(Width, Height, 1);
    JFrame frame = new JFrame("Gravity Simulator");
    public static Ui ui = new Ui();
    static Game game = new Game();
    Universe universe = new Universe();

    public static void main(String[] args) {
        game.run();
    }
    public static boolean Said;

    public Game() {
        setPreferredSize(new Dimension(Width, Height));
        initFrame();
        addKeyListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
    }

    public void initFrame() {
        frame.setDefaultCloseOperation(3);
        frame.add(this);
        frame.setResizable(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void run() {
        requestFocus();
        int frames = 0;
        long timer = 1000000000 / MaxFrames;
        long NextFrame = System.nanoTime() + timer;
        long NextSecond = System.nanoTime() + 1000000000;
        long NextRender = System.nanoTime() + 1000000000 / 60;
        boolean b = true;
        while (true) {
            if (System.nanoTime() > NextRender) {
                timer = 1000000000 / 60;
                NextRender += timer;
                subTick();
                Render();
            }
            
            if (System.nanoTime() > NextFrame && MaxFrames > 0) {
                timer = 1000000000 / MaxFrames;
                NextFrame += timer;
                Tick();
                frames++;
            }

            if (System.nanoTime() > NextSecond) {
                NextSecond += 1000000000;
                ui.fps = frames;
                frames = 0;
            }
        }

    }

    public void Resize() {
        Width = frame.getWidth();
        Height = frame.getHeight();
        this.setPreferredSize(new Dimension(Width, Height));
        universe.Resize();
        ui.Resize();
    }

    public void Tick() {
        universe.Tick();
    }

    public void subTick() {
        universe.subTick();
        if (Width != frame.getWidth() || Height != frame.getHeight()) {
            Resize();
        }
        universe.subTick();
        ui.Tick();

    }

    public void Render() {
        BufferStrategy bs = this.getBufferStrategy();
        Graphics g;
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Image_Frame = new BufferedImage(Width, Height, 2);
        g = Image_Frame.getGraphics();
        Corpse c = new Star(Width / 2, Height / 2, 0, Color.red);
        universe.Render(g);
        //---------> Ui
        Image_Ui = new BufferedImage(Width, Height, 2);
        g = Image_Ui.getGraphics();
        ui.Render(g);
        if (universe.CorpseCreator.isAticve) {
            universe.CorpseCreator.Render(g);
        }
        //----------> Backg
        Image_BackGround = new BufferedImage(Width, Height, 2);
        g = Image_BackGround.getGraphics();
        if (Universe.isFrozen)
            g.setColor(new Color(0, 25, 40));
        else
            g.setColor(Color.BLACK);
        g.fillRect(0, 0, Width, Height);
        g = bs.getDrawGraphics();
        g.drawImage(Image_BackGround, 0, 0, null);
        g.drawImage(Image_Frame, 0, 0, null);
        g.drawImage(Image_Ui, 0, 0, null);
        bs.show();
    }

    public void TecPressed(int i, boolean b) {
        if (i == KeyEvent.VK_ESCAPE && b)
            universe.CorpseCreator.ActionToggleOnOff();
        if (universe.CorpseCreator.isAticve) {
            universe.CorpseCreator.TecPressed(i, b);
        } else {
            ui.TecPressesd(i, b);
            universe.TecPressesd(i, b);
        }
    }

    public void RatPressed(int x, int y, int i, boolean b) {
        //Status
        universe.MousePressed(x, y, i, b);
        if (!b && universe.CorpseCreator.CanSpawn && i == 1) {
            universe.CreateSingleCorpse = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        TecPressed(ke.getKeyCode(), true);
        if (ke.getKeyCode() == KeyEvent.VK_F11)
            frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        TecPressed(ke.getKeyCode(), false);
    }

    @Override
    public void mouseClicked(MouseEvent me) {

    }

    @Override
    public void mousePressed(MouseEvent me) {
        RatPressed(me.getX(), me.getY(), me.getButton(), true);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        RatPressed(me.getX(), me.getY(), me.getButton(), false);
    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {

    }

    @Override
    public void mouseDragged(MouseEvent me) {
        universe.MouseDragged(me.getX(), me.getY());
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        universe.mouseMove(me.getX(), me.getY());
        ui.mouseMove(me.getX(), me.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        universe.MouseWheelDrag(mwe.getWheelRotation());

    }

}
