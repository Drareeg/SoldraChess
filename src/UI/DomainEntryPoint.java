/*
 * The MIT License
 *
 * Copyright 2014 Drareeg.
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
package UI;

import Shared.Networking.GameStartMessage;
import Shared.Networking.JoinLobbyMessage;
import Shared.Networking.Message;
import Shared.Networking.ThisIsTheLobbyMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Drareeg
 */
public class DomainEntryPoint {

    private static DomainEntryPoint _instance = null;

    public static DomainEntryPoint getInstance() {
        if (_instance == null) {
            _instance = new DomainEntryPoint();
        }
        return _instance;

    }

    public DomainEntryPoint() {
        //hier allerlei domeinobjecten aanmaken (lobby, ...)
        //tijdelijk lazy en alles gwn hierin gezet ;)
        userList = FXCollections.observableArrayList();
    }
    
    public ObservableList<String> userList;

    //voorlopig alles op de javafx thread, want der zitten paar dingen tussen die de gui veranderen
    //later betere oplossing vinden.
    public void handleMessage(Message message) {
        if (message instanceof ThisIsTheLobbyMessage) {
            userList.clear();
            userList.addAll(((ThisIsTheLobbyMessage) message).getUsernames());
        }
        if (message instanceof JoinLobbyMessage) {
            userList.add(((JoinLobbyMessage) message).getUsername());
            System.out.println("user added to list");
        }
        if(message instanceof GameStartMessage){
            GUI.setScene(GUI.GAMESCENE, new GameController());
        }
    }
}
