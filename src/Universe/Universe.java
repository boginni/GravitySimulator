package Universe;

import Main.Game;
import static Main.Game.Height;
import static Main.Game.Width;
import Main.ObjectCreator;
import Main.Ui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Universe {

    public class config {

        ArrayList<Corpse> corpses = new ArrayList();
        ArrayList<Corpse> Act = new ArrayList();
        double corpspeed;
        int tps;
        double sc;
    }

    config SolarSystem;

    private void initSolarSystem() {
        for (int i = 0; i < 360; i++) {

        }
        SolarSystem = new config();
        config con = SolarSystem;
        con.sc = 1000000;
        con.corpspeed = 1000;
        con.tps = 2000;
        Corpse Sun = new Star(0, 0, 0, Color.yellow);// Sol
        con.corpses.add(Sun);
        con.Act.add(Sun);
        Sun.setSpeed(0);
        Sun.setSize(695510 * 2);
        Sun.setMass(1.989 * Math.pow(10, 30));
        Corpse Earth = new Planet(0, 0, 0, Color.blue);// Terra
        Earth.setSize(6371 * 2);
        Earth.setMass(5.972 * Math.pow(10, 24));
        Earth.CanTrace = true;
        Earth = CreateCorpse(Sun, Earth, 149_600_000, 0, true);
        con.corpses.add(Earth);
        con.Act.add(Earth);
        Corpse a;
        for (int i = 0; i < 360; i++) {
            a = new Asteroid(0, 0, 0, new Color(100, 50, 0));
            a.setSize(1);
            a = CreateCorpse(Sun, a, 250_600_000 + new Random().nextInt(25_000_000), i, true);
            con.corpses.add(a);
        }

        for (int i = 0; i < 720; i++) {
            a = new Asteroid(0, 0, 0, new Color(100, 50, 0));
            a = CreateCorpse(Sun, a, 550_600_000 + new Random().nextInt(100_000_000), i, true);
            con.corpses.add(a);
        }
    }

    public void Resize() {
        CorpseCreator.Resize();
    }
    public static double GRAVITY_CONSTANT = 6.674284 * Math.pow(10, -11);
    public static double Scale = 1;
    public static double CorpSpeed = 1;
    double Zoom = 1;
    public static ObjectCreator CorpseCreator;
    public static ArrayList<Corpse> corpses;
    public static ArrayList<Corpse> Atractional;

    boolean Mouse_Both_B1fist, Mouse_Both_B2fist;
    boolean Tec_Left, Tec_Up, Tec_Right, Tec_Down;
    public boolean CreateSingleCorpse;
    public boolean Mouse_B1, Mouse_Middle, Mouse_B2;
    public boolean CrtlMod, Zooming;
    public static boolean isFrozen = false;

    int OrbHelperX, OrbHelperY;
    public int OrbMacroTick = 0, OrbMaxMacroTick = 30, OrbMinMacroTick = 3,
            OrbCurrentMacroTick = 30;
    public int MacroTick = 0, MaxMacroTick = 30, CurrentMacroTick = 30,
            MinMacroTick = 3;
    public int Mxstart, Mxfinal, Mystart, Myfinal, MouseX, MouseY;
    public static int CamX = 0, CamY = 0;
    public static double CamMX = 0, CamMY = 0;
    public static double maUP = 0, maDown = 0, maLeft = 0, maRight = 0;

    public static Corpse CamTrack, OrbHelper;
    private int Mzt = 120, ZoomingTick = Mzt;
    Rectangle rect;

    //Medida Padrão = SI
    public static void initObjects() {
        corpses = new ArrayList();
        Atractional = new ArrayList();
        CamTrack = null;
        OrbHelper = null;
    }

    public static void LoadConfig(config con) {
        corpses = con.corpses;
        Atractional = con.Act;
        Scale = con.sc;
        Game.MaxFrames = con.tps;
        CorpSpeed = con.corpspeed;
    }

    public Universe() {
        initObjects();
        initSolarSystem();
        LoadConfig(SolarSystem);
        CorpseCreator = new ObjectCreator();
        rect = new Rectangle(0, 0, (int) ESCpKM(Width), (int) ESCpKM(Height));
    }

    public void Tick() {
        try {
            if (!isFrozen) {
                for (int i = 0; i < corpses.size(); i++) {
                    Corpse c = corpses.get(i);
                    c.Tick();
                }
            }
        } catch (Exception e) {
        }
    }

    public void subTick() {
        Zooming = Zoom != 1;
        if (Zooming) {
            Mzt = 15;
            ZoomingTick = Mzt;
            Scale *= Zoom;
            Zoom = 1;
        } else {
            ZoomingTick--;
        }

        if (CamTrack != null) {
            CamMX = CamTrack.getX();
            CamMY = CamTrack.getY();
        }
        CamX = (int) KMpESC(CamMX) - Width / 2;
        CamY = (int) KMpESC(CamMY) - Height / 2;

        rect = new Rectangle(0, 0, (int) ESCpKM(Width), (int) ESCpKM(Height));

        if (CamTrack == null) {
            double cs = ESCpKM((CrtlMod) ? 10 : 1);
            if (Tec_Left) {
                CamMX -= cs + maLeft * 0.01;
                maLeft += cs;
            } else {
                maLeft = 0;
            }
            if (Tec_Up) {
                CamMY -= cs + maUP * 0.01;
                maUP += cs;
            } else {
                maUP = 0;
            }
            if (Tec_Right) {
                CamMX += cs + maRight * 0.01;
                maRight += cs;
            } else {
                maRight = 0;
            }
            if (Tec_Down) {
                CamMY += cs + maDown * 0.01;
                maDown += cs;
            } else {
                maDown = 0;
            }
        } else if (CamTrack instanceof spaceShip) {
            
            float ammount = 1.0f;
            spaceShip ship = (spaceShip) CamTrack;
            
            if (Tec_Left) {
                ship.angle += ammount;
            }
            if (Tec_Right) {
                ship.angle -= ammount;
            }
            if (ship.angle > 360) {
                ship.angle -= 360;
            }
            if (ship.angle < 0) {
                ship.angle += 360;
            }
            
            double force = 0.05f;
            double angle = Math.toRadians(ship.angle) + Math.PI;
            
            if (Tec_Up) {
                ship.Xs += Math.sin(angle) * force;
                ship.Ys += Math.cos(angle) * force;
            }
            if (Tec_Down) {
                ship.Xs -= Math.sin(angle) * force;
                ship.Ys -= Math.cos(angle) * force;
            }
        }
        CorpseCreator.Tick();
        //Criação de Corpos por Mouse 1
        if (CorpseCreator.CanSpawn && CrtlMod && Mouse_B1) {
            if (MacroTick < 1) {
                MacroTick = CurrentMacroTick;
                CurrentMacroTick -= 5;
                if (CurrentMacroTick < MinMacroTick) {
                    CurrentMacroTick = MinMacroTick;
                }
                CreateCorpse(Mxstart, Mxfinal, Mystart, Myfinal,
                        MaxMacroTick - CurrentMacroTick);
            } else {
                MacroTick--;
            }
        } else {
            MacroTick--;
            CurrentMacroTick = MaxMacroTick;
        }

        if (CreateSingleCorpse) {
            CreateCorpse(Mxstart, Mxfinal, Mystart, Myfinal, 0);
            CreateSingleCorpse = false;
        }

        if (Mouse_B2 && !CorpseCreator.isAticve) {
            OrbHelperX = Mxfinal + CamX;
            OrbHelperY = Myfinal + CamY;
        }

        //Criação de Corpos Por Mouse 2
        if (OrbHelper != null) {
            if (CrtlMod && Mouse_B2 && CorpseCreator.CanSpawn) {
                if (MacroTick < 1) {
                    double d = new Point(OrbHelper.GetXwofset(), OrbHelper.GetYwofset())
                            .distance(OrbHelperX - CamX, OrbHelperY - CamY);
                    if (d > KMpESC(OrbHelper.getDiameter())) {
                        CreateCorpse(OrbHelper, OrbHelperX,
                                OrbHelperY, (OrbMaxMacroTick - OrbCurrentMacroTick), true);
                    } else {
                        System.out.printf("Distance %fnR: %f%n", d, KMpESC(OrbHelper.getDiameter()));
                    }
                    MacroTick = OrbCurrentMacroTick;
                    OrbCurrentMacroTick -= 5;
                    if (OrbCurrentMacroTick < MinMacroTick) {
                        OrbCurrentMacroTick = OrbMinMacroTick;
                    }
                }
            } else {
                OrbMacroTick--;
                OrbCurrentMacroTick = OrbMaxMacroTick;
            }
        } else {
            MacroTick--;
            OrbCurrentMacroTick = OrbMaxMacroTick;
        }

    }

    public void Render(Graphics g) {
        for (int i = 0; i < corpses.size(); i++) {
            Corpse c = corpses.get(i);
            c.Render(g);
            if (!rect.contains(c.GetXwofset(), c.GetYwofset())) {
                c.RezetTrace();
            }
        }
        if (CamTrack != null) {
            g.setColor(Color.blue);
            int r = CamTrack.GetRadius() + 25;
            g.drawOval(CamTrack.GetXwofset() - r / 2, CamTrack.GetYwofset() - r / 2, r, r);
            //Corpse.FutureTrace(g, CamTrack, CamTrack.getX(), CamTrack.getY(), CamTrack.getXs(), CamTrack.getYs());
        }
        if (OrbHelper != null) {
            double d = new Point(OrbHelper.GetXwofset(), OrbHelper.GetYwofset())
                    .distance(OrbHelperX - CamX, OrbHelperY - CamY);
            NumberFormat nf3 = NumberFormat.getInstance(new Locale("da", "DK"));
            double Gforce = CalcGforce(ESCpM(d), OrbHelper.Mass);
            double Time = 2 * Math.PI * Math.sqrt(Math.pow(ESCpM(d), 3) / (GRAVITY_CONSTANT * OrbHelper.Mass));;
            Ui.OrbtHelper_Radius = (String.format("%s Km", nf3.format(ESCpKM(d))));
            if (Gforce == 0) {
                Ui.OrbtHelper_Gforce = "Não Aplicavel";
            } else {
                Ui.OrbtHelper_Gforce = String.format("%s M/s²", nf3.format(Gforce));
            }
            String s = ((OrbHelper.HaveGravity) ? String.format("%s", (Time > 0) ? (int) Time : 0) : "Na");
            Ui.OrbtHelper_orTime = (s);

            g.setColor(Color.yellow);
            g.drawLine(OrbHelper.GetXwofset(), OrbHelper.GetYwofset(), OrbHelperX - CamX, OrbHelperY - CamY);
            g.drawOval((int) (OrbHelper.GetXwofset() - d), OrbHelper.GetYwofset() - (int) d,
                    (int) d * 2, (int) d * 2);

        }
        if (Mouse_B1 && !CorpseCreator.isAticve) {
            double angle = getAngle(Mxfinal, Myfinal, Mxstart, Mystart);
            double d = new Point(Mxstart, Mystart).distance(Mxfinal, Myfinal) * 0.01;
            d = ESCpM(d);
            double Xs = MpKM(getSin(angle) * d),
                    Ys = MpKM(getCos(angle) * d);
            //Corpse.FutureTrace(g, null, ESCpKM(Mxstart + CamX), ESCpKM(Mystart + CamY), Xs, Ys);
            g.setColor((CrtlMod) ? Color.BLUE : Color.red);
            g.drawLine(Mxstart, Mystart, Mxfinal, Myfinal);
        }
        if (ZoomingTick > 0) {
            g.setColor(new Color(255, 0, 0, 255 * ZoomingTick / Mzt));
            int x, y;
            x = Width / 2;
            y = Height / 2;
            g.drawLine(x - 25, y, x + 25, y);
            g.drawLine(x, y - 25, x, y + 25);
        }

    }

    //
    // Special
    //
    public static boolean isColliding(Corpse a, Corpse b) {
        return getDistance(a.getX(), a.getY(), b.getX(), b.getY()) - a.GetRadius() < b.GetRadius();
    }

    public static boolean isColliding(Corpse a, int x, int y) {
        return getDistance(a.getX(), a.getY(), x, y) < a.GetRadius();
    }

    public static void CreateFromCollision(Corpse a, Corpse b) {
        if (a instanceof Star && b instanceof Star) {
            double Nx, Ny;
            double Nxs, Nys, Nspeed, Size;
            Nxs = (a.getXs() + b.getXs()) / 2;
            Nys = (a.getYs() + b.getYs()) / 2;
            Nx = (a.getX() + b.getX()) / 2;
            Ny = (a.getY() + b.getY()) / 2;
            Size = (a.GetRadius() + b.GetRadius()) / 2;
            SuperNova s = new SuperNova(Nx, Ny, Size, Color.white);
            Atractional.add(s);
            s.setMass((a.getMass() + b.getMass()) * 0.75);

            s.setDirection(Nxs, Nys, 0);
            a.RemoveThis();
            b.RemoveThis();
        } else if (a.HaveGravity && b.HaveGravity) {
            if (a.GetRadius() > b.GetRadius()) {
                ExplodeWhenCollide(b, a);
            } else {
                ExplodeWhenCollide(a, b);
            }
        } else {

        }
    }

    public static void ExplodeWhenCollide(Corpse Target, Corpse a) {

    }

    public void CreateCorpse(int Mxstart, int Mxfinal, int Mystart, int Myfinal, int Angle) {
        Mxstart += CamX;
        Mxfinal += CamX;
        Mystart += CamY;
        Myfinal += CamY;
        Corpse c = ObjectCreator.CorpseSetter.getCorpse();
        double g = getAngle(Mxfinal, Myfinal, Mxstart, Mystart);
        try {
            g += new Random().nextInt(Angle) - Angle / 2;
        } catch (Exception e) {
        }
        double d = getDistance(Mxstart, Mystart, Mxfinal, Myfinal) * 0.01;
        d /= CorpSpeed * 99;
        d = ESCpKM(d);
        c.setDirection((int) ESCpKM(Mxstart), (int) ESCpKM(Mystart), g, d);
        System.out.println(c.toString());
        c.setSize(ObjectCreator.CorpseSetter.getCurrentSize());
        if (CamTrack != null) {
            c.setDirection(CamTrack.getFullXs(), CamTrack.getFullYs(),
                    CamTrack.getSpeed());
        }
        c.CanTrace = CorpseCreator.TraceButton_IsActive;
        c.AddThis();

    }

    public void CreateCorpse(Corpse c, int x, int y, int Var, boolean dir) {
        Corpse Orbter = ObjectCreator.CorpseSetter.getCorpse();
        if (Var != 0) {
            x += new Random().nextInt(Var) - Var / 2;
            y += new Random().nextInt(Var) - Var / 2;
        }
        double R = getDistance(ESCpKM(x), ESCpKM(y), c.x, c.y);
        R = KMpM(R);
        double g = getAngle(c.getX(), c.getY(), ESCpKM(x), ESCpKM(y)) + ((dir) ? +90 : -90);
        double speed = Math.sqrt((Universe.GRAVITY_CONSTANT * c.getMass()) / R);
        speed = MpKM(speed);
        Orbter.CanTrace = CorpseCreator.TraceButton_IsActive;
        Orbter.setDirection((int) ESCpKM(x), (int) ESCpKM(y), g, speed);
        Orbter.setDirection(c.getFullXs(), c.getFullYs(),
                c.getSpeed());
        System.out.println(Orbter.toString());
        Orbter.AddThis();
    }

    public Corpse CreateCorpse(Corpse a, Corpse b, double R, double Angle, boolean dir) {
        double x, y;
        x = getSin(Angle) * R;
        y = getCos(Angle) * R;
        R = KMpM(R);
        double g = getAngle(x, y, a.getX(), a.getY()) + 90;
        double speed = Math.sqrt((Universe.GRAVITY_CONSTANT * a.getMass()) / R);
        speed = MpKM(speed);
        double Xs = getSin(g) * speed;
        double Ys = getCos(g) * speed;
        b.setDirection(x, y);
        b.setDirection(a.getFullXs() + Xs, a.getFullYs() + Ys, a.getSpeed());
        return b;
    }

    //
    // Conversão
    //
    public static double ESCpM(double d) { //Escala Para Metro
        return KMpM(d) * Scale;
    }

    public static double ESCpKM(double d) {
        return d * Scale;
    }

    public static double MpESC(double d) { //Metro Para Escala
        return MpKM(d) / Scale;
    }

    public static double KMpESC(double d) {
        return d / Scale;
    }

    public static double KMpM(double d) {
        return d * 1000;
    }

    public static double MpKM(double d) {
        return d / 1000;
    }

    //
    // Math
    // obs: GetAngle em escala
    public static double getDistance(double x1, double y1, double x2, double y2) {
        double dx, dy;
        dx = Math.pow(x1 - x2, 2);
        dy = Math.pow(y1 - y2, 2);
        return Math.sqrt(dx + dy);
    }

    public static double getAngle(double aX, double aY, double bX, double bY) {
        double disX, disY;
        disX = aX - bX;
        disY = aY - bY;
        return getAngle(disX, disY);
    }

    public static double getAngle(Corpse Center, Corpse Target) {
        double disX, disY;
        disX = Center.getX() - Target.getX();
        disY = Center.getY() - Target.getY();
        return getAngle(disX, disY);
    }

    private static double getAngle(double a, double b) {
        double angle = Math.toDegrees(Math.atan2(a, b));
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    public static double getSin(double Angle) {
        Angle = Math.toRadians(Angle);
        return Math.sin(Angle) * -1;
    }

    public static double getCos(double Angle) {
        Angle = Math.toRadians(Angle);
        return Math.cos(Angle) * -1;
    }

    public static double GetGforce(double x, double y, Corpse Target) {
        double dis = getDistance(x, y, Target.getX(), Target.getY());
        dis = KMpM(dis);
        if (dis < Target.GetRadius()) {
            dis = Target.GetRadius();
        }
        return CalcGforce(dis, Target.getMass());
    }

    public static double CalcGforce(double d, double mass) {
        double Gforce = mass * GRAVITY_CONSTANT / Math.pow(d, 2);
        return Gforce; // Retorno em M/s²
    }

    public static String getMassType(double mass) {
        String Type;
        int Meder = 0;
        while (mass >= 10) {
            mass = mass / 10;
            Meder++;
        }
        Type = "*10^" + Integer.toString(Meder);
        return String.format("%3.3f%sKg", mass, Type);
    }

    //
    // Input
    //
    public void TecPressesd(int i, boolean b) {
        if (i == 37 || i == 65) {
            Tec_Left = b;
        }
        //left
        if (i == 38 || i == 87) {
            Tec_Up = b;
        }
        //up
        if (i == 39 || i == 68) {
            Tec_Right = b;
        }
        //right
        if (i == 40 || i == 83) {
            Tec_Down = b;
        }
        if (i == 32 && b) {
            isFrozen = !isFrozen;
        }
        if (i == 17) {
            CrtlMod = b;
        }
    }

    public void MousePressed(int x, int y, int i, boolean b) {
        if (i == 1) {
            Mouse_B1 = b;
        }
        if (i == 2) {
            Mouse_Middle = b;
        }
        if (i == 3) {
            Mouse_B2 = b;
        }
        if (b) {
            Mxfinal = x;
            Myfinal = y;
            Mxstart = x;
            Mystart = y;
        }
        if (CorpseCreator.isAticve) {
            CorpseCreator.MouseClick(x, y, i, b);
        }
        //Methods
        if (Mouse_Middle) {
            boolean b2 = true;
            for (Corpse c : corpses) {
                if (c.getCamDistance((int) x, y) < KMpESC(c.GetRadius() * 2) + 25) {
                    if (CrtlMod) {
                        OrbHelper = c;
                    } else {
                        CamTrack = c;
                    }
                    b2 = false;
                    break;
                }
            }
            if (b2) {
                if (CrtlMod) {
                    OrbHelper = null;
                } else {
                    CamTrack = null;
                }
            }
        }
    }

    public void MouseDragged(int x, int y) {
        Mxfinal = x;
        Myfinal = y;
    }

    public void MouseWheelDrag(int i) {
        if (i == 1) {
            Zoom += 0.1;
        } else if (i == -1) {
            Zoom -= 0.1;
        }
    }

    public void mouseMove(int x, int y) {
        MouseX = x;
        MouseY = y;
    }
}
