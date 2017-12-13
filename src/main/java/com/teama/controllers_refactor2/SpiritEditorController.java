package com.teama.controllers_refactor2;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.teama.messages.ContactInfo;
import com.teama.messages.ContactInfoTypes;
import com.teama.messages.Provider;
import com.teama.requestsubsystem.GenericStaff;
import com.teama.requestsubsystem.elevatorfeature.ElevatorAdapter;
import com.teama.requestsubsystem.elevatorfeature.ElevatorStaff;
import com.teama.requestsubsystem.spiritualcarefeature.Religion;
import com.teama.requestsubsystem.spiritualcarefeature.SpiritualCareAdapter;
import com.teama.requestsubsystem.spiritualcarefeature.SpiritualCareStaff;
import com.teama.requestsubsystem.spiritualcarefeature.SpiritualCareSubsystem;
import com.teama.translator.Translator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SpiritEditorController {
    @FXML
    private VBox vbxParentBox;

    @FXML
    private JFXTextField FirstName;

    @FXML
    private JFXTextField LastName;

    @FXML
    private JFXTextField PhoneNo;

    @FXML
    private JFXComboBox<Provider> Providers;

    @FXML
    private JFXTextField Email;

    @FXML
    private JFXRadioButton rdbChrist;

    @FXML
    private JFXRadioButton rdbCath;

    @FXML
    private JFXRadioButton rdbIslam;

    @FXML
    private JFXRadioButton rdbSikhism;

    @FXML
    private JFXRadioButton rdbJewish;

    @FXML
    private JFXRadioButton rdbHinduism;

    @FXML
    private JFXRadioButton rdbBuddhism;

    @FXML
    private JFXRadioButton rdbTaoism;

    @FXML
    private JFXRadioButton rdbOther;

    @FXML
    private JFXButton Cancel;

    @FXML
    private JFXButton btnSubmit;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private TableView<SpiritualCareAdapter> spiritualInfoTable;

    @FXML
    private TableColumn<SpiritualCareAdapter, String> firstCol;

    @FXML
    private TableColumn<SpiritualCareAdapter, String> lastCol;

    @FXML
    private TableColumn<SpiritualCareAdapter, String> religionCol;

    @FXML
    private TableColumn<SpiritualCareAdapter, String> phoneCol;

    @FXML
    private TableColumn<SpiritEditorController, String> emailCol;
    private ArrayList<JFXRadioButton> rdbReligions;
    private SpiritualCareStaff staffToInsert;
    private BooleanProperty editing;
    private ToggleGroup toggleGroup;

    public void initialize(){
        rdbReligions=new ArrayList<>();
        editing = new SimpleBooleanProperty(false);
        for(Provider provider : Provider.values()){
            Providers.getItems().add(provider);
        }
        editing = new SimpleBooleanProperty();
        editing.set(false);
        editing.addListener((obs, before, editing) -> {
            if (editing) {
                btnSubmit.setText(Translator.getInstance().getText("modifyBtn"));

            }
            else{
                btnSubmit.setText(Translator.getInstance().getText("addBtn"));
            }
        });
        rdbReligions.add(rdbBuddhism); rdbReligions.add(rdbCath);
        rdbReligions.add(rdbChrist); rdbReligions.add(rdbHinduism);
        rdbReligions.add(rdbIslam); rdbReligions.add(rdbJewish);
        rdbReligions.add(rdbOther); rdbReligions.add(rdbSikhism);
        rdbReligions.add(rdbTaoism);
        toggleGroup=new ToggleGroup();
        rdbBuddhism.setToggleGroup(toggleGroup); rdbCath.setToggleGroup(toggleGroup);
        rdbChrist.setToggleGroup(toggleGroup); rdbHinduism.setToggleGroup(toggleGroup);
        rdbIslam.setToggleGroup(toggleGroup); rdbJewish.setToggleGroup(toggleGroup);
        rdbOther.setToggleGroup(toggleGroup); rdbSikhism.setToggleGroup(toggleGroup);
        rdbTaoism.setToggleGroup(toggleGroup);
        initSpiritualColumns();
    }
    private ObservableList<SpiritualCareAdapter> tableVals;
    private void initSpiritualColumns(){
        firstCol.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        lastCol.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));
        phoneCol.setCellValueFactory(
                new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(
                new PropertyValueFactory<>("email"));
        religionCol.setCellValueFactory(
                new PropertyValueFactory<>("religion")
        );
        tableVals =  FXCollections.observableArrayList();
        for(SpiritualCareStaff spiritualCareStaff: getSpiritualStaff()){
            tableVals.add(new SpiritualCareAdapter(spiritualCareStaff));
        }
        spiritualInfoTable.setItems(tableVals);
        spiritualInfoTable.setRowFactory(tv -> {
            TableRow<SpiritualCareAdapter> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty()) {
                    SpiritualCareAdapter clickedRow = row.getItem();
                    initSpiritualMod(clickedRow.getSpiritualBase());
                    editing.setValue(true);
                    System.out.println(clickedRow.getEmail());
                }
            });
            return row;
        });
    }
    public VBox getParentVBox(){
        return vbxParentBox;
    }
    private void initSpiritualMod(SpiritualCareStaff spiritualCareStaff){
        staffToInsert = spiritualCareStaff;
        if(staffToInsert!=null){
            FirstName.setText(staffToInsert.getFirstName());
            LastName.setText(staffToInsert.getLastName());
            Email.setText(staffToInsert.getEmail());
            Providers.setValue(staffToInsert.getProvider());
            PhoneNo.setText(staffToInsert.getPhoneNumber());
        }
        for(JFXRadioButton radio: rdbReligions){
            if(Religion.valueOf(radio.getId()).equals(spiritualCareStaff.getReligion())){
                radio.selectedProperty().set(true);
            }
        }

    }
    private void updateSpiritList(){
        initSpiritualColumns();
    }
    @FXML private void cancelStaff(ActionEvent e){
        blankEditor();
    }
    @FXML private void deleteStaff(ActionEvent e){
        if(staffToInsert!=null) {
            String firstName = staffToInsert.getFirstName();
            String lastName = staffToInsert.getLastName();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Interpreter");
            alert.setHeaderText("Remove Interpreter from database");
            alert.setContentText("Are your sure you want to delete \n" + firstName + " "
                    + lastName + " from the database.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                SpiritualCareSubsystem.getInstance().removeStaff(staffToInsert.getStaffID());
                blankEditor();
            } else {
                alert.close();
            }
            updateSpiritList();
            editing.setValue(false);
        }
    }
    @FXML private void submitStaff(ActionEvent e){
        try {
            Set<ContactInfoTypes> contactTypes = new HashSet<ContactInfoTypes>();
            contactTypes.add(ContactInfoTypes.EMAIL);
            contactTypes.add(ContactInfoTypes.TEXT);
            contactTypes.add(ContactInfoTypes.PHONE);
            System.out.println(Providers.getValue());
            Provider provider = Providers.getValue();
            String phoneNumber = PhoneNo.getText();
            String email = Email.getText();
            ContactInfo contactInfo = new ContactInfo(contactTypes, phoneNumber, email, provider);
            GenericStaff staffInfo = new GenericStaff(FirstName.getText(), LastName.getText(), contactInfo);
            Religion religion=null;
            for(JFXRadioButton rad : rdbReligions){
                if(rad.selectedProperty().get()){
                    religion=Religion.valueOf(rad.getId());
                }
            }
            if(!editing.getValue()) {
                SpiritualCareStaff toAdd = new SpiritualCareStaff(staffInfo, religion);
                SpiritualCareSubsystem.getInstance().addStaff(toAdd);
            }
            else{
                SpiritualCareStaff toModify = spiritualInfoTable.getItems().get(spiritualInfoTable.getSelectionModel().getFocusedIndex()).getSpiritualBase();
                toModify.setGenInfo(staffInfo);
                toModify.setReligion(religion);
                SpiritualCareSubsystem.getInstance().updateStaff(toModify);

            }
            updateSpiritList();
        }
        catch(NullPointerException exception){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid information");
            alert.setContentText("Invalid or empty field, please check information and \n submit again");
            Optional<ButtonType> result = alert.showAndWait();
        }

    }
    private void blankEditor(){
        for(JFXRadioButton radio: rdbReligions){
            radio.selectedProperty().set(false);
        }
        FirstName.clear();LastName.clear();PhoneNo.clear();
        Providers.getSelectionModel().clearSelection();
        Email.clear();
        Providers.setValue(null);
    }
    private ArrayList<SpiritualCareStaff> getSpiritualStaff(){
        SpiritualCareSubsystem spiritualCare = SpiritualCareSubsystem.getInstance();
        return  spiritualCare.getAllStaff();
    }
}
