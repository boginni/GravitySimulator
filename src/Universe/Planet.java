package Universe;

import java.awt.Color;

public class Planet extends Corpse{
    
    public Planet(double x, double y, double Angle, Color cor) {
        super(x, y, Angle, cor);
        HaveGravity = true;
        
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
