
package pkg3d_labirint;

import java.util.Random;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

public class Main1 extends Application{
    private static final int HORIZONTAL = 1;
    private static final int VERTICAL = 2;

    static final int S = 1;
    static final int E = 2;
    private final Random rand = new Random();
    /*
    I got this algorithm from: https://stackoverflow.com/questions/49000632/how-to-implement-recursive-division-in-java
    */
    private void divide(int[][] grid, int x, int y, int width, int height, int orientation) {
        if(width < 2 || height < 2) {
            return;
        }
        boolean horizontal = orientation == HORIZONTAL;

        int wx = x + (horizontal ? 0 : rand.nextInt(width-1));
        int wy = y + (horizontal ? rand.nextInt(height-1) : 0);

        int px = wx + (horizontal ? rand.nextInt(width) : 0);
        int py = wy + (horizontal ? 0 : rand.nextInt(height));

        int dx = horizontal ? 1 : 0;
        int dy = horizontal ? 0 : 1;



        int length = horizontal ? width : height;

        int dir = horizontal ? S : E;

        for(int i = 0; i < length; i++) {
            if(wx != px || wy != py) {
                grid[wy][wx] |= dir;
            }
            wx += dx;
            wy += dy;
        }

        int nx = x;
        int ny = y;
        int w = horizontal ? width : wx - x + 1;
        int h = horizontal ? wy - y + 1 : height;
        divide(grid, nx, ny, w, h, chooseOrientation(w, h));

        nx = horizontal ? x : wx + 1;
        ny = horizontal ? wy + 1 : y;
        w = horizontal ? width : x + width - wx - 1;
        h = horizontal ? y + height - wy - 1 : height;
        divide(grid, nx, ny, w, h, chooseOrientation(w, h));
    } 
    private void displayC(int[][] grid){
        boolean bottom;
        boolean s;
        boolean south;
        boolean south2;
        boolean east;
        for(int i = 0;i<grid.length;i++){
            System.out.print("|");
            for(int j = 0;j<grid[i].length;j++){
                bottom = i+1>=grid.length;
                int x = grid[i][j] & S;
                south = (((grid[i][j] & S) != 0) || bottom);
                south2 = (((j+1<grid[i].length) && ((grid[i][j+1] & S) != 0)) || bottom);
                east = (((grid[i][j] & E) != 0) || (j+1>=grid[i].length));
                
                
                System.out.print(south ? "_" : " ");
                System.out.print(east ? "|" : ((south && south2) ? "_" : " "));
            }
            System.out.println();
        }
    
    }
    private int chooseOrientation(int w, int h) {
        if(w < h) {
            return HORIZONTAL;
        } else if (h < w) {
            return VERTICAL;
        } else {
            return rand.nextInt(2) + 1;
        }
    }
    private void consolNums(int[][] grid){
        for(int i = 0;i<10;i++){
         for(int j = 0;j<10;j++){
            System.out.print(grid[i][j] + " ");
        }
         System.out.println();
        }
    }
    private void display(Group root,int[][] grid){
        root.setTranslateX(50);
        root.setTranslateY(50);
        /*==StartStop==*/
        StartStop(root);
        /*==Walls==*/
        createWalls(root,grid);
        /*== Grid ==*/
        //createGrid(root);
        /*==Box==*/
        createBox(root);

    }
    private void StartStop(Group root){
        Rectangle r;
        r = new Rectangle(0,0,50,50);
        r.setFill(Color.GREEN);
        root.getChildren().add(r);
        r = new Rectangle(450,450,50,50);
        r.setFill(Color.RED);
        root.getChildren().add(r);
    }
    private void createWalls(Group root,int[][] grid){
        Line l;    
        for(int i = 0;i<grid.length;i++){
            for(int j = 0;j<grid[0].length;j++){
                if(grid[i][j] == 1){
                    l = new Line(j*50,i*50+50,j*50+50,i*50+50);
                    l.setStrokeWidth(3);
                    root.getChildren().add(l);
                }else if(grid[i][j] == 2){
                    l = new Line(j*50+50,i*50,j*50+50,i*50+50);
                    l.setStrokeWidth(3);
                    root.getChildren().add(l);
                }else if(grid[i][j] == 3){
                    l = new Line(j*50,i*50+50,j*50+50,i*50+50);
                    l.setStrokeWidth(3);
                    root.getChildren().add(l);
                    l = new Line(j*50+50,i*50,j*50+50,i*50+50);
                    l.setStrokeWidth(3);
                    root.getChildren().add(l);
                }
            }
        } 
    }
    private void createGrid(Group root){
        Line l;    
        for(int i = 0;i<=10;i++){
            l = new Line(i*50,0,i*50,500);
            l.setStrokeWidth(1);
            l.setStroke(Color.GREY);
            root.getChildren().add(l);
        }
        for(int i = 0;i<=10;i++){
            l = new Line(0,i*50,500,i*50);
            l.setStrokeWidth(1);
            l.setStroke(Color.GREY);
            root.getChildren().add(l);
        }
    }
    private void createBox(Group root){
        box(root,0,0,500,0);
        box(root,0,0,0,500);
        box(root,0,500,500,500);
        box(root,500,0,500,500);
    }
    private void box(Group root, int x1,int y1,int x2,int y2){
        Line l = new Line(x1,y1,x2,y2);
        l.setStrokeWidth(3);
        root.getChildren().add(l);
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        int size = 500;
        Group root = new Group();
        Scene scene = new Scene(root, 600, 600);
        Canvas canvas = new Canvas(400, 200);
        
        int s = 10;
        int scale = size/10;
        int[][] grid = new int[s][s];
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                KeyCode key = e.getCode();
                if(key == KeyCode.ENTER){
                            divide(grid,0,0,s,s,chooseOrientation(s,s));
                            display(root,grid);
                }

                    
            }
        });
        //displayC(grid);
        //cStevila(grid);
        //drawPlayer(root,xp,yp);
        divide(grid,0,0,s,s,chooseOrientation(s,s));
        display(root,grid);
        primaryStage.setTitle("3D Labirint");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
