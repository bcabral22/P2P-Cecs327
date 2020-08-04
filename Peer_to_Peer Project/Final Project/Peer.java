

import java.net.*;
import java.util.Scanner;

// import jdk.internal.net.http.common.Utils.ServerName;

import java.io.*;

public class Peer{

    static String serverName = "localhost"; // 192.168.254.22
    static int port = 6601;

    public static void main(String[] args) {

        Chord myChord = new Chord("localhost", 6601);
        // Chord myClient = new Chord("localhost", "5000", "localhost", 6601 );




        // Chord myClient2 = new Chord("localhost", "6000", "localhost", "6601" );

    }
    
}