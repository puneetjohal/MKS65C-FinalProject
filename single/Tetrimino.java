import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
public class Tetrimino{

    //An array that stores the shapes as 3d arrays
    private int[][][][] shapes;

    //Each shape is its own 3d array that stores the configuration of each shape as 1s and 0s, the shape's unique number for the coordTable in the Board class, and its color
    private int[][][] I;
    private int[][][] O;
    private int[][][] T;
    private int[][][] S;
    private int[][][] Z;
    private int[][][] J;
    private int[][][] L;

    //Used to randomly pick a shape
    private ArrayList<Integer> shapeGen;

    //Used to remake the shapeGen array list
    private Integer[] newGen;

    //Creates each 3d array for the shapes, adds them to the shapes array, and creates the randGen arraylist and newGen array in order to randomly pick shapes
    public Tetrimino(){
	I = new int[][][] {
	    {
		{1,1,1,1}
	    },
	    {
		{1},
		{1},
		{1},
		{1}
	    },
	    {
		{1}
	    },
	    {
		{Color.CYAN.getRGB()}
	    }
	};
	O = new int[][][]{
	    {
		{1,1},
		{1,1}
	    },
	    {
		{2}
	    },
	    {
		{Color.YELLOW.getRGB()}
	    }
	};
	T = new int[][][]{
	    {
		{0,1,0},
		{1,1,1}
	    },
	    {
		{1,0},
		{1,1},
		{1,0},
	    },
	    {
		{1,1,1},
		{0,1,0}
	    },
	    {
		{0,1},
		{1,1},
		{0,1},
	    },
	    {
		{3}
	    },
	    {
		{Color.MAGENTA.getRGB()}
	    }
	};
	S = new int[][][] {
	    {
		{0,1,1},
		{1,1,0}
	    },
	    {
		{1,0},
		{1,1},
		{0,1}
	    },
	    {
		{4}
	    },
	    {
		{Color.GREEN.getRGB()}
	    }   
	};
	Z = new int[][][] {
	    {
		{1,1,0},
		{0,1,1}
	    },
	    {
		{0,1},
		{1,1},
		{1,0}
	    },
	    {
		{5}
	    },
	    {
		{Color.RED.getRGB()}
	    }
	};
	J = new int[][][] {
	    {
		{1,0,0},
		{1,1,1}
	    },
	    {
		{1,1},
		{1,0},
		{1,0},
	    },
	    {
		{1,1,1},
		{0,0,1}
	    },
	    {
		{0,1},
		{0,1},
		{1,1}
	    },
	    {
		{6}
	    },
	    {
		{Color.BLUE.getRGB()}
	    }
	};
	L = new int[][][] {
	    {
		{0,0,1},
		{1,1,1}
	    },
	    {
		{1,0},
		{1,0},
		{1,1}
	    },
	    {
		{1,1,1},
		{1,0,0}
	    },
	    {
		{1,1},
		{0,1},
		{0,1}
	    },
	    {
		{7}
	    },
	    {
		{Color.ORANGE.getRGB()}
	    }
	};
	shapes = new int[][][][] {I,O,T,S,Z,J,L};
	newGen = new Integer[] {0,0,1,1,2,2,3,3,4,4,5,5,6,6};
	shapeGen = new ArrayList<Integer> ();
    }

    //Used by the Board class to determine where each square of the piece should be when painting and moving, returns 1 or 0
    public int getSquare(int[][][] shape,int orientation, int x,int y){
	return shape[orientation][x][y];
    }
    
    //Used by the Board class to determine where each square of the piece should be when painting and moving, returns the width of the shape
    public int getWid(int[][][] shape, int orientation){
	return shape[orientation][0].length;
    }

    //Used by the Board class to determine where each square of the piece should be when painting and moving, returns the length of the shape
    public int getLen(int[][][] shape, int orientation){
	return shape[orientation].length;
    }

    //Returns the color of the shape being painted
    public Color getCol(int[][][] shape){
	return new Color(shape[shape.length-1][0][0]);
    }

    //Returns the color of the shapes on the coordTable
    public Color getCol(int shape){
	return getCol(shapes[shape-1]);
    }

    //Returns the unique number of the piece
    public int getNum(int[][][] shape){
	return shape[shape.length-2][0][0];
    }

    //Returns the number of orientations the shape has
    public int getOris(int[][][] shape){
	return shape.length-2;
    }

    //Returns the 3d array of a random shape
    public int[][][] randGen(){
	//If the whole list of numbers for generating random shapes is used up, refill it with numbers
	if(shapeGen.size() == 0){
	    shapeGen.addAll(Arrays.asList(newGen));
	}
	return shapes[shapeGen.remove((int)(Math.random()*shapeGen.size()))];
    }
}    
 
    
