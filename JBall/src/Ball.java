import java.awt.*;

public class Ball {

    private static double e = 1;
    private static boolean can_overlay = false;

    private double r;
    private double mass;
    private double x, y;
    private double kx, ky;
    private double dkx, dky;
    private Color color;

    public static Ball makeBall(double x, double y) {
        double r = Math.random() * 20 + 20;
        double mass = r * r;
        double kx = Math.random() * 3 + 1;
        double ky = Math.random() * 3 + 1;
        double dkx = ((int) (Math.random() * 2) * 2 - 1) * kx;
        double dky = ((int) (Math.random() * 2) * 2 - 1) * ky;
        Color color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
        return new Ball(r, mass, x, y, kx, ky, dkx, dky, color);
    }

    public Ball(double r, double mass, double x, double y, double kx, double ky, double dkx, double dky, Color color) {
        this.r = r;
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.kx = kx;
        this.ky = ky;
        this.dkx = dkx;
        this.dky = dky;
        this.color = color;
    }

    public Ball(double x, double y) {
        r = Math.random() * 20 + 20;
        mass = r * r;
        this.x = x;
        this.y = y;
        color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
        kx = ky = dkx = dky = 0;
    }

    public void setVelocity(double kx, double ky, double dkx, double dky) {
        this.kx = kx;
        this.ky = ky;
        this.dkx = dkx;
        this.dky = dky;
    }

    public void genVelocity() {
        this.kx = Math.random() * 3 + 1;
        this.ky = Math.random() * 3 + 1;
        this.dkx = ((int) (Math.random() * 2) * 2 - 1) * kx;
        this.dky = ((int) (Math.random() * 2) * 2 - 1) * ky;
    }

    public void collision() {
        if (x - r < 0) {
            x = r;
            dkx = kx;
        }
        if (y - r < 0) {
            y = r;
            dky = ky;
        }
        if (x + r > Paint.Width) {
            x = Paint.Width - r;
            dkx = -kx;
        }
        if (y + r > Paint.Height) {
            y = Paint.Height - r;
            dky = -ky;
        }
    }

    public static void collision(Ball a, Ball b) {
        double l = Math.hypot(a.x - b.x, a.y - b.y), nl = a.r + b.r;
        if (l <= nl) {
            // Вычисляем относительную скорость
            double rvx = b.dkx - a.dkx;
            double rvy = b.dky - a.dky;
            // Вычисляем нормаль коллизии
            double nx = b.x - a.x;
            double ny = b.y - a.y;
            double nh = Math.hypot(nx, ny);
            nx /= nh;
            ny /= nh;
            // Вычисляем относительную скорость относительно направления нормали
            double velAlongNormal = rvx * nx + rvy * ny;

            // Не выполняем вычислений, если скорости разделены
            if (velAlongNormal <= 0) {

                // Вычисляем скаляр импульса силы
                double j = -(1 + e) * velAlongNormal;
                j /= 1 / a.mass + 1 / b.mass;

                // Прикладываем импульс силы
                double ix = j * nx, iy = j * ny;
                a.dkx -= 1 / a.mass * ix;
                a.dky -= 1 / a.mass * iy;
                a.kx = Math.abs(a.dkx);
                a.ky = Math.abs(a.dky);
                b.dkx += 1 / b.mass * ix;
                b.dky += 1 / b.mass * iy;
                b.kx = Math.abs(b.dkx);
                b.ky = Math.abs(b.dky);
            }
            // Разделяем шары, если они вошли друг в друга
            if (!can_overlay) {
                double nxa, nxb, nya, nyb, k = (1 + nl / l) / 2;
                nxa = b.x - (b.x - a.x) * k;
                nxb = a.x - (a.x - b.x) * k;
                nya = b.y - (b.y - a.y) * k;
                nyb = a.y - (a.y - b.y) * k;
                a.x = nxa;
                a.y = nya;
                b.x = nxb;
                b.y = nyb;
            }
        }
    }

    public boolean touch(double x, double y) {
        double dx = this.x - x, dy = this.y - y;
        return (r * r >= dx * dx + dy * dy);
    }

    public void move() {
        x += dkx;
        y += dky;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int) (x - r), (int) (y - r), (int) (2 * r), (int) (2 * r));
    }

    public void tick() {
        collision();
        move();
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getKx() {
        return kx;
    }

    public void setKx(double kx) {
        this.kx = kx;
    }

    public double getKy() {
        return ky;
    }

    public void setKy(double ky) {
        this.ky = ky;
    }

    public double getDkx() {
        return dkx;
    }

    public void setDkx(double dkx) {
        this.dkx = dkx;
    }

    public double getDky() {
        return dky;
    }

    public void setDky(double dky) {
        this.dky = dky;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
