import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;


public class Paint extends JPanel implements Runnable, MouseListener, KeyListener {

    public static int Width, Height, Diagonal;
    public Thread thread;

    public static boolean running = false;

    private int delay = 10;
    private boolean isCtrl = false;
    ArrayList<Ball> balls = new ArrayList<>();
    private Ball gen_ball = null;

    public Paint() {
        setDoubleBuffered(true);
        addMouseListener(this);
        this.addKeyListener(this);
    }

    private void megacollision() {
        for (int x = 0; x < balls.size(); x++) {
            for (int y = x + 1; y < balls.size(); y++) {
                Ball.collision(balls.get(y), balls.get(x));
            }
        }
    }

    public void startTread() {
        thread = new Thread(this);
        thread.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < balls.size(); i++) {
            balls.get(i).draw(g);
        }
        if (gen_ball != null) {
            gen_ball.draw(g);
        }
    }

    public void updateView() {
        Width = getWidth();
        Height = getHeight();
        Diagonal = (int) Math.hypot(Height, Width);
    }

    @Override
    public void run() {
        while (running) {
            megacollision();
            updateView();
            for (int i = 0; i < balls.size(); i++) {
                balls.get(i).tick();
            }
            this.repaint();
            try {
                thread.sleep(delay);
            } catch (InterruptedException e) {
                running = false;
                throw new RuntimeException(e);
            }

        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(isCtrl) {
            for (int i = balls.size() - 1; i >= 0; i--) {
                if (balls.get(i).touch(e.getX(), e.getY())) {
                    balls.remove(i);
                    return;
                }
            }
        }
        else {
            gen_ball = new Ball(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(gen_ball != null) {
            double x1 = gen_ball.getX(), y1 = gen_ball.getY(), x2 = e.getX(), y2 = e.getY(), k = (double) Diagonal / 30;
            if (x1 == x2 && y1 == y2) {
                gen_ball.genVelocity();
                balls.add(gen_ball);
            } else {
                double dkx = (x2 - x1) / k, dky = (y2 - y1) / k;
                double kx = Math.abs(dkx), ky = Math.abs(dky);
                gen_ball.setVelocity(kx, ky, dkx, dky);
                balls.add(gen_ball);
            }
            gen_ball = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        isCtrl = e.getKeyCode() == 17 || isCtrl;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        isCtrl = !(e.getKeyCode() == 17) && isCtrl;
    }
}