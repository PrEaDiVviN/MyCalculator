package main;

import computerUtilities.computer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.security.Key;


public class computerController {
    @FXML
    private Label viewLabel;

    @FXML
    private Button buttonDelete;

    @FXML
    private Pane panel;

    @FXML
    private GridPane gridPanel;

    @FXML
    private Label bigNumberSignLabel;


    private StringBuilder backgroundInput = new StringBuilder("");
    private final computer Computer = new computer();
    private int cursorPosition = 0;
    private final IntegerProperty buttonWidth = new SimpleIntegerProperty(50);

    public void initialize (){
        ImageView imageView = new ImageView(getClass().getResource("/images/delete.png").toExternalForm());
        buttonDelete.setGraphic(imageView);
        viewLabel.setWrapText(true);

        panel.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.LEFT) {moveLeft(); e.consume();}
            else if(e.getCode() == KeyCode.RIGHT) {moveRight(); e.consume();}
            else if(e.getCode() == KeyCode.ENTER) {equal(); e.consume();}
            else if(e.getCode() == KeyCode.BACK_SPACE) {removeFromCursor();e.consume();}
            else if(e.getCode() == KeyCode.DELETE) {clearString();e.consume();}
        });

        panel.addEventFilter(KeyEvent.KEY_TYPED, e -> {
            reinitializeCalculator();
            switch(e.getCharacter().charAt(0)) {
                case '0' : { addZero(); break; }
                case '1' : { addOne(); break; }
                case '2' : { addTwo(); break; }
                case '3' : { addThree(); break; }
                case '4' : { addFour(); break; }
                case '5' : { addFive(); break; }
                case '6' : { addSix(); break; }
                case '7' : { addSeven(); break; }
                case '8' : { addEight(); break; }
                case '9' : { addNine(); break; }
                case '.' : { addComma(); break; }
                case '+' : { addPlus(); break; }
                case '-' : { addMinus(); break; }
                case '*' : { addMultiply(); break; }
                case '/' : { addDivide(); break; }
                case ':' : { addDivide(); break; }
                case '^' : { addPow(); break; }
                case '%' : { addPercentage(); break; }
                case '=' : { equal(); break; }
                case '(' : { addLeftBracket(); break; }
                case ')' : { addRightBracket(); break; }
            }
        });
    }

    @FXML
    void equal() {
        removeCursor();
        double result = 0;
        try {
            result = Computer.evaluateExpression(backgroundInput);
            String value = Double.toString(result);
            int index = containChar(value,'E');
            if(index != -1) {
                value = value.substring(0, index);
                index = containChar(value,'.');
                if(index != -1)
                    value = new StringBuilder(value).deleteCharAt(index).toString();
                bigNumberSignLabel.setText("TRUNCATED NUMBER");
            }
            FileWriter myWriter = new FileWriter("src\\logs\\history.txt", true);
            PrintWriter printWriter = new PrintWriter(myWriter);
            printWriter.println(backgroundInput +" = " + value);
            printWriter.close();
            if(result < 0)
                value = "(" + value + ")";
            viewLabel.setText(value + "|");
            backgroundInput = new StringBuilder(value + "|");
            cursorPosition = backgroundInput.length() - 1;
        }
        catch (Exception e) {
            try {
                FileWriter myWriter = new FileWriter("src\\logs\\history.txt", true);
                PrintWriter printWriter = new PrintWriter(myWriter);
                printWriter.println(backgroundInput + " = " + " ecuatie scrisa gresit!");
                printWriter.close();
                myWriter.close();
                setErrorMessage();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @FXML
    public void goToHistory(ActionEvent event) throws IOException {
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(new File("src\\main\\historyStructure.fxml").toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Parent home_page_parent =loader.load();
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }

    @FXML
    void clearString() {
        reinitializeCalculator();
        viewLabel.setText("");
        backgroundInput = new StringBuilder("");
        cursorPosition = 0;
    }

    @FXML
    void removeFromCursor() {
        reinitializeCalculator();
        if(cursorPosition != 0) {
            backgroundInput.deleteCharAt(cursorPosition - 1);
            if (cursorPosition > 0)
                cursorPosition = cursorPosition - 1;
            viewLabel.setText(backgroundInput.toString());
        }
    }

    @FXML
    void moveLeft() {
        reinitializeCalculator();
        if(cursorPosition != 0) {
            removeCursor();
            cursorPosition--;
            backgroundInput = new StringBuilder(backgroundInput.substring(0, cursorPosition) + "|" + backgroundInput.substring(cursorPosition, backgroundInput.length()));
            viewLabel.setText(backgroundInput.toString());
        }
    }

    @FXML
    void moveRight() {
        reinitializeCalculator();
        if((cursorPosition + 1) != backgroundInput.length()) {
            removeCursor();
            cursorPosition++;
            backgroundInput = new StringBuilder(backgroundInput.substring(0, cursorPosition) + "|" + backgroundInput.substring(cursorPosition, backgroundInput.length()));
            viewLabel.setText(backgroundInput.toString());
        }
    }

    private int containChar(String number, char Ch) {
        for(int i=0; i<number.length(); i++)
            if(number.charAt(i) == Ch)
                return i;
        return -1;
    }


    private void setErrorMessage() {
        viewLabel.setTextFill(Color.RED);
        viewLabel.setText("Error: ecuatia introdusa nu a fost una valida!");
    }

    private void reinitializeCalculator() {
        if(bigNumberSignLabel.getText().length() > 0)
            bigNumberSignLabel.setText("");
        viewLabel.setTextFill(Color.rgb(1,156,255));
    }

    private void removeCursor() {
        reinitializeCalculator();
        for (int i = 0; i < backgroundInput.length(); i++)
            if(backgroundInput.charAt(i) == '|') {
                backgroundInput.deleteCharAt(i);
                i = backgroundInput.length();
            }
    }

    private void appendToString(String s) {
        reinitializeCalculator();
        removeCursor();
        backgroundInput = new StringBuilder(backgroundInput.substring(0,cursorPosition) + s + "|" + backgroundInput.substring(cursorPosition, backgroundInput.length()));
        viewLabel.setText(backgroundInput.toString());
        cursorPosition = cursorPosition + 1;
    }

    @FXML
    void addLeftBracket() {
        appendToString("(");
    }

    @FXML
    void addRightBracket() {
        appendToString(")");
    }

    @FXML
    void addDivide() {
        appendToString(":");
    }

    @FXML
    void addMultiply() {
        appendToString("*");
    }

    @FXML
    void addMinus() {
        appendToString("-");
    }

    @FXML
    void addPlus() {
        appendToString("+");
    }

    @FXML
    void addPow() { appendToString("^"); }

    @FXML
    void addPercentage() { appendToString("%");}

    @FXML
    void addComma() {
        appendToString(".");
    }

    @FXML
    void addOne() {
        appendToString("1");
    }

    @FXML
    void addTwo() {
        appendToString("2");
    }

    @FXML
    void addThree() {
        appendToString("3");
    }

    @FXML
    void addFour() {
        appendToString("4");
    }

    @FXML
    void addFive() {
        appendToString("5");
    }

    @FXML
    void addSix() {
        appendToString("6");
    }

    @FXML
    void addSeven() {
        appendToString("7");
    }

    @FXML
    void addEight() {
        appendToString("8");
    }

    @FXML
    void addNine() {
        appendToString("9");
    }

    @FXML
    void addZero() {
        appendToString("0");
    }

    private IntegerProperty buttonWidthProperty() {
        return buttonWidth;
    }
    private int getButtonWidth() {
        return buttonWidth.get();
    }
}
