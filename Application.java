
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class QuizGame extends Application {
    private Label questionLabel = new Label();
    private RadioButton[] options = new RadioButton[4];
    private ToggleGroup optionGroup = new ToggleGroup();
    private Button nextButton = new Button("Next");
    private Label timerLabel = new Label("Time Left: 15");
    private Label scoreLabel = new Label("Score: 0");

    private String[][] questions = {
        {"What is the capital of France?", "Paris", "London", "Berlin", "Rome", "Paris"},
        {"2 + 2 = ?", "3", "4", "5", "6", "4"},
        {"Java is a ___?", "Programming Language", "Fruit", "Car", "Bike", "Programming Language"}
    };

    private int currentQuestion = 0;
    private int score = 0;
    private int timeLeft = 15;
    private Timeline timer;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-font-size: 14px;");
        questionLabel.setWrapText(true);

        for (int i = 0; i < 4; i++) {
            options[i] = new RadioButton();
            options[i].setToggleGroup(optionGroup);
            root.getChildren().add(options[i]);
        }

        nextButton.setOnAction(e -> checkAnswerAndNext());
        root.getChildren().addAll(questionLabel, timerLabel, scoreLabel, nextButton);

        loadQuestion();

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Quiz Game with Timer");
        primaryStage.show();
    }

    private void loadQuestion() {
        if (currentQuestion >= questions.length) {
            endGame();
            return;
        }

        questionLabel.setText(questions[currentQuestion][0]);
        for (int i = 0; i < 4; i++) {
            options[i].setText(questions[currentQuestion][i + 1]);
            options[i].setSelected(false);
        }

        timeLeft = 15;
        timerLabel.setText("Time Left: " + timeLeft);

        if (timer != null) timer.stop();

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft);
            if (timeLeft <= 0) {
                timer.stop();
                checkAnswerAndNext();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void checkAnswerAndNext() {
        timer.stop();
        RadioButton selected = (RadioButton) optionGroup.getSelectedToggle();
        if (selected != null && selected.getText().equals(questions[currentQuestion][5])) {
            score++;
            scoreLabel.setText("Score: " + score);
        }
        currentQuestion++;
        loadQuestion();
    }

    private void endGame() {
        questionLabel.setText("Game Over! Final Score: " + score);
        for (RadioButton option : options) {
            option.setVisible(false);
        }
        nextButton.setDisable(true);
        timerLabel.setVisible(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
