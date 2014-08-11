/*
 * The MIT License
 *
 * Copyright 2014 Dries Weyme & Geerard Ponnet.
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

import Shared.Networking.Message;
import UI.DomainEntryPoint;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Geerard
 */
public class ListenToServerThread extends Thread {

    ObjectInputStream ois;

    ListenToServerThread(ObjectInputStream ois) {
        this.ois = ois;
    }

    public void run() {
        DomainEntryPoint dep = DomainEntryPoint.getInstance();
        while (true) {
            try {
                Object incoming = ois.readObject();
                if (incoming != null) {
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            dep.handleMessage((Message) incoming);
                        }
                    });

                }
            } catch (IOException ex) {
                Logger.getLogger(ListenToServerThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ListenToServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
