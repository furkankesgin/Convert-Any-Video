import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

    static String output1="";
    static int durationOfVideo=0;
    public void ffmpegCommand2(String input, String output, String extentions) throws IOException {

        System.out.println(input);
        try{
            Process process1 = Runtime.getRuntime().exec("ffprobe -i \""+input+"\" -show_entries format=duration -v quiet -of csv=\"p=0\" -sexagesimal");

        output1 = new String();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process1.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output1 = line;
        }

        int exitVal = process1.waitFor();
        if (exitVal == 0) {
            System.out.println("output1: "+output1);
            process1.destroy();
            process1.destroyForcibly();
            int dottedValue = 1;
            try {
                dottedValue = Math.round(Float.parseFloat(output1.substring(output1.lastIndexOf(":") + 1)));
                output1 = output1.substring(0, output1.lastIndexOf(":")+1) + dottedValue;
                durationOfVideo = InputHandler.getSecondsFromFormattedDuration(output1);
                System.out.println("DurationOfVideo"+durationOfVideo);
            }catch (Exception e){

                e.printStackTrace();
            }
        } else {
            //abnormal...
            System.out.println("gasdfdasf");
        }

    } catch (IOException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

        Process process = Runtime.getRuntime().exec("ffmpeg -i "+"\""+input+"\""+" -y " + "\""+output+"."+extentions+"\"");
        InputHandler errorHandler = new InputHandler(process.getErrorStream(), "Error Stream");
        errorHandler.start();
        InputHandler inputHandler = new InputHandler(process.getInputStream(), "Output Stream");
        inputHandler.start();
        try {
            //There is problem while controlling Thread, thats why my own code is help for that.
//    process.waitFor();
            while(true){
                Thread.sleep(100);
                if(!(process.isAlive() && source.is_thread_running)){
                    source.is_thread_running=false;

                    process.destroy();
                    process.destroyForcibly();
                    source.btnConvert.setEnabled(true);
                    source.pbar.setValue(100);
                    break;
                }
            }

            if(source.btnDenyTrigger) {
                source.btnDenyTrigger=false;
                File myObj = new File(source.textOutputFile.getText() + "."+source.comboExtentions.getSelectedItem().toString());
                String fullPath = source.textOutputFile.getText() + "."+source.comboExtentions.getSelectedItem().toString();

//                JOptionPane.showMessageDialog(null,myObj.getName());

                if (myObj.exists()) {
                    int inputConfirm = JOptionPane.showConfirmDialog(null,"Do you want to delete canceled file.");
                    if(inputConfirm==0)
                        if (myObj.delete()) {
                            JOptionPane.showMessageDialog(null, "Deleted the file: " + myObj.getName());
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to delete the file.");
                        }
                }


            }

        } catch (Exception e) {

            source.btnDenyTrigger=false;
            JOptionPane.showMessageDialog(null, "process.waitFor() error", "Error!", 1);
            throw new IOException("process interrupted");
        }
    }
}
