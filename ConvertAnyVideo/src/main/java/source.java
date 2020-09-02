import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;


public class source extends JFrame implements ActionListener {

    int pX, pY;
    JFileChooser fileChooser = new JFileChooser();
    JComboBox comboExtentions;
    JTextField textInputFile = new JTextField();
    JTextField textOutputFile = new JTextField();
    JButton btnInputFile = new JButton("InputPath");
    JButton btnOutputFile = new JButton("OutputPath");
    ImageIcon iconAccept = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Accept.png")));
    ImageIcon iconDeny = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Deny.png")));
    static JButton btnConvert = new JButton();
    static JButton btnDeny = new JButton();
    ConverterThread converter;
    static boolean is_thread_running = false;

    source() {

        super("Convert Any Video");
        frameOptions();
        frame_add();
        buttonsBounds();
        buttonProperties();
        textBoxesBounds();
        textBoxesProperties();
        comboboxBounds();
        setFocusable(true);
        giveAction();
        showLoading();
        //icon must be in src>main>resources
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("play-button.png")));


        //After program running thread will not stop, thats why I stopped my process by using this.
        this.addWindowListener(new WindowAdapter() {
                                   @Override
                                   public void windowClosing(WindowEvent e) {
                                       is_thread_running=false;
                                       return;
                                   }
                               }
        );
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                pX = me.getX();
                pY = me.getY();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent me) {
                setLocation(getLocation().x + me.getX() - pX, getLocation().y + me.getY() - pY);
            }
        });
    }

    void frame_add() {
        setLayout(null);
        add(textInputFile);
        add(textOutputFile);
        add(btnInputFile);
        add(btnOutputFile);
        add(btnConvert);
        add(btnDeny);
        add(comboExtentions);

    }

    void buttonsBounds() {
        btnInputFile.setBounds(5, 20, 100, 20);
        btnInputFile.setBorder(new RoundedBorder(10)); //10 is the radius
        btnOutputFile.setBounds(5, 55, 100, 20);
        btnOutputFile.setBorder(new RoundedBorder(10)); //10 is the radius
        btnConvert.setBounds(255, 75, 50, 50);
        btnConvert.setBorder(new RoundedBorder(10)); //10 is the radius
        btnDeny.setBounds(215, 75, 50, 50);
        btnDeny.setBorder(new RoundedBorder(10)); //10 is the radius

    }

    void buttonProperties() {
        btnConvert.setFont(btnConvert.getFont().deriveFont(16f));
        btnConvert.setEnabled(false);
        btnDeny.setEnabled(false);

        //resize and put icon in button
        int offsetAccept = btnConvert.getInsets().left;
        int offsetDeny = btnDeny.getInsets().left;
        btnConvert.setIcon(resizeIcon(iconAccept, btnConvert.getWidth() - offsetAccept, btnConvert.getHeight() - offsetAccept));
        btnDeny.setIcon(resizeIcon(iconDeny, btnDeny.getWidth() - offsetDeny, btnDeny.getHeight() - offsetDeny));

        //remove background from button
        btnDeny.setOpaque(false);
        btnDeny.setBackground(new Color(255,0,0,0));
        btnDeny.setForeground(new Color(255,0,0,0));
        btnDeny.setFocusable(false);

        btnConvert.setOpaque(false);
        btnConvert.setBackground(new Color(255,0,0,0));
        btnConvert.setForeground(new Color(255,0,0,0));
        btnConvert.setFocusable(false);
    }
    private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    void textBoxesBounds() {
        textInputFile.setBounds(110, 20, 380/2, 20);
        textOutputFile.setBounds(110, 55, 380/2, 20);

    }

    void textBoxesProperties() {

        textInputFile.setEditable(false);
        textOutputFile.setEditable(false);
        textInputFile.setFocusable(false);
        textOutputFile.setFocusable(false);
    }

    void comboboxBounds() {

        comboExtentions.setBounds(5, 90, 100, 20);
        comboExtentions.setFocusable(false);
    }

    void giveAction() {
        btnInputFile.addActionListener(this);
        btnOutputFile.addActionListener(this);
        btnConvert.addActionListener(this);
        btnDeny.addActionListener(this);
    }


    void frameOptions() {
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        String convertFileExtentions[] = {"mp4", "mov"};
        comboExtentions = new JComboBox(convertFileExtentions);

    }


    void progress_bar() {
        add(pbar);
        pbar.setMinimum(0);
        pbar.setMaximum(100);
        pbar.setBounds(110, 95, 110, 7);
        pbar.setForeground(Color.green);
        pbar.setIndeterminate(false); //Progressbar will stop moving

    }

    static JProgressBar pbar = new JProgressBar();


    void showLoading() {
        progress_bar();
        pbar.setVisible(true);
    }

    public String fileWithPath(String path,String cutDelimination, boolean getExtention){
        String text = path;
        int index = text.lastIndexOf(cutDelimination);
        if(!getExtention){
            System.out.println(text.substring(0,index));
            return text.substring(0,index);
        }
        else{
            System.out.println(text.substring(index, text.length()));
            return text.substring(index, text.length());
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConvert) {
            System.out.println("."+textOutputFile.getText()+".");
            if (textOutputFile.getText().isEmpty()) {
                textOutputFile.setText(fileWithPath(textInputFile.getText(),".",false));
            }
            btnConvert.setEnabled(false);
            btnDeny.setEnabled(true);
            showLoading();

            source.pbar.setIndeterminate(true); //progressbar will move infinitely right to left
            converter = new ConverterThread(textInputFile.getText(), textOutputFile.getText(), comboExtentions.getSelectedItem().toString());
            converter.setDaemon(true);
            converter.start();
        }
        if(e.getSource() == btnDeny){
            is_thread_running=false;
            btnDeny.setEnabled(false);
        }

        String videoFormats[] = {".mov",".webm",".mpg",".mp2", ".mpeg", ".mpe", ".mpv",".ogg",".mp4",".m4p", ".m4v",".avi",".wmw", ".mov",".qt",".flv", ".swf", ".avchd"};

        if (e.getSource() == btnInputFile) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setFileSelectionMode(JFileChooser.APPROVE_OPTION);
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                textInputFile.setText(selectedFile.toString().toLowerCase());
                if (!textInputFile.getText().isEmpty()) {

                    for(int i = 0; i < videoFormats.length; i++){
                        if(fileWithPath(textInputFile.getText(), ".", true).equals(videoFormats[i])){
                            btnConvert.setEnabled(true);
                            btnDeny.setEnabled(false);
                        }
                    }
                }
            }
        }

        if (e.getSource() == btnOutputFile) {
            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            // fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFolder = fileChooser.getSelectedFile();
                textOutputFile.setText(selectedFolder.toString().toLowerCase());
            }

        }

    }
}
