package com.teama.controllers_refactor2;

import com.teama.controllers.SpiritualCareController;
import com.teama.translator.Translator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class StaffController extends StaffToolController{

    @FXML
    private Pane parentPane;

    @FXML
    private VBox intVBox;

    @FXML
    private VBox spirVBox;

    @FXML
    private VBox eleVBox;

    @FXML
    void interchange(ActionEvent event) {
    }

    public void initialize(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScreenDrawers/staffPanes/MaintenanceEditor.fxml"));
            loader.load();
            MatStaffController myController = loader.getController();
            if(eleVBox.getChildren()!=null) {
                eleVBox.getChildren().clear();
            }
            eleVBox.getChildren().add(myController.getVbxParentPane());
            System.out.println(myController.getVbxParentPane().getChildren().size());
            System.out.println(eleVBox);

            FXMLLoader interLoader = new FXMLLoader((getClass().getResource("/MainScreenDrawers/staffPanes/interpreterPane.fxml")));
            interLoader.setResources(Translator.getInstance().getNewBundle());
            interLoader.load();
            InterStaffController interController = interLoader.getController();
            if(intVBox.getChildren()!=null) {
                intVBox.getChildren().clear();
            }
            intVBox.getChildren().add(interController.getParentVbox());

            FXMLLoader spiritLoader = new FXMLLoader((getClass().getResource("/MainScreenDrawers/staffPanes/SpiritualEditor.fxml")));
            spiritLoader.setResources(Translator.getInstance().getNewBundle());
            spiritLoader.load();
            SpiritEditorController spiritController = spiritLoader.getController();
            if(spirVBox.getChildren()!=null){
                spirVBox.getChildren().clear();
            }
            spirVBox.getChildren().add(spiritController.getParentVBox());
        }
        catch(IOException error){
            error.printStackTrace();
        }
    }

    @Override
    public Pane getParentPane() {
        return parentPane;
    }

    @Override
    public String getFXMLPath() {
        return "/MainScreenDrawers/StaffEditor.fxml";
    }
}
