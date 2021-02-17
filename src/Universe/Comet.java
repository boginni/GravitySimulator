/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Universe;

import Main.Game;
import java.awt.Graphics;
import java.util.ArrayList;

import java.awt.Color;

public class Comet extends Asteroid {

    int GasTrace_CTick = 0;

    public Comet(double x, double y, double Angle, Color cor) {
        super(x, y, Angle, cor);
        Temperature = 1;
        gasTrace = new ArrayList();
        size = 10;
        HaveGravity = false;
    }

    ArrayList<GasTrace> gasTrace;

    @Override
    public void Render(Graphics g) {
        super.Render(g);
        for (int i = 0; i < gasTrace.size(); i++) {
            if (gasTrace.get(i).isDead) {
                gasTrace.remove(i);
            } else {
                gasTrace.get(i).Render(g);
            }
        }
        if (Temperature > 0 && GasTrace_CTick < 1) {
            double angle, xs, ys;
            for (int i = 0; i < Universe.corpses.size(); i++) {
                Corpse s = Universe.corpses.get(i);
                if (!(s instanceof Star))
                    continue;
                if (getDistance(s) < 150) {
                    angle = Universe.getAngle(s, this);
                    xs = Universe.getSin(angle) * 2;
                    ys = Universe.getCos(angle) * 2;
                    gasTrace.add(new GasTrace(60, 5, this.cor.brighter(), x, y, xs, ys));
                }
            }

            GasTrace_CTick = 5;
        }
        GasTrace_CTick--;
    }

    @Override
    public void Tick() {
        super.Tick(); //To change body of generated methods, choose Tools | Templates.

    }

}
