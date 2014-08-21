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
import Shared.Chess.Board;
import Shared.Chess.ChessPiece;
import Shared.Chess.Coordinate;
import Shared.Networking.MoveMessage;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Drareeg
 */
class GameController implements Initializable, BoardChangeListener {

    private final static HashMap<String, String> images;

    static {
        images = new HashMap<>();
        images.put("RookB", SoldraChess.class.getResource("resources/rook_black.png").toExternalForm());
        images.put("RookW", SoldraChess.class.getResource("resources/rook_white.png").toExternalForm());
        images.put("KnightB", SoldraChess.class.getResource("resources/knight_black.png").toExternalForm());
        images.put("KnightW", SoldraChess.class.getResource("resources/knight_white.png").toExternalForm());
        images.put("BishopB", SoldraChess.class.getResource("resources/bishop_black.png").toExternalForm());
        images.put("BishopW", SoldraChess.class.getResource("resources/bishop_white.png").toExternalForm());
        images.put("QueenB", SoldraChess.class.getResource("resources/queen_black.png").toExternalForm());
        images.put("QueenW", SoldraChess.class.getResource("resources/queen_white.png").toExternalForm());
        images.put("KingB", SoldraChess.class.getResource("resources/king_black.png").toExternalForm());
        images.put("KingW", SoldraChess.class.getResource("resources/king_white.png").toExternalForm());
        images.put("PawnB", SoldraChess.class.getResource("resources/pawn_black.png").toExternalForm());
        images.put("PawnW", SoldraChess.class.getResource("resources/pawn_white.png").toExternalForm());
    }

    private Board board;
    private Client client;
    private ChessFieldPane[][] panes;
    private boolean myTurn = false;

    @FXML
    AnchorPane anchorPane;

    public GameController(Board board, Client client) {
        this.board = board;
        board.setBCL(this);
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        panes = new ChessFieldPane[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessFieldPane field = new ChessFieldPane(row, col);
                field.setMinSize(100, 100);
                field.setOnMouseClicked(new CoordinateEventHandler(row, col));
                field.setLayoutX(col * 100);
                field.setLayoutY(row * 100);
                anchorPane.getChildren().add(field);
                panes[row][col] = field;
            }
        }
    }

    public void syncBoardToUI() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                panes[row][col].setPiece(board.getPiece(new Coordinate(row, col)));
            }
        }
    }

    private boolean isSomethingSelected;
    private int selCol;
    private int selRow;

    void clicked(int row, int col) {
        if (myTurn) {
            if (!isSomethingSelected) {
                if (panes[row][col].hasAPiece()) {
                    isSomethingSelected = true;
                    selCol = col;
                    selRow = row;
                    panes[selRow][selCol].select().doStyle();
                }
            } else {
                isSomethingSelected = false;
                panes[selRow][selCol].deselect().doStyle();
                if (selRow != row || selCol != col) {
                    //elke illegale zet is nu nog mogelijk
                    //beter ook niet de messages vanaf hier sturen, but yeah, lazy
                    client.sendMessage(new MoveMessage(new Coordinate(selRow, selCol), new Coordinate(row, col)));
                }
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

    private static class ChessFieldPane extends Pane {
        ChessPiece piece;
        int row;
        int col;
        boolean selected;

        public ChessFieldPane(int row, int col) {
            this.row = row;
            this.col = col;
        }

        void setPiece(ChessPiece piece) {
            this.piece = piece;
            doStyle();
        }

        public void doStyle() {
            this.setStyle(((row + col) % 2 == 1) ? "-fx-background-color: black;" : "-fx-background-color: white;");
            if (piece != null) {
                this.setStyle("-fx-background-image: url('"
                        + images.get(piece.getClass().getSimpleName() + (piece.isWhite ? "W" : "B"))
                        + "'); -fx-background-repeat: no-repeat; -fx-background-position: center center; ");
            }
            if (selected) {
                this.setStyle(this.getStyle() + "-fx-border-color: blue;\n"
                        + "-fx-border-insets: 5;\n"
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

        private boolean hasAPiece() {
            return piece != null;
        }

    }

    class CoordinateEventHandler implements EventHandler<MouseEvent> {

        int row;
        int col;

        private CoordinateEventHandler(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void handle(MouseEvent event) {
            clicked(row, col);
        }

    }
}
