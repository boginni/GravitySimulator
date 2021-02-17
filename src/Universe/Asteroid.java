/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Universe;

import java.awt.Color;

/**
 *
 * @author BRUNNO
 */
public class Asteroid extends Corpse{
    
    
    public Asteroid(double x, double y, double Angle, Color cor) {
        super(x, y, Angle, cor);
        HaveGravity = false;
        
    }

    @Override
    public void RemoveThis() {
        Universe.corpses.remove(this);
    }

    @Override
    public void AddThis() {
        Universe.corpses.add(this);
    }

    
    
    
    
}
