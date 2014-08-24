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
import Shared.Chess.ChessPiece;
import Shared.Chess.Coordinate;
import Shared.Chess.Variants.Board;
import Shared.Networking.GameFinishedMessage;
import Shared.Networking.MoveMessage;
import Shared.Networking.SurrenderMessage;
import Shared.Networking.ThisIsMyHiddenQueenMessage;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Drareeg
 */
class GameController implements Initializable, BoardChangeListener {

    private Board board;
    private Client client;
    private ChessFieldPane[][] panes;
    private boolean myTurn = false;
    private boolean amIWhite;

    @FXML
    AnchorPane anchorPane;

    @FXML
    Label player1Label;

    @FXML
    Label player2Label;

    @FXML
    Label turnLabel;

    @FXML
    Button finishedButton;

    @FXML
    Label finishedLabel;
    @FXML
    Button surrenderButton;
    private final static HashMap<String, String> images;

    static {
        images = new HashMap<>();
        images.put("FieldB", SoldraChess.class.getResource("resources/field_black.png").toExternalForm());
        images.put("FieldW", SoldraChess.class.getResource("resources/field_white.png").toExternalForm());
    }

    public GameController(Board board, Client client, boolean amIWhite, String against) {
        this.board = board;
        board.setBCL(this);
        this.client = client;
        this.amIWhite = amIWhite;
        String me = DomainEntryPoint.getInstance().getLobby().getSelfUsername();
        if (amIWhite) {
//            player1Label.setText(me);
//            player2Label.setText(against);
//            turnLabel.setText(me);
        } else {
//            player1Label.setText(against);
//            player2Label.setText(me);
        }
        client.sendMessage(new ThisIsMyHiddenQueenMessage(3, amIWhite));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        panes = new ChessFieldPane[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessFieldPane field;
                if (amIWhite) {
                    field = new ChessFieldPane(row, col);
                } else {
                    field = new ChessFieldPane(7 - row, 7 - col);
                }
                field.setMinSize(50, 50);
                field.setOnMouseClicked(new CoordinateEventHandler(field));
                field.setLayoutX(col * 50);
                field.setLayoutY(row * 50);
                anchorPane.getChildren().add(field);
                panes[row][col] = field;
            }
        }
    }

    public void syncBoardToUI() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (amIWhite) {
                    panes[row][col].setPiece(board.getPiece(new Coordinate(row, col)));
                } else {
                    panes[7 - row][7 - col].setPiece(board.getPiece(new Coordinate(row, col)));
                }
            }
        }
    }

    private boolean isSomethingSelected;
    private ChessFieldPane selectedPane;

    void clicked(ChessFieldPane pane) {
        if (myTurn) {
            if (!isSomethingSelected) {
                ChessPiece clickedPiece = board.getPiece(pane.getCoord());
                if (clickedPiece != null && clickedPiece.isWhite == amIWhite) {
                    isSomethingSelected = true;
                    selectedPane = pane;
                    pane.select().doStyle();
                }
            } else {
                isSomethingSelected = false;
                selectedPane.deselect().doStyle();
                // if (selRow != row || selCol != col) {
                //elke illegale zet is nu nog mogelijk -> geeft niet, het is de server die controleert en zegt wat de state is na de zet.
                //beter ook niet de messages vanaf hier sturen, but yeah, lazy
                client.sendMessage(new MoveMessage(selectedPane.getCoord(), pane.getCoord()));
                //}
            }
        }
    }

    public void setMyTurn(boolean myTurn) {
        System.out.println("Setting myturn to " + myTurn);
        this.myTurn = myTurn;
    }

    @Override
    public void boardChanged() {
        this.syncBoardToUI();
    }

    void gameFinished(GameFinishedMessage aThis) {
        finishedButton.setVisible(true);
        finishedLabel.setVisible(true);
        finishedLabel.setText("GAME FINISHED result: " + aThis.getResult());
        surrenderButton.setVisible(false);
    }

    @FXML
    public void backToLobby(ActionEvent e) {
        GUI.setScene(GUI.LOBBYSCENE, new LobbyController(client));
    }

    @FXML
    public void surrender(ActionEvent e) {
        Action response = Dialogs.create()
                .owner(null)
                .title(null)
                .message("Surrender?")
                .showConfirm();
        if (response == Dialog.Actions.YES) {
            client.sendMessage(new SurrenderMessage());
            GUI.setScene(GUI.LOBBYSCENE, new LobbyController(client));
        }
    }

    private class ChessFieldPane extends StackPane {
        ChessPiece piece;
        Coordinate coord;
        boolean selected;

        ImageView label;

        public ChessFieldPane(int row, int col) {
            this.coord = new Coordinate(row, col);
            label = new ImageView();
            this.getChildren().add(label);
        }

        void setPiece(ChessPiece piece) {
            this.piece = piece;
            doStyle();
        }

        public void doStyle() {
            this.setStyle("-fx-background-image: url('"
                    + images.get("Field" + ((((coord.getRow() + coord.getCol()) % 2 == 1)) ? "W" : "B"))
                    + "');");
            if (piece != null) {
                label.setImage(piece.getImage(amIWhite));
            } else {
                label.setImage(null);
            }
            if (selected) {
                this.setStyle(this.getStyle() + "-fx-border-color: blue;\n"
                        + "-fx-border-width: 3;\n"
                        + "-fx-border-style: dashed;\n");
            }
        }

        private ChessFieldPane select() {
            selected = true;
            return this;
        }

        private ChessFieldPane deselect() {
            selected = false;
            return this;
        }

        public Coordinate getCoord() {
            return coord;
        }

    }

    class CoordinateEventHandler implements EventHandler<MouseEvent> {
        ChessFieldPane pane;

        private CoordinateEventHandler(ChessFieldPane pane) {
            this.pane = pane;
        }

        @Override
        public void handle(MouseEvent event) {
            clicked(pane);
        }

    }
}
