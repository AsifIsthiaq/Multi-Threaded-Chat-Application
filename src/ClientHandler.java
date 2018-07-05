import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable{
    private String username;
    private String password;
    private Socket socket;
    
    public ArrayList <ClientHandler> friendList = new ArrayList<>();
    
    public ClientHandler(String username, String password, Socket socket) {
        this.username = username;
        this.password = password;
        this.socket = socket;
    }
    
    public void run(){
        try{
            System.out.println(username + " logged in");
            InputStreamReader inFromClient = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(inFromClient);
            while(true){
                String str = in.readLine();
                String arr[] = str.split(":"); 
                
                if(arr[0].equals("cmd") && arr[1].equals("logout")){
                    break;
                }
                else if(arr[0].equals("cmd") && arr[1].equals("show_list")){
                    ShowList();
                }
                else if(arr[0].equals("cmd") && arr[1].equals("show_friend_list")){
                    ShowFriendList();
                }
                else if(arr[0].equals("cmd") && arr[1].equals("add_to_friend_list")){
                    ClientHandler ch = FindUser(arr[2]); 
                    if(ch!=null){
                        friendList.add(ch);
                    }
                }
                else if(arr[0].equals("add_friend")){
                    AddAsFriend(arr[1]);
                }
                else if(arr[0].equals("accept")){
                    AcceptAsFriend(arr[1]);
                }
                else if(arr[0].equals("deny")){
                    DenyAsFriend(arr[1]);
                }
                if(arr[0].equals("msg")){
                    SendMessage(arr);
                }
                else if(arr[0].equals("broadcast")){
                    Broadcast(arr[1]);
                }
            }
            socket.close();
        }catch(Exception e){
            
        }
    }
     
    public ClientHandler FindOnlineUser(String str){
        ClientHandler ch=null;
        for(int i=0;i<Server.onlineUserList.size();i++){        
            if(Server.onlineUserList.get(i).getUsername().equals(str)){
                ch=Server.onlineUserList.get(i);
                break;
            }
        }
        if(ch==null){
            return null;
        }
        else{
            return ch;
        }
    }
    
    public ClientHandler FindUser(String str){
        ClientHandler ch=null;
        for(int i=0;i<Server.clients.size();i++){        
            if(Server.clients.get(i).getUsername().equals(str)){
                ch=Server.clients.get(i);
                break;
            }
        }
        if(ch==null){
            return null;
        }
        else{
            return ch;
        }
    }

    public void AddAsFriend(String str){
        try{
            ClientHandler ch = FindUser(str);
            DataOutputStream out = new DataOutputStream(ch.getSocket().getOutputStream());
            out.writeBytes("Friend Request:" + this.getUsername() + '\n');
        }catch(Exception e){
            
        }
    }
    
    public void AcceptAsFriend(String str){
        try{
            ClientHandler ch = FindUser(str);
            DataOutputStream out = new DataOutputStream(ch.getSocket().getOutputStream());
            out.writeBytes("Friend Request Accepted By:" + this.getUsername() + '\n');
            friendList.add(ch);
            ch.friendList.add(this);
        }catch(Exception e){
            
        }
    }
    public void DenyAsFriend(String str){
        try{
            ClientHandler ch = FindUser(str);
            DataOutputStream out = new DataOutputStream(ch.getSocket().getOutputStream());
            out.writeBytes("Friend Request Denied By:" + this.getUsername() + '\n');
        }catch(Exception e){
            
        }
    }
    
    public void Broadcast(String str){
        try{
            DataOutputStream out;
            for(int i=0; i<Server.onlineUserList.size(); i++){
                if(this!=Server.onlineUserList.get(i)){
                    out = new DataOutputStream(Server.onlineUserList.get(i).getSocket().getOutputStream());
                    out.writeBytes(this.getUsername()+ ":" + str + '\n');
                }
            }
        }catch(Exception e){
            
        }
    }
    
    public void ShowList(){
        try{
            DataOutputStream out = new DataOutputStream(this.getSocket().getOutputStream());
            String userListStr="";
            for(int i=0;i<Server.onlineUserList.size();i++){
                    userListStr= userListStr+Server.onlineUserList.get(i).getUsername()+"  "+(i+2)+".";
            }
            out.writeBytes("Online Users:"+"1."+userListStr.substring(0,userListStr.length()-2)+'\n');
                              
        }catch(Exception e){
            
        }
    }
    
    public void ShowFriendList(){
        try{
            DataOutputStream out = new DataOutputStream(this.getSocket().getOutputStream());
            String userListStr="";
            for(int i=0;i<friendList.size();i++){
                    userListStr= userListStr+friendList.get(i).getUsername()+"  "+(i+2)+".";
            }
            out.writeBytes("Friend List:"+"1."+userListStr.substring(0,userListStr.length()-2)+'\n');
                              
        }catch(Exception e){
            
        }
    }
    
    public void SendMessage(String s[]){
        int len = s.length;
        try{
            DataOutputStream out;
            for(int i=2; i<len; i++){
                ClientHandler ch = FindOnlineUser(s[i]);
                if(ch==null){
                    return;
                }
                out = new DataOutputStream(ch.getSocket().getOutputStream());
                out.writeBytes(this.getUsername()+ ":" + s[1] + '\n');
            }
        }catch(Exception e){
            
        }    
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    
}
