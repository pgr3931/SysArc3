package solution;

import com.sun.media.jai.widget.DisplayJAI;

import javax.media.jai.PlanarImage;
import javax.swing.*;
import java.awt.*;

class Window {

    static void show(PlanarImage image) {
        JFrame frame = new JFrame();
        frame.setTitle("DisplayJAI");

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        DisplayJAI dj = new DisplayJAI(image);
        contentPane.add(new JScrollPane(dj), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setVisible(true);
    }
}
