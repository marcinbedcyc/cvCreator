package controllers;

import cvObjects.Address;
import cvObjects.LifeEvent;
import cvObjects.Person;
import generators.CvGenerator;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import models.AbilityModel;
import models.LifeEventModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainWindowController {
    private String imageSource;
    private String destination;
    private HostServices hostServices;
    private List<AbilityModel> skillsList = new ArrayList<>();
    private List<AbilityModel> interestsList = new ArrayList<>();
    private List<LifeEventModel> experiencesList = new ArrayList<>();
    private List<LifeEventModel> coursesList = new ArrayList<>();
    private List<LifeEventModel> educationsList = new ArrayList<>();

    @FXML
    private VBox mainBox;

    @FXML
    private Button imageChoserButton;

    @FXML
    private Label pathLabel;

    @FXML
    private Button chosePathButton;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField streetTextField;

    @FXML
    private TextField houseNumberTextField;

    @FXML
    private TextField houseLetterTextField;

    @FXML
    private TextField flatNumberTextField;

    @FXML
    private TextField postCodedTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextArea shortInfoTextArea;

    @FXML
    private Button okButton;

    @FXML
    private VBox skillsVBox;

    @FXML
    private Button addSkillButton;

    @FXML
    private VBox interestsVBox;

    @FXML
    private Button addInterestButton;

    @FXML
    private VBox experienceVBox;

    @FXML
    private Button addExperienceButton;

    @FXML
    private VBox educationVbox;

    @FXML
    private Button addEducationButton;

    @FXML
    private VBox coursesVbox;

    @FXML
    private Button addCourseButton;

    @FXML
    private Button openButton;


    @FXML
    void initialize() {
        openButton.setDisable(true);
        imageSource = this.getClass().getResource("/man.png").getPath();
        mainBox.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                generateCV(null);
            }
        });
        houseLetterTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (houseLetterTextField.getText().length() > 1) {
                    String s = houseLetterTextField.getText().substring(0, 1);
                    houseLetterTextField.setText(s);
                }
            }
        });
        System.out.println(imageSource);
    }

    private LifeEventModel addAndBindLifeEvent(VBox container, String windowsTitle, String header) {
        Dialog<LifeEvent> dialog = customDialog(windowsTitle, header);
        Optional<LifeEvent> result = dialog.showAndWait();

        if (result.isPresent()) {
            TextField title = new TextField(result.get().getTitle());
            TextArea description = new TextArea(result.get().getDescription());
            description.setWrapText(true);
            description.setMinHeight(60);
            description.setPromptText("Opis");

            DatePicker begin;
            if (result.get().getBegin() != null)
                begin = new DatePicker(result.get().getBegin());
            else {
                begin = new DatePicker();
                begin.setPromptText("Początek");
            }
            DatePicker end;
            if (result.get().getEnd() != null)
                end = new DatePicker(result.get().getBegin());
            else {
                end = new DatePicker();
                end.setPromptText("Koniec");
            }

            container.getChildren().addAll(title, description, begin, end);

            LifeEventModel lifeEventModel = new LifeEventModel();
            lifeEventModel.titleProperty().bind(title.textProperty());
            lifeEventModel.descriptionProperty().bind(description.textProperty());
            lifeEventModel.beginProperty().bind(begin.valueProperty());
            lifeEventModel.endProperty().bind(end.valueProperty());
            return lifeEventModel;
        }
        else
            return null;

    }

    private AbilityModel addAndBindAbility(VBox container, String defaultText){
        TextInputDialog textInputDialog = new TextInputDialog(defaultText);
        Optional<String> result = textInputDialog.showAndWait();

        if(result.isPresent()) {
            TextField temp = new TextField(result.get());
            container.getChildren().add(temp);

            AbilityModel abilityModel = new AbilityModel();
            abilityModel.abilityProperty().bind(temp.textProperty());
            return abilityModel;
        }
        else
            return null;
    }

    @FXML
    void addCourse(ActionEvent event) {
        coursesList.add(addAndBindLifeEvent(coursesVbox, "Dodaj kurs", "Wprowadź dane dla kursu"));
    }

    @FXML
    void addEducation(ActionEvent event) {
        educationsList.add(addAndBindLifeEvent(educationVbox, "Dodaj edukację", "Wprowadź dane dla edukacji"));
    }

    @FXML
    void addExperience(ActionEvent event) {
        experiencesList.add(addAndBindLifeEvent(experienceVBox,"Dodaj doświadczenie", "Wprowadź dane dla doświadczenia"));
    }

    @FXML
    void addInterest(ActionEvent event) {
        interestsList.add(addAndBindAbility(interestsVBox, "Nowe Zainteresowanie"));
    }

    @FXML
    void addSkill(ActionEvent event) {
        skillsList.add(addAndBindAbility(skillsVBox, "Nowa Umiejętność"));
    }

    @FXML
    void choseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(nameTextField.getScene().getWindow());
        if(file != null) {
            imageSource = file.getAbsolutePath();
            imageView.setImage(new Image("file://" + imageSource));
        }
    }

    private List<LifeEvent> getListLifeEvents(List<LifeEventModel> models){
        List<LifeEvent> elements = new ArrayList<>();
        for(LifeEventModel event : models){
            elements.add(new LifeEvent(event.getBegin(), event.getEnd(), event.getTitle(), event.getDescription()));
        }
        return elements;
    }

    @FXML
    void generateCV(ActionEvent event) {
        destination = "/home/marcin/tempPDFs/cv.pdf";
        int houseNumber, flatNumber;
        houseNumber = flatNumber = -1;
        boolean isFilled = true;

        Person cvMaker = new Person();

        //validate name
        if(!nameTextField.getText().equals("")){
            cvMaker.setName(nameTextField.getText());
            nameTextField.setStyle("-fx-background-color: rgba(0,255,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: green");
        }
        else{
            nameTextField.setStyle("-fx-background-color: rgba(255,0,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: red");
            isFilled = false;
        }

        //validate surname
        if(!surnameTextField.getText().equals("")){
            cvMaker.setSurname(surnameTextField.getText());
            surnameTextField.setStyle("-fx-background-color: rgba(0,255,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: green");
        }else {
            surnameTextField.setStyle("-fx-background-color: rgba(255,0,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: red");
            isFilled = false;
        }

        //validate e-mail
        if(!emailTextField.getText().equals("")){
            cvMaker.setEmailAddress(emailTextField.getText());
            emailTextField.setStyle("-fx-background-color: rgba(0,255,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: green");
        }else {
            emailTextField.setStyle("-fx-background-color: rgba(255,0,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: red");
            isFilled = false;
        }

        //validate phone number
        try{
            phoneNumberTextField.setStyle("-fx-background-color: rgba(0,255,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: green");
            cvMaker.setPhoneNumber(Integer.valueOf(phoneNumberTextField.getText()));
        }catch(NumberFormatException exception){
            phoneNumberTextField.setStyle("-fx-background-color: rgba(255,0,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: red");
            isFilled = false;
        }

        //validate date of birth
        if(birthDatePicker.getValue() != null) {
            cvMaker.setDateOfBirth(birthDatePicker.getValue());
            birthDatePicker.setStyle("-fx-prompt-text-fill: black; -fx-border-color: green");
            birthDatePicker.getEditor().setStyle("-fx-background-color: rgba(0,255,0,0.25); -fx-prompt-text-fill: black");
        }
        else{
            birthDatePicker.setStyle("-fx-prompt-text-fill: black; -fx-border-color: red");
            birthDatePicker.getEditor().setStyle("-fx-background-color: rgba(255,0,0,0.25); -fx-prompt-text-fill: black");
            isFilled = false;
        }

        //validate house's number
        try {
            houseNumber = Integer.valueOf(houseNumberTextField.getText());
            houseNumberTextField.setStyle("-fx-background-color: rgba(0,255,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: green");
        }catch(NumberFormatException exception){
            houseNumberTextField.setStyle("-fx-background-color: rgba(255,0,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: red");
            isFilled = false;
        }

        //validate flat's number
        try {
            flatNumber = Integer.valueOf(flatNumberTextField.getText());
            flatNumberTextField.setStyle("-fx-background-color: rgba(0,255,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: green");
        }catch(NumberFormatException exception){
            if(!flatNumberTextField.getText().equals("")) {
                flatNumberTextField.setStyle("-fx-background-color: rgba(255,0,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: red");
                isFilled = false;
            }
            else {
                flatNumber = -1;
                flatNumberTextField.setStyle("-fx-background-color: rgba(0,255,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: green");
            }
        }

        //validate house's letter
        Character houseLetter = null;
        houseLetterTextField.setStyle("-fx-background-color: rgba(0,255,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: green");
        if(!houseLetterTextField.getText().equals("")) {
            if(Character.isLetter(houseLetterTextField.getText().charAt(0)))
                houseLetter = houseLetterTextField.getText().charAt(0);
            else{
                houseLetterTextField.setStyle("-fx-background-color: rgba(255,0,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: red");
                isFilled = false;
            }
        }

        //validate street
        String street = "";
        if(!streetTextField.getText().equals("")){
            street = streetTextField.getText();
            streetTextField.setStyle("-fx-background-color: rgba(0,255,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: green");
        }else {
            streetTextField.setStyle("-fx-background-color: rgba(255,0,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: red");
            isFilled = false;
        }

        //validate city
        String city = "";
        if(!cityTextField.getText().equals("")){
            city = cityTextField.getText();
            cityTextField.setStyle("-fx-background-color: rgba(0,255,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: green");
        }else {
            cityTextField.setStyle("-fx-background-color: rgba(255,0,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: red");
            isFilled = false;
        }

        //validate post code
        String postCode = "";
        if(!postCodedTextField.getText().equals("")){
            postCode = postCodedTextField.getText();
            if(postCode.length() == 6){
                if(Character.isDigit(postCode.charAt(0)) && Character.isDigit(postCode.charAt(1)) && Character.isDigit(postCode.charAt(3)) &&
                        Character.isDigit(postCode.charAt(4)) && Character.isDigit(postCode.charAt(5)) && postCode.charAt(2) == '-'){
                    postCodedTextField.setStyle("-fx-background-color: rgba(0,255,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: green");
                }
                else{
                    postCodedTextField.setStyle("-fx-background-color: rgba(255,0,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: red");
                    isFilled = false;
                }
            }
            else{
                postCodedTextField.setStyle("-fx-background-color: rgba(255,0,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: red");
                isFilled = false;
            }
        }else {
            postCodedTextField.setStyle("-fx-background-color: rgba(255,0,0,0.25); -fx-prompt-text-fill: black; -fx-border-color: red");
            isFilled = false;
        }

        cvMaker.setAddress(new Address(street, houseNumber, flatNumber, houseLetter, postCode, city));
        cvMaker.setImageFile(imageSource);

        cvMaker.setShortInfo(shortInfoTextArea.getText());
        shortInfoTextArea.getStylesheets().add(this.getClass().getResource("/css/textArea.css").toExternalForm());

        List<String> interest = new ArrayList<>();
        for(AbilityModel i : interestsList){
            interest.add(i.getAbility());
        }
        cvMaker.setInterest(interest);

        List<String> skills = new ArrayList<>();
        for(AbilityModel i : skillsList){
            skills.add(i.getAbility());
        }
        cvMaker.setSkills(skills);

        cvMaker.setExperience(getListLifeEvents(experiencesList));

        cvMaker.setCourses(getListLifeEvents(coursesList));

        cvMaker.setEducation(getListLifeEvents(educationsList));

        if(isFilled) {
            int maxFontSize = 15;
            float height;
            CvGenerator cvGenerator = new CvGenerator(cvMaker, maxFontSize);
            height = cvGenerator.generateCv(destination);
            while (height > 842) {
                maxFontSize--;
                CvGenerator temp = new CvGenerator(cvMaker, maxFontSize);
                height = temp.generateCv(destination);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wygenerowano");
            alert.setHeaderText("Twoje Cv zostało wygenerowane");
            alert.setContentText("Sprawdź katalog docelowy");
            alert.showAndWait();
            openButton.setDisable(false);
        }
    }

    private Dialog<LifeEvent> customDialog(String windowsTitle, String header){
        Dialog<LifeEvent> dialog = new Dialog<>();
        dialog.setTitle(windowsTitle);
        dialog.setHeaderText(header);

        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField title = new TextField();
        title.setPromptText("Tytuł");
        TextArea description = new TextArea();
        description.setWrapText(true);
        description.setPromptText("Opis");
        DatePicker begin = new DatePicker();
        DatePicker end = new DatePicker();

        grid.add(new Label("Tytuł:"), 0, 0);
        grid.add(title, 1, 0);
        grid.add(new Label("Opis:"), 0, 1);
        grid.add(description, 1, 1);
        grid.add(new Label("Poczatek:"), 0, 2);
        grid.add(begin, 1, 2);
        grid.add(new Label("Koniec:"), 0, 3);
        grid.add(end, 1, 3);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        title.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new LifeEvent(begin.getValue(), end.getValue(), title.getText(), description.getText());
            }
            return null;
        });
        return dialog;
    }

    @FXML
    void chosePath(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(okButton.getScene().getWindow());
        if(directory != null){
            destination = directory.getAbsolutePath() + "/cv.pdf";
            pathLabel.setText(directory.getAbsolutePath());
        }
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    void openCV(ActionEvent event) {
        hostServices.showDocument(destination);
    }
}
