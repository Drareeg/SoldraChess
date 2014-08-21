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
package Lobby;
import Shared.Networking.ChallengeMessage;
import Shared.Networking.ChatMessage;
import Shared.Networking.JoinLobbyMessage;
import Shared.Networking.LeaveLobbyMessage;
import Shared.Networking.ThisIsTheLobbyMessage;
import Shared.Other.Challenge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Dries
 */
public class Lobby {

    private ObservableList<String> chat;
    private ObservableList<String> userList;
    private ObservableList<Challenge> challengeList;
    private String selfUsername;

    public Lobby() {
        chat = FXCollections.observableArrayList();
        userList = FXCollections.observableArrayList();
        challengeList = FXCollections.observableArrayList();
    }

    public void handleJoin(JoinLobbyMessage message) {
        userList.add(message.getUsername());
    }

    public void handleThisIsTheState(ThisIsTheLobbyMessage thisIsTheLobby) {
        userList.clear();
        userList.addAll(thisIsTheLobby.getUsernames());
    }

    public void handleLeave(LeaveLobbyMessage leaveLobby) {
        userList.remove(leaveLobby.getUsername());
    }

    public void handleChat(ChatMessage aThis) {
        chat.add(aThis.getOriginName() + ": " + aThis.getContent());
    }

    public ObservableList<String> getChat() {
        return chat;
    }

    public ObservableList<String> getUserList() {
        return userList;
    }

    public ObservableList<Challenge> getChallengeList() {
        return challengeList;
    }

    public String getSelfUsername() {
        return selfUsername;
    }

    public void setSelfName(String text) {
        this.selfUsername = text;
    }

    public void gotChallenged(ChallengeMessage challengeMessage) {
        challengeList.add(challengeMessage.getChallenge());
    }

}
