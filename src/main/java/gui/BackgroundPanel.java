package gui;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel
{
    private  Image backgroundImage;
    
    public BackgroundPanel()
    {
        //backgroundImage = new ImageIcon(getClass().getResource("/images/bg-plain-oversize.png")).getImage();
        var url = getClass().getResource("/images/bg-oversize.png");
        
        System.out.println("IMAGE NULL BOOO!! = " + url);
        
        if (url != null)
        {
            backgroundImage = new ImageIcon(url).getImage();
        }
        
        setLayout(new java.awt.BorderLayout());
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
