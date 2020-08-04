

import java.io.IOException;
import java.io.Serializable;

public class Node implements Serializable{

    private int id; // node id
    private String address;
    private int port;

    public Node(String add, int port){
        this.address = add;
        this.port = port;
    }
    

    public Node(int i, String ip, int p){
        this.id = i;
        this.address = ip;
        this.port = p;
    }

    public int getID(){
        return this.id;
    }

    public String getAddress(){
        return this.address;
    }

    public int getPort(){
        return this.port;
    }

    public void setID(int id){
        this.id = id;
    }

    // public void setaddress(String ipaddr){
    //     this.ipaddr = ipaddr;
    // }

    public void setPort(int port){
        this.port = port;
    }

    public String toString() {
        return this.getID() + "" ;
    }

}