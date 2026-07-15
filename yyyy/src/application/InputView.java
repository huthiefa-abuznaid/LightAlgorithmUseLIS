package application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class InputView {
    private Stage stage;

    public InputView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        Label label = new Label("Input Data From");
     label.setFont(new Font(20));
        RadioButton opt1 = new RadioButton("From User");
        RadioButton opt2 = new RadioButton("From File");
        
        
        ToggleGroup group = new ToggleGroup();
        opt1.setToggleGroup(group);
        opt2.setToggleGroup(group);
   
        opt1.setSelected(true); 

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setOnAction(e -> {
           
            RadioButton selected = (RadioButton) group.getSelectedToggle();
            Data resultView = new Data(stage,selected.getText());
            resultView.show(); 
        });

     VBox radio =new VBox( 10,opt1, opt2);
     radio.setAlignment(Pos.CENTER_LEFT);
     radio.setMaxWidth(120);
    
        VBox layout = new VBox(20, label, radio,confirmBtn);
        layout.setAlignment(Pos.CENTER); 

        Scene scene = new Scene(layout, 400, 400);
        stage.setTitle("Input Data");
        stage.setScene(scene);
        stage.show();
    }
}