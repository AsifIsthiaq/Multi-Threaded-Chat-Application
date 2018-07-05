import java.io.*;
import java.net.*;
import java.util.*;

public class ClientWriter implements Runnable{
    private OutputStream stream;
    
    public ClientWriter(OutputStream stream) {
        this.stream = stream;
    }
    
    public void run(){
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream out = new DataOutputStream(stream);
        try{
            while(true){
                String msg = keyRead.readLine();
                out.writeBytes(msg + '\n');
            }
        }catch(Exception e){
            
        }
    }

    public OutputStream getStream() {
        return stream;
    }

    public void setStream(OutputStream stream) {
        this.stream = stream;
    }
}
