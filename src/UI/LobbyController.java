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
import Shared.Chess.Variant;
import Shared.Networking.AcceptChallengeMessage;
import Shared.Networking.ChallengeMessage;
import Shared.Networking.ChatMessage;
import Shared.Other.Challenge;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
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
    ListView<Challenge> challengeList;

    @FXML
    TextField chatField;

    @FXML
    Button chatButton;

    @FXML
    Button acceptButton;

    DomainEntryPoint dep;
    private Client client;

    public LobbyController(Client client) {
        this.client = client;
    }
    private int i = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dep = DomainEntryPoint.getInstance();
        userList.setItems(dep.getLobby().getUserList());
        chatList.setItems(dep.getLobby().getChat());
        challengeList.setItems(dep.getLobby().getChallengeList());
        String[] variants = new String[]{"Attractchess", "1 2 3 chess", "TornadoChess"};
        Menu challengeMenu = new Menu("Challenge");
        for (Variant variant : Variant.values()) {
            MenuItem challengeVariantItem = new MenuItem(variant.getName());
            challengeVariantItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    client.sendMessage(new ChallengeMessage(userList.getSelectionModel().getSelectedItem(), new Challenge(variant, dep.getLobby().getSelfUsername())));
                }
            });
            challengeMenu.getItems().add(challengeVariantItem);
        }
        ContextMenu contextMenu = new ContextMenu(challengeMenu);
        userList.setContextMenu(contextMenu);
        BooleanBinding acceptButtonBinding = new BooleanBinding() {
            {
                super.bind(challengeList.getSelectionModel().getSelectedItems());
            }

            @Override
            protected boolean computeValue() {
                return challengeList.getSelectionModel().isEmpty();
            }
        };
        acceptButton.disableProperty().bind(acceptButtonBinding);
    }

    @FXML
    public void sendChat(ActionEvent e) {
        if (!chatField.textProperty().isEmpty().get()) {
            client.sendMessage(new ChatMessage(chatField.textProperty().get(), dep.getLobby().getSelfUsername()));
            chatField.clear();
        }
    }

    @FXML
    public void accept(ActionEvent e) {
        client.sendMessage(new AcceptChallengeMessage(challengeList.getSelectionModel().getSelectedItem()));
    }

}
