import java.io.InputStream;

//This will show every line of output of terminal if it will be live 
class InputHandler extends Thread {

    InputStream input_;
    
    InputHandler(InputStream input, String name) {
    super(name);
    input_ = input;
    }
    
    public void run() {
    try {
    int c;
    while ((c = input_.read()) != -1) {
    System.out.write(c);
    }
    } catch (Throwable t) {
    t.printStackTrace();
    }
    }
    
    }
    
    