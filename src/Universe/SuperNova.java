/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Universe;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author BRUNNO
 */
public class SuperNova extends Corpse {

    public static BufferedImage LightExplosion;
    int LightExplosionLife = 0;
    float alpha = 1;
    boolean Up = true;

    //GasTrace
    public SuperNova(double x, double y, double Angle, Color cor) {
        super(x, y, Angle, cor);
        if (LightExplosion == null) {
            CreateLightExplosion();
        }
    }

    private static void CreateLightExplosion() {
        LightExplosion = new BufferedImage(400, 400, 2);
        Graphics g = LightExplosion.getGraphics();
        int s, a = 0;
        for (int i = 0; i < 255; i++) {
            s = 400 * (255 - i) / 255;
            a++;
            g.setColor(new Color(i, i, i, a));
            g.fillOval(LightExplosion.getWidth() / 2 - s / 2,
                    LightExplosion.getHeight() / 2 - s / 2, s, s);
        }
    }

    @Override
    public void Render(Graphics g) {
        int s = this.GetRadius() * 100;
        Graphics2D g2 = (Graphics2D) g;
        if (alpha > 0.01) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.drawImage(LightExplosion, GetXwofset() - s / 2, GetYwofset() - s / 2,
                    s, s, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
        g.setColor(cor);
        s = this.GetRadius();
        g.drawOval(GetXwofset() - s / 2, GetYwofset() - s / 2, s, s);
    }

    @Override
    public void Tick() {
        if (alpha > 0.01) {
            alpha -= 0.01;
        }
        //Raiz(2*GetGforce*Mass/GetRadius)
    }

    @Override
    public void RemoveThis() {
        Universe.corpses.remove(this);
        Universe.Atractional.remove(this);
    }

    @Override
    public void AddThis() {
        Universe.corpses.add(this);
        Universe.Atractional.add(this);
    }

}
