import java.io.IOException;
import javax.swing.JOptionPane;

public class ConverterThread extends Thread{
    String pathInput, pathOutput, extention;
    public ConverterThread(String pathInput, String pathOutput, String extention){
        this.pathInput =pathInput;
        this.pathOutput=pathOutput;
        this.extention=extention;

    }
    @Override
    public void run() {
        try {
            source.is_thread_running = true;
            ffmpegCommand2(pathInput, pathOutput, extention);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "ffmpegCommand2 methods have problem", "Error!", 1);
        }

        source.btnConvert.setEnabled(true);
        source.btnDeny.setEnabled(false);
        source.pbar.setIndeterminate(false);
    }


    public void ffmpegCommand2(String input, String output, String extentions) throws IOException {

        Process process = Runtime.getRuntime().exec("ffmpeg -i "+input+" -y " + output+"."+extentions);
        InputHandler errorHandler = new InputHandler(process.getErrorStream(), "Error Stream");
        errorHandler.start();
        InputHandler inputHandler = new InputHandler(process.getInputStream(), "Output Stream");
        inputHandler.start();
        try {
            //There is problem while controlling Thread, thats why my own code is help for that.
//    process.waitFor();
            while(true){
                Thread.sleep(4000);
                if(!(process.isAlive() && source.is_thread_running)){
                    source.is_thread_running=false;
                    process.destroy();
                    process.destroyForcibly();
                    source.btnConvert.setEnabled(true);
                    break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "process.waitFor() error", "Error!", 1);
            throw new IOException("process interrupted");
        }
    }
}
