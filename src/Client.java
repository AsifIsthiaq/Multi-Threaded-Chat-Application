import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String args[]){
        try{
            BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
            Socket s = new Socket();
            Socket soc = new Socket("localhost", 7777);
            String str;
            while(true){
                System.out.println("UserName:Password->");
                str = keyRead.readLine();
                DataOutputStream out = new DataOutputStream(soc.getOutputStream());
                InputStreamReader inStream = new InputStreamReader(soc.getInputStream());
                BufferedReader in = new BufferedReader(inStream);
                out.writeBytes("sign_in:" + str +'\n');
                String response = in.readLine();
                if(response.equals("Logged in")){
                    System.out.println("SuccessFully Logged In.");
                    break;
                }
                else if(response.equals("User doesnt exist")){
                    out.writeBytes("sign_up:" + str + '\n');
                    System.out.println("Registation Procedure Complete.");
                }
            }
            ClientReader cr = new ClientReader(soc.getInputStream());
            Thread th = new Thread(cr);
            th.start();
            ClientWriter cw = new ClientWriter(soc.getOutputStream());
            Thread th2 = new Thread(cw);
            th2.start();
        }catch(Exception e){  
            
        } 
    }
}
