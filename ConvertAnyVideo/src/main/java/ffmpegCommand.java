import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CyclicBarrier;

public class ffmpegCommand extends Thread {

    public ffmpegCommand(String input, String output, String extentions) {

        System.out.println("Started");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("ffmpeg", "-i", input, "-y",output + "." + extentions);
        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        try (var reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {


        } catch (IOException ioException) {
            ioException.printStackTrace();
        }





    }

}

