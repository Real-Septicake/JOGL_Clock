import javax.swing.*;
import java.time.ZonedDateTime;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import com.jogamp.opengl.glu.GLU;


public class Main {
    public final static int[][] NUMS = { // { Top, Middle, Bottom, Top Left, Bottom Left, Top Right, Bottom Right }
            {1, 0, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 0, 1, 1},
            {1, 1, 1, 0, 1, 1, 0},
            {1, 1, 1, 0, 0, 1, 1},
            {0, 1, 0, 1, 0, 1, 1},
            {1, 1, 1, 1, 0, 0, 1},
            {1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 1, 0, 1, 1},
    };

    /*
    -----------------------------------------------------------------------
    Configs
    -----------------------------------------------------------------------
     */

    //SCREEN
    public final static int     WIDTH = 480, HEIGHT = 300; // Width and height of screen

    // NUMBERS
    public final static boolean CUT_CORNERS = true; // Whether to cut outside corners
    public final static boolean SHADOWS = true;
    public final static double  THICKNESS = 8; // Thickness of lines
    public final static double  GAP = 1;
    public final static double  NUM_KERNING = 5; // Distance between numbers
    public final static double  NUM_WIDTH = 50, NUM_HEIGHT = 80;
    public static final double CUT = (CUT_CORNERS)?THICKNESS/2.0:0;

    // COLORS
    public final static float[] MAIN_COLOR_RGB = { 1.0f, 0, 0 };
    public final static float[] SHADOW_COLOR_RGB = { 0.2f, 0.2f, 0.2f };

    // COLON
    public final static double   COLON_WIDTH = 10;
    public final static double   COLON_DOT_DISTANCE = 20;
    public final static double   COLON_DOT_HEIGHT = 10;
    public final static double   COLON_KERNING = 10;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Clock Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(WIDTH, HEIGHT);

            GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);

            final GLCanvas canvas = new GLCanvas(capabilities);
            Clock c = new Clock();
            canvas.addGLEventListener(c);
            canvas.setSize(WIDTH, HEIGHT);

            frame.getContentPane().add(canvas);
            frame.setVisible(true);

            final FPSAnimator anim = new FPSAnimator(canvas, 20, true);
            anim.start();
        });
    }
    private static class Clock implements GLEventListener {

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
        }

        @Override
        public void dispose(GLAutoDrawable glAutoDrawable) {

        }

        @Override
        public void display(GLAutoDrawable glAutoDrawable) {
            final GL2 gl = glAutoDrawable.getGL().getGL2();

            gl.glClear (GL2.GL_COLOR_BUFFER_BIT |  GL2.GL_DEPTH_BUFFER_BIT );
            drawTime(gl, 20, 20);

            gl.glFlush();
        }

        @Override
        public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

        }

        /*
        -----------------------------------------------------------------------
        Segment Rendering
        -----------------------------------------------------------------------
         */

        private void drawTop(final GL2 gl, double xOff, double yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+THICKNESS+GAP, yOff+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+THICKNESS-GAP);
            gl.glVertex2d(xOff+THICKNESS+GAP, yOff+THICKNESS-GAP);

            gl.glEnd();
        }
        private void drawMiddle(final GL2 gl, double xOff, double yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+THICKNESS+GAP, yOff+NUM_HEIGHT/2.0-THICKNESS/2.0+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+NUM_HEIGHT/2.0-THICKNESS/2.0+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+NUM_HEIGHT/2.0+THICKNESS/2.0-GAP);
            gl.glVertex2d(xOff+THICKNESS+GAP, yOff+NUM_HEIGHT/2.0+THICKNESS/2.0-GAP);

            gl.glEnd();
        }
        private void drawBottom(final GL2 gl, double xOff, double yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+THICKNESS+GAP, yOff+NUM_HEIGHT-THICKNESS+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+NUM_HEIGHT-THICKNESS+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+NUM_HEIGHT-GAP);
            gl.glVertex2d(xOff+THICKNESS+GAP, yOff+NUM_HEIGHT-GAP);

            gl.glEnd();
        }
        private void drawTopLeft(final GL2 gl, double xOff, double yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+GAP, yOff+GAP+CUT);
            gl.glVertex2d(xOff+THICKNESS-GAP, yOff+GAP+CUT);
            gl.glVertex2d(xOff+THICKNESS-GAP, yOff+NUM_HEIGHT/2.0-GAP);
            gl.glVertex2d(xOff+GAP, yOff+NUM_HEIGHT/2.0-GAP);

            gl.glEnd();
        }
        private void drawBottomLeft(final GL2 gl, double xOff, double yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+GAP, yOff+NUM_HEIGHT/2.0+GAP);
            gl.glVertex2d(xOff+THICKNESS-GAP, yOff+NUM_HEIGHT/2.0+GAP);
            gl.glVertex2d(xOff+THICKNESS-GAP, yOff+NUM_HEIGHT-GAP-CUT);
            gl.glVertex2d(xOff+GAP, yOff+NUM_HEIGHT-GAP-CUT);

            gl.glEnd();
        }
        private void drawTopRight(final GL2 gl, double xOff, double yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS+GAP, yOff+GAP+CUT);
            gl.glVertex2d(xOff+NUM_WIDTH-GAP, yOff+GAP+CUT);
            gl.glVertex2d(xOff+NUM_WIDTH-GAP, yOff+NUM_HEIGHT/2.0-GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS+GAP, yOff+NUM_HEIGHT/2.0-GAP);

            gl.glEnd();
        }
        private void drawBottomRight(final GL2 gl, double xOff, double yOff){
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS+GAP, yOff+NUM_HEIGHT/2.0+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-GAP, yOff+NUM_HEIGHT/2.0+GAP);
            gl.glVertex2d(xOff+NUM_WIDTH-GAP, yOff+NUM_HEIGHT-GAP-CUT);
            gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS+GAP, yOff+NUM_HEIGHT-GAP-CUT);

            gl.glEnd();
        }

        /*
        -----------------------------------------------------------------------
        Number Rendering
        -----------------------------------------------------------------------
         */

        public void drawNumber(int num, final GL2 gl, double xOff, double yOff){
            drawDigit(num/10, gl, xOff, yOff);
            drawDigit(num%10, gl, xOff+(NUM_WIDTH + NUM_KERNING), yOff);
        }

        public void drawDigit(int digit, final GL2 gl, double xOff, double yOff){
            int[] segs = NUMS[digit];

            for(int i = 0; i < segs.length; i++){
                if(segs[i] == 1){
                    gl.glColor3f(MAIN_COLOR_RGB[0], MAIN_COLOR_RGB[1], MAIN_COLOR_RGB[2]);
                }else if(segs[i] == 0 && !SHADOWS){
                    continue;
                }else{
                    gl.glColor3f(SHADOW_COLOR_RGB[0], SHADOW_COLOR_RGB[1], SHADOW_COLOR_RGB[2]);
                }

                switch (i){
                    case 0 -> drawTop(gl, xOff, yOff);
                    case 1 -> drawMiddle(gl, xOff, yOff);
                    case 2 -> drawBottom(gl, xOff, yOff);
                    case 3 -> drawTopLeft(gl, xOff, yOff);
                    case 4 -> drawBottomLeft(gl, xOff, yOff);
                    case 5 -> drawTopRight(gl, xOff, yOff);
                    case 6 -> drawBottomRight(gl, xOff, yOff);
                }
            }
        }

        /*
        -----------------------------------------------------------------------
        Clock Rendering
        -----------------------------------------------------------------------
         */

        public void drawColon(final GL2 gl, double xOff, double yOff){
            double colonOffset = NUM_HEIGHT/2.0-COLON_DOT_DISTANCE/2.0-THICKNESS;

            gl.glColor3f(MAIN_COLOR_RGB[0], MAIN_COLOR_RGB[1], MAIN_COLOR_RGB[2]);

            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2d(xOff, yOff+colonOffset);
            gl.glVertex2d(xOff+COLON_WIDTH, yOff+colonOffset);
            gl.glVertex2d(xOff+COLON_WIDTH, yOff+colonOffset+ COLON_DOT_HEIGHT);
            gl.glVertex2d(xOff, yOff+colonOffset+ COLON_DOT_HEIGHT);

            gl.glVertex2d(xOff, yOff+NUM_HEIGHT-colonOffset- COLON_DOT_HEIGHT);
            gl.glVertex2d(xOff+COLON_WIDTH, yOff+NUM_HEIGHT-colonOffset- COLON_DOT_HEIGHT);
            gl.glVertex2d(xOff+COLON_WIDTH, yOff+NUM_HEIGHT-colonOffset);
            gl.glVertex2d(xOff, yOff+NUM_HEIGHT-colonOffset);

            gl.glEnd();
        }

        public void drawTime(final GL2 gl, double xOff, double yOff){
            ZonedDateTime time = ZonedDateTime.now();
            drawNumber(time.getHour(), gl, xOff, yOff);
            drawColon(gl, xOff+NUM_WIDTH*2+NUM_KERNING+COLON_KERNING, yOff);
            drawNumber(time.getMinute(), gl, xOff+NUM_WIDTH*2+NUM_KERNING+COLON_KERNING*2+COLON_WIDTH, yOff);
            drawColon(gl, xOff+NUM_WIDTH*4+NUM_KERNING*2+COLON_KERNING*3+COLON_WIDTH, yOff);
            drawNumber(time.getSecond(), gl, xOff+NUM_WIDTH*4+NUM_KERNING*2+COLON_KERNING*4+COLON_WIDTH*2, yOff);
        }
    }
}