import javax.swing.*;
import java.util.GregorianCalendar;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import com.jogamp.opengl.glu.GLU;


public class Main {
    public final static int    WIDTH = 480, HEIGHT = 300; // Width and height of screen
    public final static boolean CUT_CORNERS = true; // Whether to cut outside corners

    public final static double THICKNESS = 10; // Thickness of lines
    public final static double GAP = 1;
    public final static double SCALE = 1;

    public final static double NUM_WIDTH = 50, NUM_HEIGHT = 80;

    public static final double CUT = (CUT_CORNERS)?THICKNESS/2.0:0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Clock Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(WIDTH, HEIGHT);

            GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            GLCanvas canvas = new GLCanvas(capabilities);


            FPSAnimator anim = new FPSAnimator(10);
            anim.add(canvas);
            anim.start();

            Clock c = new Clock();
            canvas.addGLEventListener(c);

            frame.getContentPane().add(canvas);
            frame.setVisible(true);
        });
    }
    private static class Clock implements GLEventListener {
        GregorianCalendar cal = new GregorianCalendar();

        @Override
        public void init(GLAutoDrawable glAutoDrawable) {
            GL2 gl = glAutoDrawable.getGL().getGL2();
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();
            GLU glu = new GLU();
            glu.gluOrtho2D(0, WIDTH, HEIGHT, 0);
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glViewport(0, 0, WIDTH, HEIGHT);
            gl.glLoadIdentity();

            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINES);
        }

        @Override
        public void dispose(GLAutoDrawable glAutoDrawable) {

        }

        @Override
        public void display(GLAutoDrawable glAutoDrawable) {
            final GL2 gl = glAutoDrawable.getGL().getGL2();
            gl.glColor3f(1.0f, 0.0f, 0.0f);

            drawZero(gl, 20, 20);

            gl.glFlush();
        }

        @Override
        public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

        }

        public void update(){

        }

        public void render(GL2 gl){

        }

        private void drawTop(final GL2 gl, int xOff, int yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+THICKNESS+GAP, yOff+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+THICKNESS-GAP);
            gl.glVertex2d(xOff+THICKNESS+GAP, yOff+THICKNESS-GAP);

            gl.glEnd();
        }
        private void drawMiddle(final GL2 gl, int xOff, int yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+THICKNESS+GAP, yOff+NUM_HEIGHT/2.0-THICKNESS/2.0+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+NUM_HEIGHT/2.0-THICKNESS/2.0+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+NUM_HEIGHT/2.0+THICKNESS/2.0-GAP);
            gl.glVertex2d(xOff+THICKNESS+GAP, yOff+NUM_HEIGHT/2.0+THICKNESS/2.0-GAP);

            gl.glEnd();
        }
        private void drawBottom(final GL2 gl, int xOff, int yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+THICKNESS+GAP, yOff+NUM_HEIGHT-THICKNESS+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+NUM_HEIGHT-THICKNESS+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+NUM_HEIGHT-GAP);
            gl.glVertex2d(xOff+THICKNESS+GAP, yOff+NUM_HEIGHT-GAP);

            gl.glEnd();
        }
        private void drawTopLeft(final GL2 gl, int xOff, int yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+GAP, yOff+GAP+CUT);
            gl.glVertex2d(xOff+THICKNESS-GAP, yOff+GAP+CUT);
            gl.glVertex2d(xOff+THICKNESS-GAP, yOff+NUM_HEIGHT/2.0-GAP);
            gl.glVertex2d(xOff+GAP, yOff+NUM_HEIGHT/2.0-GAP);

            gl.glEnd();
        }
        private void drawBottomLeft(final GL2 gl, int xOff, int yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+GAP, yOff+NUM_HEIGHT/2.0+GAP);
            gl.glVertex2d(xOff+THICKNESS-GAP, yOff+NUM_HEIGHT/2.0+GAP);
            gl.glVertex2d(xOff+THICKNESS-GAP, yOff+NUM_HEIGHT-GAP-CUT);
            gl.glVertex2d(xOff+GAP, yOff+NUM_HEIGHT-GAP-CUT);

            gl.glEnd();
        }
        private void drawTopRight(final GL2 gl, int xOff, int yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS+GAP, yOff+GAP+CUT);
            gl.glVertex2d(xOff+NUM_WIDTH-GAP, yOff+GAP+CUT);
            gl.glVertex2d(xOff+NUM_WIDTH-GAP, yOff+NUM_HEIGHT/2.0-GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS+GAP, yOff+NUM_HEIGHT/2.0-GAP);

            gl.glEnd();
        }
        private void drawBottomRight(final GL2 gl, int xOff, int yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS+GAP, yOff+NUM_HEIGHT/2.0+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-GAP, yOff+NUM_HEIGHT/2.0+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-GAP, yOff+NUM_HEIGHT-GAP-CUT);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS+GAP, yOff+NUM_HEIGHT-GAP-CUT);

            gl.glEnd();
        }

        public void drawZero(final GL2 gl, int xOff, int yOff){
            drawTop(gl, xOff, yOff);
            drawBottom(gl, xOff, yOff);
            drawTopLeft(gl, xOff, yOff);
            drawBottomLeft(gl, xOff, yOff);
            drawTopRight(gl, xOff, yOff);
            drawBottomRight(gl, xOff, yOff);
        }
    }
}