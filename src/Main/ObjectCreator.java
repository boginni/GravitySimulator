package Main;

import Universe.Comet;
import Universe.Star;
import Universe.Asteroid;
import Universe.Planet;
import Universe.GasCloud;
import Universe.Corpse;
import Universe.Universe;
import Universe.spaceShip;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class ObjectCreator {

    public void Resize() {
        int xs = Game.Width * 1 / 8;
        int wi = Game.Width - xs * 2;
        int ys = Game.Height * 1 / 8;
        int he = 10;
        RestartCorpses = new Rectangle(xs + wi * 1 / 2 - 50, ys + Game.Height * 6 / 8 * 7 / 8 - 15,
                100, 30);
        CorpseType_Rect = new Rectangle(xs + wi * 1 / 2 - 125,
                ys + he, 250, 50);
        he += 60;
        CorRect = new Rectangle(xs + wi * 1 / 2 - 125,
                ys + he, 250, 50);
        he += 60;
        TraceButton_Rectangle = new Rectangle(xs + wi * 1 / 2 + 15 + 125,
                ys + 25 + 12, 20, 20);
        SizeButton_Rectangle = new Rectangle(xs + wi * 1 / 2 - 125,
                ys + he, 250, 50);
        he += 60;
        MassButton_Rectangle = new Rectangle(xs + wi * 1 / 2 - 125,
                ys + he, 250, 50);

    }

    public static class Cor {

        static int i = 0;
        static private final int MAX_INDEX = 7;
        static private Color cor;

        public static void Click1() {
            i++;
            if (i > MAX_INDEX)
                i = 0;
        }

        public static void Click2() {
            i--;
            if (i < 0)
                i = MAX_INDEX;
        }

        public static Color getColor(int atual) {
            int color = i + atual;
            if (color > MAX_INDEX)
                color -= MAX_INDEX + 1;
            if (color < 0)
                color += MAX_INDEX + 1;
            switch (color) {
                case 0:
                    cor = Color.BLUE;
                    break;
                case 1:
                    cor = Color.cyan;
                    break;
                case 2:
                    cor = Color.GREEN;
                    break;
                case 3:
                    cor = Color.yellow;
                    break;
                case 4:
                    cor = Color.red;
                    break;
                case 5:
                    cor = Color.magenta;
                    break;
                case 6:
                    cor = Color.WHITE;
                    break;
                case 7:
                    cor = Color.gray;
                    break;
                default:
                    throw new AssertionError();
            }
            return cor;
        }

    }

    public static class CorpseSetter {

        static final int MaxIndex = 5;
        static private String nome;
        static private int current = 0;
        static private double[][] Status;

        public static void Init() {
            Status = new double[MaxIndex + 1][2];
            for (int i = 0; i < MaxIndex + 1; i++) {
                Status[i][0] = 2;
                Status[i][1] = 1;
            }
            Status[4][0] = 695510*2;
            Status[4][1] = 1.989 * Math.pow(10, 30);
            Status[5][0] = 125;
        }

        public static double getCurrentSize() {
            return Status[getIndex()][0];
        }

        public static double getCurrentMass() {
            return Status[getIndex()][1];
        }

        public static void setCurrentSize(double size) {
            Status[getIndex()][0] = size;

            if (Status[getIndex()][0] < 2)
                Status[getIndex()][0] = 2;

            //Star Limit
            if (Status[4][0] < 75)
                Status[4][0] = 75;
        }

        public static void setCurrentMass(double mass) {
            Status[getIndex()][1] = mass;
            if (Status[getIndex()][1] < 1)
                Status[getIndex()][1] = 1;
        }

        public static int getIndex() {
            return current;
        }

        static public void Next() {
            current++;
            if (current > 5)
                current = 0;

        }

        static public void Last() {
            current--;
            if (current < 0)
                current = 5;
        }

        static public Corpse getCorpse() {
            Corpse c;
            switch (current) {
                case 0:
                    c = new Asteroid(0, 0, 0, Cor.getColor(0));
                    break;
                case 1:
                    c = new Comet(0, 0, 0, Cor.getColor(0));
                    break;
                case 2:
                    c = new GasCloud(0, 0, 0, Cor.getColor(0));
                    break;
                case 3:
                    c = new Planet(0, 0, 0, Cor.getColor(0));
                    break;
                case 4:
                    c = new Star(0, 0, 0, Cor.getColor(0));
                    break;
                case 5:
                    c = new spaceShip(0, 0, 0, Cor.getColor(0));
                    break;
                default:
                    c = new Comet(0, 0, 0, Cor.getColor(0));
                    break;
            }
            c.setSize(getCurrentSize());
            c.setMass(getCurrentMass());
            return c;
        }
    }

    public ObjectCreator() {
        CorpseSetter.Init();
        Resize();
    }

    public boolean isAticve;
    public boolean CanSpawn = !isAticve;
    Rectangle RestartCorpses = null;
    Rectangle CorRect = null;
    Rectangle CorpseType_Rect;
    Rectangle TraceButton_Rectangle;
    public boolean TraceButton_IsActive;
    Rectangle SizeButton_Rectangle;
    boolean SizeButton_Up, SizeButton_Down;
    Rectangle MassButton_Rectangle;
    boolean MassButton_Up, MassButton_Down;
    boolean CtrlMod;

    public void Tick() {
        if (CtrlMod) {
            if (SizeButton_Up) {
                CorpseSetter.setCurrentSize((int) CorpseSetter.getCurrentSize() + 1);
            }
            if (SizeButton_Down) {
                CorpseSetter.setCurrentSize((int) CorpseSetter.getCurrentSize() - 1);
            }
            if (MassButton_Up) {
                CorpseSetter.setCurrentMass(CorpseSetter.getCurrentMass() * 10);
                MassButton_Up = false;
            }
            if (MassButton_Down) {
                CorpseSetter.setCurrentMass(CorpseSetter.getCurrentMass() / 10);
                MassButton_Down = false;
            }
        } else {
            if (SizeButton_Up) {
                CorpseSetter.setCurrentSize(CorpseSetter.getCurrentSize() * 1.01);
            }
            if (SizeButton_Down) {
                CorpseSetter.setCurrentSize(CorpseSetter.getCurrentSize() * 0.99);
            }
            if (MassButton_Up) {
                CorpseSetter.setCurrentMass(CorpseSetter.getCurrentMass() * 1.01);
            }
            if (MassButton_Down) {
                CorpseSetter.setCurrentMass(CorpseSetter.getCurrentMass() * 0.99);
            }
        }

    }

    public void Render(Graphics g) {
        int xs, ys, wi, he;
        xs = Game.Width * 1 / 8;
        wi = Game.Width - xs * 2;
        ys = Game.Height * 1 / 8;
        he = Game.Height - ys * 2;
        FontMetrics me;
        g.setFont(new Font("Arial", 1, 25));
        me = g.getFontMetrics();
        g.setColor(new Color(0, 255, 255, 127));
        g.fillRoundRect(xs, ys, wi, he, 50, 50);
        g.drawRoundRect(xs, ys, wi, he, 50, 50);
        g.setColor(Color.black);
        //Curent Corpse Selection Rectangle
        g.drawRect(RestartCorpses.x, RestartCorpses.y, RestartCorpses.width,
                RestartCorpses.height);
        g.drawRect(CorpseType_Rect.x, CorpseType_Rect.y, CorpseType_Rect.width, CorpseType_Rect.height);
        int x, y;
        String s = CorpseSetter.getCorpse().getClass().getName().substring(9);
        x = CorpseType_Rect.x + (CorpseType_Rect.width - me.stringWidth(s)) / 2;
        y = CorpseType_Rect.y + (CorpseType_Rect.height - me.getHeight()) / 2 + me.getAscent();
        g.setColor(Color.white);
        g.drawString(s, x, y);
        //Color button
        g.setColor(Cor.getColor(-1));
        g.fillRect(CorRect.x, CorRect.y, CorRect.width - 166, CorRect.height);
        g.setColor(Cor.getColor(-2));
        g.fillRect(CorRect.x, CorRect.y, CorRect.width * 1 / 6, CorRect.height);
        g.setColor(Cor.getColor(1));
        g.fillRect(CorRect.x + 166, CorRect.y, CorRect.width - 166, CorRect.height);
        g.setColor(Cor.getColor(2));
        g.fillRect(CorRect.x + 209, CorRect.y, CorRect.width * 1 / 6, CorRect.height);
        g.setColor(Cor.getColor(0));
        g.fillRect(CorRect.x + 83, CorRect.y, CorRect.width - 166, CorRect.height);
        g.setColor(Color.black);
        g.drawRect(CorRect.x + 83, CorRect.y, CorRect.width - 166, CorRect.height);
        g.drawRect(CorRect.x, CorRect.y, CorRect.width, CorRect.height);

        if (TraceButton_IsActive) {
            g.setColor(Color.white);
            g.fillRect(TraceButton_Rectangle.x, TraceButton_Rectangle.y,
                    TraceButton_Rectangle.width, TraceButton_Rectangle.height);
        }
        //Size Button
        g.setColor(Color.BLACK);
        g.drawRect(TraceButton_Rectangle.x, TraceButton_Rectangle.y,
                TraceButton_Rectangle.width, TraceButton_Rectangle.height);
        s = String.format("%3.0f Km", CorpseSetter.getCurrentSize());
        x = SizeButton_Rectangle.x + (SizeButton_Rectangle.width - me.stringWidth(s)) / 2;
        y = SizeButton_Rectangle.y + (SizeButton_Rectangle.height - me.getHeight()) / 2 + me.getAscent();
        g.setColor(Color.white);
        g.drawString(s, x, y);
        //MASS Button
        g.setColor(Color.BLACK);
        g.drawRect(MassButton_Rectangle.x, MassButton_Rectangle.y,
                MassButton_Rectangle.width, MassButton_Rectangle.height);
        g.drawRect(SizeButton_Rectangle.x, SizeButton_Rectangle.y,
                SizeButton_Rectangle.width, SizeButton_Rectangle.height);
        double mass = CorpseSetter.getCurrentMass();
        s = Universe.getMassType(mass);
        x = MassButton_Rectangle.x + (MassButton_Rectangle.width - me.stringWidth(s)) / 2;
        y = MassButton_Rectangle.y + (MassButton_Rectangle.height - me.getHeight()) / 2 + me.getAscent();
        g.setColor(Color.white);
        g.drawString(s, x, y);

    }

    void TecPressed(int i, boolean b) {
        if (i == 17) {
            CtrlMod = b;
        }
    }

    public void MouseClick(int x, int y, int i, boolean b) {
        if (i == 1) {
            if (RestartCorpses.contains(x, y)) {
                Universe.initObjects();
            }
        }
        if (CorpseType_Rect.contains(x, y) && b) {
            if (i == 3)
                CorpseSetter.Last();
            if (i == 1)
                CorpseSetter.Next();
        }

        if (CorRect.contains(x, y) && b) {
            if (i == 1)
                Cor.Click1();
            if (i == 3)
                Cor.Click2();
        }

        if (SizeButton_Rectangle.contains(x, y)) {
            if (i == 1)
                SizeButton_Up = b;
            if (i == 3)
                SizeButton_Down = b;
        } else {
            SizeButton_Up = false;
            SizeButton_Down = false;
        }

        if (MassButton_Rectangle.contains(x, y)) {
            if (i == 1)
                MassButton_Up = b;
            if (i == 3)
                MassButton_Down = b;
        } else {
            MassButton_Up = false;
            MassButton_Down = false;
        }

        if (TraceButton_Rectangle.contains(x, y) && b) {
            TraceButton_IsActive = !TraceButton_IsActive;
        }
    }

    public void ActionToggleOnOff() {
        boolean b = !isAticve;
        isAticve = b;
        CanSpawn = !b;
    }

}
