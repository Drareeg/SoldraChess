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

import Lobby.Lobby;
import Networking.Client;
import Shared.Chess.Variants.Board;
import Shared.Networking.AcceptChallengeMessage;
import Shared.Networking.ChallengeMessage;
import Shared.Networking.ChatMessage;
import Shared.Networking.GameFinishedMessage;
import Shared.Networking.GameStartMessage;
import Shared.Networking.JoinLobbyMessage;
import Shared.Networking.LeaveLobbyMessage;
import Shared.Networking.Message;
import Shared.Networking.MessageHandler;
import Shared.Networking.MoveMessage;
import Shared.Networking.SurrenderMessage;
import Shared.Networking.ThisIsMyHiddenQueenMessage;
import Shared.Networking.ThisIsTheBoardMessage;
import Shared.Networking.ThisIsTheLobbyMessage;
import Shared.Networking.TurnMessage;

/**
 *
 * @author Drareeg
 */
public class DomainEntryPoint implements MessageHandler {

    private static DomainEntryPoint _instance = null;

    private Client client;

    //om te kunnen doorgeven aan gamecontroller
    public void setClient(Client client) {
        this.client = client;
    }

    public static DomainEntryPoint getInstance() {
        if (_instance == null) {
            _instance = new DomainEntryPoint();
        }
        return _instance;
    }

    private Lobby lobby;

    public DomainEntryPoint() {
        lobby = new Lobby();
    }

    public Lobby getLobby() {
        return lobby;
    }

    private Board currentBoard;

    //voorlopig alles op de javafx thread, want der zitten paar dingen tussen die de gui veranderen
    //later betere oplossing vinden.
    public void handleMessage(Message message) {
        message.handleSelf(this);
    }

    @Override
    public void handleJoinLobby(JoinLobbyMessage message) {
        lobby.handleJoin(message);
    }

    @Override
    public void handleChallenge(ChallengeMessage challengeMessage) {
        lobby.gotChallenged(challengeMessage);
    }

    @Override
    public void handleMove(MoveMessage moveMessage) {
        throw new UnsupportedOperationException("Gebruik ThisIsTheBoardMessage.");
        //currentBoard.movePiece(moveMessage.getFromRow(), moveMessage.getFromCol(), moveMessage.getToRow(), moveMessage.getToCol());
    }

    //best niet bijhouden in een veld (zelf helemaal niet bijhouden). refactoren dat er toch messages aan doorgegeven kunnen worden... (hoeft niet rechtstraaks)
    GameController gameController;

    @Override
    public void handleGameStart(GameStartMessage gameStart) {
        currentBoard = new Board();
        gameController = new GameController(currentBoard, client, gameStart.AmIWhite(), gameStart.getAgainstName());
        GUI.setScene(GUI.GAMESCENE, gameController);
        gameController.syncBoardToUI();
    }

    @Override
    public void handleTurnMessage(TurnMessage aThis) {
        gameController.setMyTurn(aThis.isMyTurn());
    }

    @Override
    public void handleThisIsTheBoard(ThisIsTheBoardMessage aThis) {
        currentBoard.updateTo(aThis.getBoard());
    }

    @Override
    public void handleThisIsTheLobbyMessage(ThisIsTheLobbyMessage thisIsTheLobby) {
        lobby.handleThisIsTheState(thisIsTheLobby);
    }

    @Override
    public void handleLeaveLobby(LeaveLobbyMessage leaveLobby) {
        lobby.handleLeave(leaveLobby);
    }

    @Override
    public void handleChatMessage(ChatMessage aThis) {
        lobby.handleChat(aThis);
    }

    @Override
    public void handleAcceptChallenge(AcceptChallengeMessage aThis) {
        throw new UnsupportedOperationException("C -> S message"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleGameFinished(GameFinishedMessage aThis) {
        gameController.gameFinished(aThis);
    }

    @Override
    public void handleSurrender(SurrenderMessage aThis) {
        throw new UnsupportedOperationException("enkel voor server"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleThisIsMyHiddenQueenMessage(ThisIsMyHiddenQueenMessage aThis) {
        throw new UnsupportedOperationException("voor server"); //To change body of generated methods, choose Tools | Templates.
    }

}
