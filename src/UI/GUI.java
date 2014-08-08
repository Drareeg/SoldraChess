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
package UI;

import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Dries
 */
public class GUI {

    public static final String MAINSCENE = "MainScene.fxml";

    private final static Map<String, String> titelMap;

    static {
        titelMap = new HashMap();
    }

    public static Stage primaryStage;

    public GUI(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("SoldraChess");
        try {
            startApp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setScene(String sceneName, Object controller) {
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource(sceneName));
        if (controller != null) {
            loader.setController(controller);
        }
        try {
            loader.load();
            Parent root = loader.getRoot();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/UI/main.css");
            primaryStage.setScene(scene);
            doTitle(sceneName);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doTitle(String sceneName) {
        if (titelMap.containsKey(sceneName)) {
            primaryStage.setTitle(titelMap.get(sceneName));
        } else {
            primaryStage.setTitle("SoldraChess");
        }
    }

    public static void setScene(String sceneName) {
        setScene(sceneName, null);
    }

    public void startApp() throws Exception {
        GUI.setScene(GUI.MAINSCENE, new MainSceneController());
    }
}
