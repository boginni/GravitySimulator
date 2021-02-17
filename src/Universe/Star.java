/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Universe;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author BRUNNO
 */
public class Star extends Corpse {

    public static BufferedImage Corona;
    BufferedImage myCorona;
    public Star(double x, double y, double Angle, Color cor) {
        super(x, y, Angle, cor);
        size = 25;
        this.Mass = 100;
        HaveGravity = true;
        if(Corona == null){
            CreateCorona();
        } else {
            myCorona = Corona;
        }
    }

    static void CreateCorona() {
        int x = 0;
        int y = 0;
        int s;
        int r = 255, g = 255, b = 0, a = 0;
        Corona = new BufferedImage(720, 480, 2);
        Graphics graphics = Corona.getGraphics();
        for (int i = 0; i < 255; i += 5) {
            s = 100 * 3 - i;
            a++;
            graphics.setColor(new Color(r, g, b, a));
            graphics.fillOval(Corona.getWidth() / 2 - s / 2, Corona.getHeight()
                    / 2 - s / 2, s, s);
        }
        a = 120;
        //Cor
        s = 100;
        graphics.setColor(new Color(255, 255, 255));
        graphics.fillOval(Corona.getWidth() / 2 - s / 2, Corona.getHeight()
                / 2 - s / 2, s, s);
        //White Degrade
        for (int i = 0; i < 255; i++) {
            s = 100 * 5 / 3 - i;
            a = i;
            if (a > 10)
                a = 5;
            graphics.setColor(new Color(r, g, b, a));
            graphics.fillOval(Corona.getWidth() / 2 - s / 2, Corona.getHeight()
                    / 2 - s / 2, s, s);
        }
    }

    
    @Override
    public void Render(Graphics Mgraphics) {
        super.Render(Mgraphics);
        /*
        int s = (int) Universe.KMpESC(getSize());
        if (s < 2)
            s = 2;
        Mgraphics.drawImage(myCorona, GetXwofset() - s / 2, GetYwofset() - s / 2
                ,s,s, null);
        */
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
