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

import Networking.Client;
import Shared.Networking.ChallengeMessage;
import Shared.Networking.ChatMessage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 *
 * @author Drareeg
 */
class LobbyController implements Initializable {

    @FXML
    ListView<String> userList;

    @FXML
    ListView<String> chatList;

    @FXML
    TextField chatField;

    @FXML
    Button chatButton;

    DomainEntryPoint dep;
    private Client client;

    public LobbyController(Client client) {
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dep = DomainEntryPoint.getInstance();
        userList.setItems(dep.getLobby().getUserList());
        chatList.setItems(dep.getLobby().getChat());
    }

    @FXML
    public void challenge(ActionEvent e) {
        client.sendMessage(new ChallengeMessage(userList.getSelectionModel().getSelectedItem()));
    }

    @FXML
    public void sendChat(ActionEvent e) {
        if (!chatField.textProperty().isEmpty().get()) {
            client.sendMessage(new ChatMessage(chatField.textProperty().get(), dep.getLobby().getSelfUsername()));
            chatField.clear();
        }
    }
}
