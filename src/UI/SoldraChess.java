/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Networking.Client;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Dries
 */
public class SoldraChess extends Application {

    @Override
    public void start(Stage stage) {
        Client client = new Client();
        client.requestLobby();
        setUserAgentStylesheet(STYLESHEET_MODENA);
        new GUI(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
