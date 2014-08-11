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
import Shared.Networking.MoveMessage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Drareeg
 */
class GameController implements Initializable {

    private Board board;
    private Client client;

    @FXML
    public GridPane boardGrid;

    public GameController(Board board, Client client) {
        this.board = board;
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessFieldControl cfc = new ChessFieldControl(r, c, this);
                cfc.setOnMouseClicked(cfc);
                boardGrid.add(cfc, c, r);
            }
        }
    }
    
    private boolean isSomethingSelected; 
    private int selCol;
    private int selRow;

    void clicked(int row, int col) {
        if(!isSomethingSelected){
            //efkes weg omdat isempty nog niet bestaat.
            //if(!board.isEmpty(row, col)){
                isSomethingSelected = true;
                selCol = col;
                selRow = row;
            //}
        }else{
            if(selRow != row || selCol != col){
                //elke illegale zet is nu nog mogelijk
                //beter ook niet de messages vanaf hier sturen, but yeah, lazy
                isSomethingSelected = false;
                client.sendMessage(new MoveMessage(selRow, selCol, row, col));
            }
        }
    }

   

}
