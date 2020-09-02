
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class loading_frame extends JFrame implements ActionListener {



    loading f;



    public loading_frame() {

        process();

        create_frame();


        f.dispose();
        setVisible(true);
    }



    void create_frame(){

        try {
            java.lang.Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        this.setLayout(null);
        setSize(500,250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        //setUndecorated(true);
        //setOpacity(0.7f);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);





    }







    void process() {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                f = new loading();
            }
        });

/*
		for (int i = 0; i <= 100; i++) {
			final int percent = i;

			try {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
					loading_frame.updateBar(percent);
					}
				});


				java.lang.Thread.sleep(100);


			} catch (InterruptedException e) {

			}
		}

		f.dispose();
	*/
    }








    public void actionPerformed(ActionEvent e) {


    }

}
