package image;

import com.sun.cldc.jna.*;

/**
 * All of the below code was borrowed from WPILibJ/src/edu/wpi/first/wpilibj/image/EllipseDescriptor.java. As per the license, the FIRST BSD license is in this package folder.
 */
public class RectangleDescriptor extends Structure {

    double m_minWidth;
    double m_maxWidth;
    double m_minHeight;
    double m_maxHeight;

    public RectangleDescriptor(double m_minWidth, double m_maxWidth, double m_minHeight, double m_maxHeight) {
        this.m_minWidth = m_minWidth;
        this.m_maxWidth = m_maxWidth;
        this.m_minHeight = m_minHeight;
        this.m_maxHeight = m_maxHeight;
        allocateMemory();
        write();
    }

    /**
     * Free the c memory associated with this object.
     */
    public void free() {
        release();
    }

    public void read() {
        m_minWidth = backingNativeMemory.getDouble(0);
        m_maxWidth = backingNativeMemory.getDouble(8);
        m_minHeight = backingNativeMemory.getDouble(16);
        m_maxHeight = backingNativeMemory.getDouble(24);
    }

    public void write() {
        backingNativeMemory.setDouble(0, m_minWidth);
        backingNativeMemory.setDouble(8, m_maxWidth);
        backingNativeMemory.setDouble(16, m_minHeight);
        backingNativeMemory.setDouble(24, m_maxHeight);

    }

    public int size() {
        return 32;
    }
}