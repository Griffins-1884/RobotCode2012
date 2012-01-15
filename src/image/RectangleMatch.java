package image;

import com.sun.cldc.jna.*;

/**
 * All of the below code was borrowed from WPILibJ/src/edu/wpi/first/wpilibj/image/EllipseMatch.java. As per the license, the FIRST BSD license is in this package folder.
 */
public class RectangleMatch {

    static final int rectangleMatchSize = 64;
    public Position[] m_corner = new Position[4];
    public double m_rotation;
    public double m_width;
    public double m_height;
    public double m_score;

    public class Position {
    	public double m_xPos, m_yPos;
    	public Position(double m_xPos, double m_yPos) {
    		this.m_xPos = m_xPos;
    		this.m_yPos = m_yPos;
    	}
    }
    private class RectangleMatchStructure extends Structure {

        public RectangleMatchStructure(int address) {
            useMemory(new Pointer(address, rectangleMatchSize));
            read();
        }

        public void read() {
            m_corner[0] = new Position(backingNativeMemory.getFloat(0), backingNativeMemory.getFloat(4));
            m_corner[0] = new Position(backingNativeMemory.getFloat(8), backingNativeMemory.getFloat(12));
            m_corner[0] = new Position(backingNativeMemory.getFloat(16), backingNativeMemory.getFloat(20));
            m_corner[0] = new Position(backingNativeMemory.getFloat(24), backingNativeMemory.getFloat(28));
            m_rotation = backingNativeMemory.getDouble(32);
            m_width = backingNativeMemory.getDouble(40);
            m_height = backingNativeMemory.getDouble(48);
            m_score = backingNativeMemory.getDouble(56);

        }

        public void write() {
        }

        public int size() {
            return rectangleMatchSize;
        }
    }

    RectangleMatch(int address) {
        new RectangleMatchStructure(address);
    }

    public String toString() {
        return "Rectangle Match:\n" +
                "  Corner 1 pos x: " + m_corner[0].m_xPos + "  y: " + m_corner[0].m_xPos + "\n" +
                "  Corner 1 pos x: " + m_corner[1].m_xPos + "  y: " + m_corner[1].m_xPos + "\n" +
                "  Corner 1 pos x: " + m_corner[2].m_xPos + "  y: " + m_corner[2].m_xPos + "\n" +
                "  Corner 1 pos x: " + m_corner[3].m_xPos + "  y: " + m_corner[3].m_xPos + "\n" +
                "  Width: " + m_width + " height: " + m_height + "\n" +
                "  Rotation: " + m_rotation + " Score: " + m_score + "\n";
    }

    protected static RectangleMatch[] getMatchesFromMemory(int address, int number) {
        if (address == 0) {
            return new RectangleMatch[0];
        }

        RectangleMatch[] toReturn = new RectangleMatch[number];
        for (int i = 0; i < number; i++) {
            toReturn[i] = new RectangleMatch(address + i * rectangleMatchSize);
        }
        return toReturn;
    }
}