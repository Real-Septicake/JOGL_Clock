import javax.swing.*;
import java.awt.*;
import java.time.ZonedDateTime;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import com.jogamp.opengl.glu.GLU;

@SuppressWarnings("unused")
public class Main {

    /*
    -----------------------------------------------------------------------
    Configs
    -----------------------------------------------------------------------
     */

    private final static double  SCALE = 2;

    //SCREEN
    private final static double PADDING = 20 * SCALE;

    // NUMBERS
    private final static boolean CUT_CORNERS = true; // Whether to cut outside corners
    private final static boolean SHADOWS = true;
    private final static double  THICKNESS = 8*SCALE; // Thickness of lines
    private final static double  GAP = 1*SCALE;
    private final static double  NUM_KERNING = 5*SCALE; // Distance between numbers
    private final static double  NUM_WIDTH = 50*SCALE, NUM_HEIGHT = 80*SCALE;

    // COLORS (BG, Shadow, Highlight)
    private final static Palette RED = Palette.ofNormalFloat(new float[]{ 0.15f, 0.1f, 0.1f }, new float[]{ 0.3f, 0.15f, 0.15f }, new float[]{ 1.f, 0.f, 0.32f });
    private final static Palette GREEN = Palette.ofFloat(new float[]{ 24f, 37f, 24f }, new float[]{ 36f, 49f, 33f }, new float[]{ 173f, 225f, 163f });
    private final static Palette TECH_LIGHT_BLUE = Palette.ofFloat(new float[]{ 27f, 196f, 238f }, new float[]{ 62f, 205f, 240f }, new float[]{ 198f, 240f, 251f });
    private final static Palette TECH_DARK_BLUE = Palette.ofFloat(new float[]{ 3f, 31f, 38f }, new float[]{ 5f, 46f, 57f }, new float[]{ 64f, 206f, 242f });
    private final static Palette SEABREEZE = Palette.ofFloat(new float[]{ 194f, 195f, 199f }, new float[]{ 131f, 118f, 156f }, new float[]{ 28f, 43f, 83f });
    private final static Palette LIGHT_FOREST = Palette.ofFloat(new float[]{ 235f, 239f, 231f }, new float[]{ 224f, 231f, 218f }, new float[]{ 128f, 159f, 105f });
    private final static Palette WATERMELON = Palette.ofFloat(new float[]{ 3f, 29f, 9f }, new float[]{ 8f, 49f, 19f }, new float[]{ 228f, 75f, 113f});
    private final static Palette BUBBLEGUM = Palette.ofFloat(new float[]{ 255f, 235f, 251f }, new float[]{ 255, 214f, 247f }, new float[]{ 255, 115f, 227f });
    private final static Palette BLACK_AND_WHITE = Palette.ofFloat(new float[]{ 20f, 20f, 20f }, new float[]{ 31f, 31f, 31f }, new float[]{ 245f, 245f, 245f });
    private final static Palette TEAL_AND_PINK = Palette.ofFloat(new float[]{ 30f, 65f, 65f }, new float[]{ 17f, 85f, 85f }, new float[]{ 255f, 90f, 239f });
    private final static Palette PURPLE_AND_BLUE = Palette.ofFloat(new float[]{ 39f, 5f, 36f }, new float[]{ 59f, 2f, 55f }, new float[]{ 80f, 254f, 254f });

    private final static Palette CURRENT_PALETTE = WATERMELON;

    // COLON
    private final static double   COLON_WIDTH = 10*SCALE;
    private final static double   COLON_DOT_DISTANCE = 20*SCALE;
    private final static double   COLON_DOT_HEIGHT = 10*SCALE;
    private final static double   COLON_KERNING = 10*SCALE;


    /*
    No Touching
     */
    private final static int[][] NUMS = { // { Top, Middle, Bottom, Top Left, Bottom Left, Top Right, Bottom Right }
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

    private static final double CUT = (CUT_CORNERS)?THICKNESS/2.0:0;
    private final static double  WIDTH = (NUM_WIDTH*6+NUM_KERNING*3+COLON_KERNING*4+COLON_WIDTH*2+PADDING*2),
                                 HEIGHT = (NUM_HEIGHT+PADDING*2); // Width and height of screen


    public static void main(String[] args) {
        new Clock().run();
    }

    /*
    -----------------------------------------------------------------------
    Segment Rendering
    -----------------------------------------------------------------------
     */
    private static void drawTop(final GL2 gl, double xOff, double yOff){
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(xOff+THICKNESS+GAP, yOff+GAP);
        gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+GAP);
        gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+THICKNESS-GAP);
        gl.glVertex2d(xOff+THICKNESS+GAP, yOff+THICKNESS-GAP);

        gl.glEnd();
    }
    private static void drawMiddle(final GL2 gl, double xOff, double yOff){
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(xOff+THICKNESS+GAP, yOff+NUM_HEIGHT/2.0-THICKNESS/2.0+GAP);
        gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+NUM_HEIGHT/2.0-THICKNESS/2.0+GAP);
        gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+NUM_HEIGHT/2.0+THICKNESS/2.0-GAP);
        gl.glVertex2d(xOff+THICKNESS+GAP, yOff+NUM_HEIGHT/2.0+THICKNESS/2.0-GAP);

        gl.glEnd();
    }
    private static void drawBottom(final GL2 gl, double xOff, double yOff){
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(xOff+THICKNESS+GAP, yOff+NUM_HEIGHT-THICKNESS+GAP);
        gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+NUM_HEIGHT-THICKNESS+GAP);
        gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS-GAP, yOff+NUM_HEIGHT-GAP);
        gl.glVertex2d(xOff+THICKNESS+GAP, yOff+NUM_HEIGHT-GAP);

        gl.glEnd();
    }
    private static void drawTopLeft(final GL2 gl, double xOff, double yOff){
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(xOff+GAP, yOff+GAP+CUT);
        gl.glVertex2d(xOff+THICKNESS-GAP, yOff+GAP+CUT);
        gl.glVertex2d(xOff+THICKNESS-GAP, yOff+NUM_HEIGHT/2.0-GAP);
        gl.glVertex2d(xOff+GAP, yOff+NUM_HEIGHT/2.0-GAP);

        gl.glEnd();
    }
    private static void drawBottomLeft(final GL2 gl, double xOff, double yOff){
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(xOff+GAP, yOff+NUM_HEIGHT/2.0+GAP);
        gl.glVertex2d(xOff+THICKNESS-GAP, yOff+NUM_HEIGHT/2.0+GAP);
        gl.glVertex2d(xOff+THICKNESS-GAP, yOff+NUM_HEIGHT-GAP-CUT);
        gl.glVertex2d(xOff+GAP, yOff+NUM_HEIGHT-GAP-CUT);

        gl.glEnd();
    }
    private static void drawTopRight(final GL2 gl, double xOff, double yOff){
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS+GAP, yOff+GAP+CUT);
        gl.glVertex2d(xOff+NUM_WIDTH-GAP, yOff+GAP+CUT);
        gl.glVertex2d(xOff+NUM_WIDTH-GAP, yOff+NUM_HEIGHT/2.0-GAP);
        gl.glVertex2d(xOff+NUM_WIDTH-THICKNESS+GAP, yOff+NUM_HEIGHT/2.0-GAP);

        gl.glEnd();
    }
    private static void drawBottomRight(final GL2 gl, double xOff, double yOff){
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
    public static void drawNumber(int num, final GL2 gl, double xOff, double yOff){
        drawDigit(num/10, gl, xOff, yOff);
        drawDigit(num%10, gl, xOff+(NUM_WIDTH + NUM_KERNING), yOff);
    }
    public static void drawDigit(int digit, final GL2 gl, double xOff, double yOff){
        int[] segs = NUMS[digit];

        for(int i = 0; i < segs.length; i++){
            if(segs[i] == 1){
                gl.glColor3f(CURRENT_PALETTE.highlight[0], CURRENT_PALETTE.highlight[1], CURRENT_PALETTE.highlight[2]);
            }else if(segs[i] == 0 && !SHADOWS){
                continue;
            }else{
                gl.glColor3f(CURRENT_PALETTE.shadow[0], CURRENT_PALETTE.shadow[1], CURRENT_PALETTE.shadow[2]);
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
    public static void drawColon(final GL2 gl, double xOff, double yOff){
        double colonOffset = NUM_HEIGHT/2.0-COLON_DOT_DISTANCE/2.0-THICKNESS;

        gl.glColor3f(CURRENT_PALETTE.highlight[0], CURRENT_PALETTE.highlight[1], CURRENT_PALETTE.highlight[2]);

        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(xOff, yOff+colonOffset);
        gl.glVertex2d(xOff+COLON_WIDTH, yOff+colonOffset);
        gl.glVertex2d(xOff+COLON_WIDTH, yOff+colonOffset+COLON_DOT_HEIGHT);
        gl.glVertex2d(xOff, yOff+colonOffset+ COLON_DOT_HEIGHT);

        gl.glVertex2d(xOff, yOff+NUM_HEIGHT-colonOffset- COLON_DOT_HEIGHT);
        gl.glVertex2d(xOff+COLON_WIDTH, yOff+NUM_HEIGHT-colonOffset- COLON_DOT_HEIGHT);
        gl.glVertex2d(xOff+COLON_WIDTH, yOff+NUM_HEIGHT-colonOffset);
        gl.glVertex2d(xOff, yOff+NUM_HEIGHT-colonOffset);

        gl.glEnd();
    }


    private static void init(GLAutoDrawable glAutoDrawable){
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU glu = new GLU();
        glu.gluOrtho2D(0, WIDTH, HEIGHT, 0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glViewport(0, 0, (int)WIDTH, (int)HEIGHT);
        gl.glLoadIdentity();
    }


    private static class Clock implements GLEventListener, Runnable {

        @Override
        public void init(GLAutoDrawable glAutoDrawable) {
            Main.init(glAutoDrawable);
        }

        @Override
        public void dispose(GLAutoDrawable glAutoDrawable) {

        }

        @Override
        public void display(GLAutoDrawable glAutoDrawable) {
            final GL2 gl = glAutoDrawable.getGL().getGL2();

            gl.glClearColor(CURRENT_PALETTE.background[0], CURRENT_PALETTE.background[1], CURRENT_PALETTE.background[2], 1.f);
            gl.glClear (GL2.GL_COLOR_BUFFER_BIT |  GL2.GL_DEPTH_BUFFER_BIT );
            drawTime(gl, PADDING, PADDING);

            gl.glFlush();
        }

        @Override
        public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

        }

        /*
        -----------------------------------------------------------------------
        Clock Rendering
        -----------------------------------------------------------------------
         */

        public void drawTime(final GL2 gl, double xOff, double yOff){
            ZonedDateTime time = ZonedDateTime.now();
            drawNumber(time.getHour(), gl, xOff, yOff);
            drawColon(gl, xOff+NUM_WIDTH*2+NUM_KERNING+COLON_KERNING, yOff);
            drawNumber(time.getMinute(), gl, xOff+NUM_WIDTH*2+NUM_KERNING+COLON_KERNING*2+COLON_WIDTH, yOff);
            drawColon(gl, xOff+NUM_WIDTH*4+NUM_KERNING*2+COLON_KERNING*3+COLON_WIDTH, yOff);
            drawNumber(time.getSecond(), gl, xOff+NUM_WIDTH*4+NUM_KERNING*2+COLON_KERNING*4+COLON_WIDTH*2, yOff);
        }

        @Override
        public void run() {
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Clock Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize((int)WIDTH, (int)HEIGHT);

                GLProfile profile = GLProfile.get(GLProfile.GL2);
                GLCapabilities capabilities = new GLCapabilities(profile);

                final GLCanvas canvas = new GLCanvas(capabilities);
                Clock c = new Clock();
                canvas.addGLEventListener(c);
                canvas.setSize((int)WIDTH, (int)HEIGHT);

                frame.getContentPane().add(canvas);
                frame.setVisible(true);

                final FPSAnimator anim = new FPSAnimator(canvas, 20, true);
                anim.start();
            });
        }
    }

    private static class Palette {
        public float[] background;
        public float[] shadow;
        public float[] highlight;

        private Palette(float[] b, float[] s, float[] h){
            background = b;
            shadow = s;
            highlight = h;
        }

        public static Palette ofNormalFloat(float[] background, float[] shadow, float[] highlight){
            return new Palette(background, shadow, highlight);
        }

        public static Palette ofFloat(float[] background, float[] shadow, float[] highlight){
            return new Palette(
                    new float[]{ background[0] / 255f, background[1] / 255f, background[2] / 255f },
                    new float[]{ shadow[0]     / 255f, shadow[1]     / 255f, shadow[2]     / 255f },
                    new float[]{ highlight[0]  / 255f, highlight[1]  / 255f, highlight[2]  / 255f }
            );
        }

        public static Palette ofColor(Color background, Color shadow, Color highlight){
            return new Palette(
                    new float[]{ background.getRed() / 255f, background.getGreen() / 255f, background.getBlue() / 255f },
                    new float[]{ shadow.getRed()     / 255f, shadow.getGreen()     / 255f, shadow.getBlue()     / 255f },
                    new float[]{ highlight.getRed()  / 255f, highlight.getGreen()  / 255f, highlight.getBlue()  / 255f }
            );
        }
    }
}