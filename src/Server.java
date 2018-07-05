
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {    
    public static ArrayList <ClientHandler> clients = new ArrayList<>();
    public static ArrayList<ClientHandler> onlineUserList = new ArrayList<ClientHandler>();
   
    public static String isLoggedin(String userName, String password){
        ClientHandler c=null;
        
        for(int i=0;i<Server.clients.size();i++){        
            if(clients.get(i).getUsername().equals(userName)
                            && clients.get(i).getPassword().equals(password)){
                c=clients.get(i);
                break;
            }
        }
        if(c==null){
            return "User doesnt exist";
        }
        else{
            return "Logged in";
        }
    }
    
    public static void main(String args[]){
        try{
            ServerSocket sSocket = new ServerSocket(7777);
            while(true){
                Socket cSocket = sSocket.accept();
                InputStreamReader inFromClient = new InputStreamReader(cSocket.getInputStream());
                BufferedReader in = new BufferedReader(inFromClient);
                DataOutputStream out = new DataOutputStream(cSocket.getOutputStream());
                String msg = in.readLine();
                System.out.println(msg);
                String arr[] = msg.split(":");
               
                if(arr[0].equals("sign_in")){
                    String log = isLoggedin(arr[1], arr[2]);
                    out.writeBytes(log + '\n');
                    while(!log.equals("Logged in")){
                        msg = in.readLine();
                        System.out.println(msg);
                        arr = msg.split(":");
                        if(arr[0].equals("sign_in")){
                            log = isLoggedin(arr[1], arr[2]);
                            out.writeBytes(log + '\n');
                            ClientHandler tempClient=null;
                            for(int i=0;i<Server.clients.size();i++){
                                if(clients.get(i).getUsername().equals(arr[1])
                                    && clients.get(i).getPassword().equals(arr[2])){
                                    tempClient=clients.get(i);
                                    break;
                                }
                            }
                            onlineUserList.add(tempClient);
                            Thread th = new Thread(tempClient);
                            th.start();
                        }
                        else if(arr[0].equals("sign_up")){
                            ClientHandler ch = new ClientHandler(arr[1],arr[2],cSocket);
                            clients.add(ch);
                            System.out.println("Registered " + arr[1]);
                            log = "not logged";
                        }
                    }
                }
            }
            
        }catch(Exception e){
            
        }  
    }
}
