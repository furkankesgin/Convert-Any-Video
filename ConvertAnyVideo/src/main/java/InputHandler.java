import java.io.InputStream;
import java.util.regex.Pattern;

//This will show every line of output of terminal if it will be live 
class InputHandler extends Thread {

    InputStream input_;
    static boolean isStringReady = false;
    boolean timeFound = false;
    String timeString = "aaaaa";
    static String time="";
    static int percentShow = 0;
    int counter=0;
    String [] timeFinder = {"a","a","a","a","a"};
    InputHandler(InputStream input, String name) {
    super(name);
    input_ = input;
    }

    public void shiftArray(){
        for(int i =1; i < timeFinder.length; i++){
            timeFinder[i-1]=timeFinder[i];
        }
    }

    public static int getSecondsFromFormattedDuration(String duration){
        if(duration==null)
            return 0;
        try{

            Pattern patternDuration = Pattern.compile("\\d+(?::\\d+){0,2}");

            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            if(patternDuration.matcher(duration).matches()){
                String[] tokens = duration.split(":");
                if(tokens.length==1){
                    seconds = Integer.parseInt(tokens[0]);
                }else if(tokens.length == 2){
                    minutes = Integer.parseInt(tokens[0]);
                    seconds = Integer.parseInt(tokens[1]);
                }else{
                    hours = Integer.parseInt(tokens[0]);
                    minutes = Integer.parseInt(tokens[1]);
                    seconds = Integer.parseInt(tokens[2]);
                }

                return 3600 * hours + 60 * minutes + seconds;
            }else
                return 0;

        }catch (NumberFormatException ignored){
            return 0;
        }

    }
    
    public void run() {
    try {
    int c;
        String str = null;

//time=00:00:01.96
        while ((c = input_.read()) != -1) {
//    System.out.println(c);
        str = Character.toString((char) c);
//            System.out.print(str);

//            timeString = timeString.substring(1);
//            timeString = timeString+str;
//            System.out.println(timeString+"newwwwwwwwwwwwwwwww");
            shiftArray();
            timeFinder[4] = str;


            if (timeFound){
                if(counter==8){
                    timeFound = false;
                    isStringReady= true;
                    counter = 0;
                    int currentTime = getSecondsFromFormattedDuration(time);
                    int seconds = ConverterThread.durationOfVideo-currentTime;
                    percentShow = 100 * currentTime/ConverterThread.durationOfVideo;
                    source.pbar.setValue(InputHandler.percentShow);

                    System.out.println("percentageShow : "+percentShow);
                source.label.setText(percentShow+"%");
                }

                else if (counter == 0){

                    time = str;
                    isStringReady = false;
//                    System.out.println("sifir oldu");
                    counter++;


                }else{
                    time += str;
                    counter++;

                }
            }
            if(timeFinder[0].equals("t") && timeFinder[1].equals("i") && timeFinder[2].equals("m") && timeFinder[3].equals("e") && timeFinder[4].equals("=")){
                timeFound = true;
            }
//            System.out.println(timeFinder[0]+" "+timeFinder[1]+" "+timeFinder[2]+" "+timeFinder[3]+" "+timeFinder[4]);

////            System.out.println(timeString);
//            System.out.println(timeString);

        }


    } catch (Throwable t) {
    t.printStackTrace();
    }
    }
    
    }
    
    