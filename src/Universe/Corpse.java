package Universe;

import static Universe.Universe.CorpSpeed;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Para Alterar Posições X e Y, deve-se utilizar medida em Escalas
 *
 * @author BRUNNO
 */
public abstract class Corpse {

    protected static class Trace {

        public double Xs, Ys, Xf, Yf;
        int Life;
        int Maxlife = 60;
        double White, Maxwhite;
        boolean isDead;

        Trace(double X, double Y) {
            Xs = X;
            Ys = Y;
            Xf = X;
            Yf = Y;
            this.Maxwhite = 255;
            Life = Maxlife;

        }

        public Trace(int Xs, int Ys, int Xf, int Yf, int Maxlife, int Maxwhite) {
            this.Xs = Xs;
            this.Ys = Ys;
            this.Xf = Xf;
            this.Yf = Yf;
            this.Maxlife = Maxlife;
            this.Maxwhite = Maxwhite;
            Life = Maxlife;
            White = Maxwhite;

        }

        public void setXY(int x, int y, Corpse Creator) {
            Xf = x;
            Yf = y;
        }

        public void Render(Graphics g) {
            Tick();
            int x1, y1, x2, y2;
            x1 = (int) Universe.KMpESC(Xs) - Universe.CamX;
            y1 = (int) Universe.KMpESC(Ys) - Universe.CamY;
            x2 = (int) Universe.KMpESC(Xf) - Universe.CamX;
            y2 = (int) Universe.KMpESC(Yf) - Universe.CamY;
            int White = (int) this.White;
            g.setColor(new Color(White, White, White));
            g.drawLine(x1, y1,
                    x2, y2);
        }

        public void Tick() {
            Life--;
            White = Life * Maxwhite / Maxlife;
            if (Life < 1) {
                isDead = true;
            }

        }
    }

    protected static class GasTrace {

        int Life, MaxLife;
        double size, Maxsize;

        Color cor;
        boolean isDead;
        double x, y, Xs, Ys;

        public GasTrace(int MaxLife, double Maxsize, Color cor, double x, double y, double Xs, double Ys) {
            this.MaxLife = MaxLife;
            this.Maxsize = Maxsize;
            this.cor = cor;
            this.x = x;
            this.y = y;
            this.Xs = Xs;
            this.Ys = Ys;
            Life = MaxLife;
            size = this.Maxsize;
            isDead = false;
        }

        public void Render(Graphics g) {
            Rentick();
            g.setColor(cor);
            g.fillOval(getX() - getSize() / 2, getY() - getSize() / 2, getSize(), getSize());
        }

        public void Rentick() {
            x += Xs;
            y += Ys;
            Life--;
            if (Life < 1) {
                isDead = true;
            }
            size = Life * Maxsize / MaxLife;

        }

        public int getX() {
            return (int) x - Universe.CamX;
        }

        public int getY() {
            return (int) y - Universe.CamY;
        }

        public int getSize() {
            return (int) size;
        }
    }
    public double x, y, Xs, Ys, Xsp = 0, Ysp = 0; //Km
    double Mass, size, density;
    public boolean isColliding, HaveGravity, CanTrace = false;
    double Temperature;
    Color cor;
    ArrayList<Trace> trace;
    static final int TRACE_CREATION_MAX_TICK = 5;
    int Trace_Tick = 0;
    double Trace_x, Trace_y;
    Trace lastTrace;
    public int Mxstart, Mxfinal, Mystart, Myfinal;
    public static double Zoom = 1;

    public Corpse(double x, double y, double Angle, Color cor) {
        this.x = x;
        this.y = y;
        this.Xs = Universe.MpESC(Universe.getSin(Angle));
        this.Ys = Universe.MpESC(Universe.getCos(Angle));
        size = 5;
        this.cor = cor;
    }

    public void Tick() {
        double gX = 0, gY = 0, angle, gForce;
        Corpse c;
        x += Xsp * Universe.CorpSpeed;
        y += Ysp * Universe.CorpSpeed;
        for (int i = 0; i < Universe.Atractional.size(); i++) {
            c = Universe.Atractional.get(i);
            if (c == this)
                continue;
            //if (Universe.isColliding(this, c))
            //    this.Collide(c);
            angle = Universe.getAngle(getX(), getY(), c.getX(), c.getY());
            gForce = Universe.GetGforce(x, y, c) * CorpSpeed;
            gX += Universe.MpKM(Universe.getSin(angle) * gForce);
            gY += Universe.MpKM(Universe.getCos(angle) * gForce);
        }
        Xs += gX;
        Ys += gY;
        x += Xs * Universe.CorpSpeed;
        y += Ys * Universe.CorpSpeed;
    }

    public void Render(Graphics g) {
        int x, y, s;
        x = GetXwofset();
        y = GetYwofset();
        s = (int) Universe.KMpESC(size);
        if (s < 2)
            s = 2;
        g.setColor(cor);
        g.fillOval(x - s / 2, y - s / 2, s, s);
        if (CanTrace) {
            Trace(g);
        }
    }

    public void Trace(Graphics g) {
        if (trace == null) {
            trace = new ArrayList();
            lastTrace = new Trace(getX(), getY());
        }
        lastTrace.Xf = getX();
        lastTrace.Yf = getY();
        if (!(Trace_Tick > 0)) {
            lastTrace = new Trace(getX(), getY());
            Trace_Tick = TRACE_CREATION_MAX_TICK;
            trace.add(lastTrace);
        } else {
            Trace_Tick--;
        }
        for (int i = 0; i < trace.size(); i++) {
            if (trace.get(i).isDead) {
                trace.remove(i);
            } else {
                trace.get(i).Render(g);
            }
        }

    }

    public static void FutureTrace(Graphics g, Corpse Me, double x, double y, double Xs, double Ys) {
        double angle, gforce;
        Corpse c;
        int x1, y1, x2 = 0, y2;
        int CamX = (int) Universe.ESCpKM(Universe.CamX),
                CamY = (int) Universe.ESCpKM(Universe.CamY);
        for (int i = 0; i < 500; i++) {
            for (int j = 0; j < Universe.Atractional.size(); j++) {
                c = Universe.Atractional.get(j);
                if (c == Me)
                    continue;
                if (Universe.isColliding(c, (int) x, (int) y))
                    return;
                angle = Universe.getAngle((int) x, (int) y, c.getX(), c.getY());
                gforce = Universe.GetGforce((int) x, (int) y, c) * CorpSpeed;
                Xs += Universe.MpESC(Universe.getSin(angle) * gforce);
                Ys += Universe.MpESC(Universe.getCos(angle) * gforce);
            }
            x1 = (int) x;
            y1 = (int) y;
            x += Xs * Universe.CorpSpeed;
            y += Ys * Universe.CorpSpeed;
            x2 = (int) x;
            y2 = (int) y;
            g.setColor(Color.green);
            g.drawLine((int) x1 - CamX, y1 - CamY,
                    x2 - CamX, y2 - CamY);
        }

    }

    @Override
    public String toString() {
        double gforce = Universe.CalcGforce(Universe.KMpM(getDiameter()), this.Mass);
        return String.format("XY em Escalas: %.0f| %.0f %n"
                + "Xs: %3.3f Km/s%n"
                + "Ys: %3.3f Km/s%n"
                + "R: %3.3f Km%n"
                + "Mass: %s%n"
                + "Speed: %f Km/s%n"
                + "Gforce: %f M/s²%n", getX(),
                getY(), getXs(), getYs(),
                getDiameter(), Universe.getMassType(Mass), getSpeed(), gforce);

    }

    public void RezetTrace() {
        trace = null;
        lastTrace = null;
    }

    public abstract void RemoveThis();

    public abstract void AddThis();

    //Setters
    public void setSpeed(double speed) {
        Xs *= speed;
        Ys *= speed;
    }

    public void setDirection(int Mxstart, int Mystart, double Angle, double speed) {
        x = Mxstart;
        y = Mystart;
        this.Xs = Universe.getSin(Angle) * speed;
        this.Ys = Universe.getCos(Angle) * speed;
    }

    public void setDirection(double xs, double ys, double speed) {
        Xsp = xs;
        Ysp = ys;
    }

    public void setDirection(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(double i) {
        this.size = i;
    }

    public void setMass(double m) {
        this.Mass = m; //Mass em Kg
    }

    //Special Getters
    /**
     * Distância Entre o Corpo em Km
     *
     * @param target Corpo para Medir a distancia
     * @return Posiotion.distance(target.position)
     */
    public double getDistance(Corpse target) {
        return Universe.getDistance(x, y, target.getX(), target.getY());
    }

    public double getCamDistance(int x, int y) {
        Point p = new Point(x, y);
        return p.distance(GetXwofset(), GetYwofset());
    }

    /**
     * Retorna a Massa do Corpo em Kg
     *
     * @return This.mass
     */
    public double getMass() {
        return this.Mass;
    }

    //Atribute Getters
    public double getSpeed() {
        return Math.hypot(Xs, Ys);
    }

    public double getTotalSpeed() {
        return Math.hypot(Xsp, Ysp) + getSpeed();
    }

    public double getInitialSpeed() {
        return Math.hypot(Xsp, Ysp);
    }

    public int GetRadius() {
        return (int) (size / 4);
    }

    public double getDiameter() {
        return (size / 2);
    }

    public int getSize() {
        return (int) size;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int GetXwofset() {
        return (int) (Universe.KMpESC(x) - Universe.CamX);
    }

    public int GetYwofset() {
        return (int) (Universe.KMpESC(y) - Universe.CamY);
    }

    public double getXs() {
        return Xs;
    }

    public double getYs() {
        return Ys;
    }

    public double getFullXs() {
        return Xs + Xsp;
    }

    public double getFullYs() {
        return Ys + Ysp;
    }

}
