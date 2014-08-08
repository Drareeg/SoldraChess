/*
 * The MIT License
 *
 * Copyright 2014 Dries Weyme.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package Networking;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Dries
 */
public class Client {
    private Socket connection;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    public Client(){
        try{
         connection = new Socket(InetAddress.getLocalHost().getHostName(), 1234);
         oos = new ObjectOutputStream(connection.getOutputStream());
         oos.flush();
         ois = new ObjectInputStream(connection.getInputStream());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void requestLobby(){
        String response = null;
        try{
            oos.writeObject("Geeft mij nekeer de lobbylist manneken");
            System.out.println("CLIENT: Ik wil de lobby list!");
            do{
                response = (String) ois.readObject();
                System.out.println("Server: "+response);
                System.out.println("Client: Thanks server!");
            }while(response.equals(null));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}