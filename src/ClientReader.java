import java.io.*;

public class ClientReader implements Runnable{
    private InputStream stream;

    public ClientReader(InputStream stream) {
        this.stream = stream;
    }
    public void run(){
        InputStreamReader inStream = new InputStreamReader(stream);
        BufferedReader in = new BufferedReader(inStream);
        while(true){
            try{
                String str = in.readLine();
                System.out.println(str);
            }catch(Exception e){
                
            }  
        }
    }
    
    public InputStream getStream() {
        return stream;
    }
    
    public void setStream(InputStream stream) {
        this.stream = stream;
    }
}
