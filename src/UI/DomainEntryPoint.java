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
import Shared.Chess.Board;
import Shared.Networking.ChallengeMessage;
import Shared.Networking.ChatMessage;
import Shared.Networking.GameStartMessage;
import Shared.Networking.JoinLobbyMessage;
import Shared.Networking.LeaveLobbyMessage;
import Shared.Networking.Message;
import Shared.Networking.MessageHandler;
import Shared.Networking.MoveMessage;
import Shared.Networking.ThisIsTheLobbyMessage;

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleMove(MoveMessage moveMessage) {
        currentBoard.movePiece(moveMessage.getFromRow(), moveMessage.getFromCol(), moveMessage.getToRow(), moveMessage.getToCol());
    }

    @Override
    public void handleGameStart(GameStartMessage gameStart) {
        currentBoard = new Board();
        GameController gameController = new GameController(currentBoard, client);
        GUI.setScene(GUI.GAMESCENE, gameController);
        gameController.syncBoardToUI();
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
}
