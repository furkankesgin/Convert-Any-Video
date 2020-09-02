
import java.awt.*;


import javax.swing.*;

public class loading extends JFrame{

    //color
    Color white = new Color(255,255,255);
    Color light_black = new Color(61,61,61);
    Color black = new Color(0,0,0);
    Color green = new Color(108,254,0);
    Color blue = new Color(0,150,255);


    Color background_color = light_black;


    static JProgressBar pbar = new JProgressBar();

    JLabel label = new JLabel();

    static int MINIMUM = 0;
    static int MAXIMUM = 100;

    int size_x = 250;
    int size_y = 100;

    String text = "loading...";



    public loading() {
        labels();
        progress_bar();

    }






    void create_frame(){
        this.setLayout(null);
        this.getContentPane().setBackground(background_color);
        //this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(("42697-fire-icon.png"))));

        setSize(size_x,size_y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setOpacity(0.7f);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }



    void labels() {
        add(label);
        label.setBounds(size_x / 3,size_y / 5,250,20);
        label.setFont(new Font("arial",Font.BOLD,20));
        label.setForeground(white);
        label.setText(text);
    }


    void progress_bar() {
        add(pbar);
        pbar.setMinimum(MINIMUM);
        pbar.setMaximum(MAXIMUM);
        pbar.setBounds(size_x / 5,size_y / 2,150,10);
        pbar.setBackground(background_color);
        pbar.setForeground(green);
        pbar.setIndeterminate(true);
    }



    public static void updateBar(int newValue) {
        pbar.setValue(newValue);
    }




}
