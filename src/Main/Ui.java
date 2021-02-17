package Main;

import Universe.Corpse;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import Universe.Universe;

public class Ui {

    public class Info {

        String s;

        public Info() {
            s = "";
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

    }

    int fps = 0;
    int x, y, wi, he;
    boolean less, plus, CtrlMod, Hud;
    public static ArrayList<Info> strings = new ArrayList();

    public static String CamX, CamY, Mouse, OrbtHelper_Radius,
            CreationSpeed, OrbtHelper_Scales, OrbtHelper_Gforce,
            OrbtHelper_orTime;

    public void Resize() {
        x = 0;
        y = Game.Height - 70;
        wi = Game.Width;
        he = 100;
    }

    public Ui() {
        Resize();
    }

    public class Infos {

        boolean isActive;
    }

    public void Tick() {
        if (CtrlMod) {
            if (plus) {
                Universe.CorpSpeed += Universe.CorpSpeed * 0.01;
            }
            if (less && Universe.CorpSpeed > 0.00001)
                Universe.CorpSpeed -= Universe.CorpSpeed * 0.01;
        } else {
            if (plus) {
                Game.MaxFrames++;
            }
            if (less) {
                if (Game.MaxFrames > 1) {
                    Game.MaxFrames--;
                }
            }
        }
    }

    public void Render(Graphics g) {
        g.setColor(new Color(0, 60, 60));
        g.fillRect(0, y, wi, y + he);
        g.setFont(new Font("Arial", 1, 20));
        FontMetrics me = g.getFontMetrics();
        int swi = 0;
        int y = this.y + me.getHeight();
        g.setColor(Color.WHITE);
        //
        double seg = Universe.CorpSpeed * fps;

        //
        String s1 = String.format("speed %s/Seg", SecondsPTime((int) seg));
        g.drawString(s1, x + swi, y);

        swi += me.stringWidth(s1) + 10;
        s1 = String.format("CorpSpeed: %.3f", Universe.CorpSpeed);
        g.drawString(s1, x + swi, y);
        swi += me.stringWidth(s1) + 10;
        s1 = String.format("Scale: %.0f Km/Pixel", Universe.Scale);
        g.drawString(s1, x + swi, y);
        swi += me.stringWidth(s1) + 10;
        s1 = String.format("TPS: %d", fps);
        g.drawString(s1, x + swi, y);
        swi += me.stringWidth(s1) + 10;
        s1 = String.format("Speed: %.3f", Universe.CorpSpeed);
        g.drawString(s1, x + swi, y);
        swi += me.stringWidth(s1) + 10;

        Corpse a;
        String s,e="  ";
        int w, h;
        a = Universe.OrbHelper;
        int x = Game.Width - 250 + 32;
        y = Game.Height / 2 - 100 - 32;
        h = y;
        if (a != null) { // OrbtHelper
            g.setColor(Color.orange);
            g.drawRoundRect(0, y, 200, 250, 25, 25);
            g.setColor(new Color(255, 150, 0, 50));
            g.fillRoundRect(0, y, 200, 250, 25, 25);
            s = a.getClass().getName().substring(9);
            w = me.stringWidth(s);
            h += 20;
            g.setColor(Color.white);
            g.drawString(s, 0 + 100 - w / 2, h);
            h += 20;
            s = "Raio";
            g.drawString(s, 0, h);
            h += 20;
            s = e+OrbtHelper_Radius;
            g.drawString(s, 0, h);
            h += 20;
            s = "Gforce:";
            g.drawString(s, 0, h);
            h += 20;
            s = e+OrbtHelper_Gforce;
            g.drawString(s, 0, h);
            h += 20;
            s = "Período Orbital:";
            g.drawString(s, 0, h);
            h += 20;
            int time;
            if (!"Na".equals(OrbtHelper_orTime)) {
                time = Integer.parseInt(OrbtHelper_orTime);
                s = SecondsPTime(time);
            } else {
                s = "Não Aplicavel";
            }
            g.drawString(e+s, 0, h);

        }
        a = Universe.CamTrack;
        h = y;
        if (a != null) {
            g.setColor(Color.blue);
            g.drawRoundRect(x, y, 200, 250, 25, 25);
            g.setColor(new Color(0, 100, 255, 50));
            g.fillRoundRect(x, y, 200, 250, 25, 25);
            s = a.getClass().getName().substring(9);
            w = me.stringWidth(s);
            g.setColor(Color.white);
            h += 20;
            g.drawString(s, x + 100 - w / 2, h);
            x += 3;
            h += 20;
            s = "Mass:";
            g.drawString(s, x, h);
            h += 20;
            if (!a.HaveGravity) {
                s = "Não aplicavel";
            } else
                s = e+Universe.getMassType(a.getMass());
            g.drawString(s, x, h);
            h += 20;
            s = "Size:";
            g.drawString(s, x, h);
            h += 20;
            s = e+String.format("%d Km", a.getSize());
            g.drawString(s, x, h);
            h += 20;
            s = "Speed 1:";
            g.drawString(s, x, h);
            h += 20;
            s = e+String.format("%.3f Km/s", a.getSpeed());
            g.drawString(s, x, h);
            h += 20;
            s = "Speed 2";
            g.drawString(s, x, h);
            h += 20;
            s = e+String.format("%.3f Km/s", a.getInitialSpeed());
            g.drawString(s, x, h);
            h += 20;
            s = "Speed 3";
            g.drawString(s, x, h);
            h += 20;
            s = e+String.format("%.3f Km/s", a.getTotalSpeed());
            g.drawString(s, x, h);

        }

    }

    public String SecondsPTime(int Sec) {
        int Min = 0, Hour = 0, Day = 0;
        while (Sec >= 60) {
            Min++;
            Sec -= 60;
            if (Min >= 60) {
                Min -= 60;
                Hour++;
            }
            if (Hour >= 24) {
                Hour -= 24;
                Day++;
            }
        }
        return String.format("%02d:%02d:%02d+%03d Dias", Hour, Min, Sec, Day);
    }

    public void TecPressesd(int i, boolean b) {
        if (i == 107) { //VK_PLUS
            plus = b;
        }
        if (i == 109) { //VK_LESS
            less = b;
        }
        if (i == 17) {
            CtrlMod = b;
        }
    }

    public void mouseMove(int x, int y) {
        if (Mouse != null) {
            Mouse = "Mouse x|y:" + x + " | " + y;
        }
    }
}
