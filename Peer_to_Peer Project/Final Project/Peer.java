

import java.net.*;
import java.util.Scanner;

// import jdk.internal.net.http.common.Utils.ServerName;

import java.io.*;

public class Peer{

    static String serverName = "localhost"; // 192.168.254.22
    static int port = 6601;

    public static void main(String[] args) {

        Chord myChord = new Chord("localhost", 6601);
    }
    
}