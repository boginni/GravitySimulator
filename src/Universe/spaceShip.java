/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Universe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;


public class spaceShip extends Corpse {
    public double angle = 0;
    public boolean trusting = false;
    public spaceShip(double x, double y, double Angle, Color cor) {
        super(x, y, Angle, cor);
        HaveGravity = false;
    }

    @Override
    public void Render(Graphics g) {
        
        double x, y;
        double s;
        x = GetXwofset();
        y = GetYwofset();
        s = (int) Universe.KMpESC(size);
        if (s < 0.75f)
            s = 0.75f;
        g.setColor(cor);
        
        int[] px = new int[3];
        int[] py = new int[3];
        
        int ppx = (int) (0 * s);
        int ppy = (int) (-23 * s);
        int hipote = (int) (10 * s); 
        int cateto = (int) (26 * s);
        
        px[0] = 0 + ppx; px[1] = -hipote + ppx; px[2] = hipote + ppx;
        py[0] = 0 + ppy; py[1] = cateto + ppy; py[2] = cateto + ppy;
        
        double nx, ny;
        double rad = Math.toRadians(angle);
        
        for (int i = 0; i < 3; i++) {
            nx = (px[i] * Math.cos(rad)) + (py[i] * Math.sin(rad));
            ny = (px[i] * -Math.sin(rad)) + (py[i] * Math.cos(rad));
            px[i] = (int) (nx + x);
            py[i] = (int) (ny + y);
        }
        
        g.drawPolygon(px,py,3);
        
        if(trusting){
            
        }
        
        if (CanTrace) {
            Trace(g);
        }
    }
    
    
    
    @Override
    public void RemoveThis() {
        Universe.corpses.remove(this);
        Universe.CamTrack = null;
    }

    @Override
    public void AddThis() {
        Universe.corpses.add(this);
        Universe.CamTrack = this;
    }
    
}
