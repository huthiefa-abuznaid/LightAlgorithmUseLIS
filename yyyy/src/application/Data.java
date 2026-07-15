package application;
//circle.setFill(Color.GREEN);
//circle.setFill(Color.RED);
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Data {
	private Stage stage;
	private String userChoice;

	private int maxSources = 0;
	private int currentCount = 0;
	private String[] enteredNumbers;

	private TextField inputField = new TextField();
	private TextField sourceLimitField = new TextField();
	private TextArea displayArea = new TextArea();
	private Button addBtn = new Button("➕ Add");
	private Button setLimitBtn = new Button("✔ Set Limit");
	private Button btDpTable = new Button("⚡ Run LIS & Show DP Table");

	// ── Colour palette
	// ────────────────────────────────────────────────────────────
	private static final String BG = "#0f1117";
	private static final String CARD = "#1a1d27";
	private static final String ACCENT = "#f5c518";
	private static final String ACCENT2 = "#3de8a0";
	private static final String TEXT = "#e8eaf6";
	private static final String MUTED = "#7986cb";
	private static final String PANEL_BG = "#252836";

	private static final String GLOBAL_CSS = ".root { -fx-background-color: " + BG + "; }"
			+ ".lbl-title { -fx-text-fill: " + ACCENT
			+ "; -fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: 'Segoe UI', sans-serif; }"
			+ ".lbl-sub   { -fx-text-fill: " + MUTED + "; -fx-font-size: 13px; -fx-font-family: 'Segoe UI'; }"
			+ ".btn-primary { -fx-background-color: " + ACCENT
			+ "; -fx-text-fill: #111; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 20; -fx-font-size: 13px; }"
			+ ".btn-primary:hover { -fx-background-color: #ffd740; }" + ".btn-accent2 { -fx-background-color: "
			+ ACCENT2
			+ "; -fx-text-fill: #0f1117; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20; -fx-font-size: 14px; }"
			+ ".btn-accent2:hover { -fx-background-color: #69f0ae; }"
			+ ".btn-back  { -fx-background-color: transparent; -fx-text-fill: " + MUTED + "; -fx-border-color: " + MUTED
			+ "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 6 18; }"
			+ ".btn-back:hover { -fx-text-fill: " + TEXT + "; -fx-border-color: " + TEXT + "; }"
			+ ".tf { -fx-background-color: " + PANEL_BG + "; -fx-text-fill: " + TEXT
			+ "; -fx-background-radius: 6; -fx-border-color: #3a3f5c; -fx-border-radius: 6; -fx-padding: 6 10; -fx-font-family: monospace; }"
			+ ".tf:focused { -fx-border-color: " + ACCENT + "; }" + ".ta { -fx-control-inner-background: " + PANEL_BG
			+ "; -fx-text-fill: " + TEXT + "; -fx-font-family: monospace; }"
			+ ".scroll-pane { -fx-background-color: transparent; -fx-background: transparent; }"
			+ ".scroll-pane .viewport { -fx-background-color: transparent; }";

	public Data(Stage stage, String choice) {
		this.stage = stage;
		this.userChoice = choice;
	}

	private Label styled(String text, String styleClass) {
		Label l = new Label(text);
		l.getStyleClass().add(styleClass);
		return l;
	}

	public void show() {
		Label title = styled("⚡ Max LED Lighting", "lbl-title");
		Label sub = styled("Input mode: " + userChoice, "lbl-sub");

		VBox dynamicContent = new VBox(12);
		dynamicContent.setAlignment(Pos.CENTER);

		switch (userChoice.toLowerCase()) {
		case "from user":
			setupUIProperties();
			setupActions();
			configureUserInterface(dynamicContent);
			break;
		case "from file":
			configureFileInterface(dynamicContent);
			break;
		}

		btDpTable.getStyleClass().add("btn-accent2");
		btDpTable.setMaxWidth(Double.MAX_VALUE);
		btDpTable.setOnAction(e -> runLISAndShowResult());

		Button backBtn = new Button("← Back");
		backBtn.getStyleClass().add("btn-back");
		backBtn.setOnAction(e -> new InputView(stage).show());

		VBox layout = new VBox(18, title, sub, dynamicContent, btDpTable, backBtn);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(30, 40, 30, 40));

		Scene scene = new Scene(layout, 520, 620);

		try {
			java.io.File cssFile = java.io.File.createTempFile("led_style", ".css");
			cssFile.deleteOnExit();
			java.nio.file.Files.writeString(cssFile.toPath(), GLOBAL_CSS);
			scene.getStylesheets().add(cssFile.toURI().toString());
		} catch (Exception ex) {
			System.err.println("CSS load warning: " + ex.getMessage());
		}

		stage.setScene(scene);
		stage.setTitle("Max LED Lighting — " + userChoice);
		stage.show();
	}

	// ── User-input mode
	// ───────────────────────────────────────────────────────────

	private void setupUIProperties() {
		sourceLimitField.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
			char ch = event.getCharacter().charAt(0);
			String current = sourceLimitField.getText();
			if (current.isEmpty()) {
				if (ch < '1' || ch > '9') {
					event.consume();
					return;
				}
			} else {
				if (ch < '0' || ch > '9') {
					event.consume();
					return;
				}
			}
			int next = 0;
			// mix numbers
			for (char c : (current + ch).toCharArray()) {
				next = next * 10 + (c - '0');
			}

		});
		sourceLimitField.setPromptText("e.g.  6");
		sourceLimitField.getStyleClass().add("tf");
		sourceLimitField.setMaxWidth(120);

		inputField.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
			char ch = event.getCharacter().charAt(0);
			String current = inputField.getText();
			if (current.isEmpty()) {
				if (ch < '1' || ch > '9') {
					event.consume();
					return;
				}
			} else {
				if (ch < '0' || ch > '9') {
					event.consume();
					return;
				}
			}
			int next = 0;
			// mix numbers
			for (char c : (current + ch).toCharArray()) {
				next = next * 10 + (c - '0');
			}
			if (next > maxSources)
				event.consume();
		});
		inputField.setPromptText("Enter value…");
		inputField.getStyleClass().add("tf");
		inputField.setMaxWidth(120);
		inputField.setDisable(true);

		displayArea.setEditable(false);
		displayArea.setPrefHeight(130);
		displayArea.getStyleClass().add("ta");

		addBtn.getStyleClass().add("btn-primary");
		addBtn.setDisable(true);
		setLimitBtn.getStyleClass().add("btn-primary");
	}

	private void setupActions() {
		sourceLimitField.setOnAction(e -> handleSetLimit());
		inputField.setOnAction(e -> handleAddEntry());
		setLimitBtn.setOnAction(e -> handleSetLimit());
		addBtn.setOnAction(e -> handleAddEntry());
	}

	private void handleSetLimit() {
		String raw = sourceLimitField.getText().trim();
		if (raw.isEmpty()) {
			showAlert("Missing Input", "Please enter a number of LEDs.");
			return;
		}
		int val = Integer.parseInt(raw);
		if (val <= 0) {
			showAlert("Invalid Input", "Limit must be greater than 0.");
			sourceLimitField.clear();
			return;
		}
		maxSources = val;
		currentCount = 0;
		enteredNumbers = new String[maxSources];
		displayArea.clear();
		displayArea.appendText(
				"Limit set to " + maxSources + "  (enter " + maxSources + " unique positive values)\n─────────────\n");
		inputField.setDisable(false);
		addBtn.setDisable(false);
		sourceLimitField.setDisable(true);
		setLimitBtn.setDisable(true);
	}

	private void handleAddEntry() {
		if (currentCount >= maxSources)
			return;

		String value = inputField.getText().trim();
		if (value.isEmpty())
			return;

		for (int i = 0; i < currentCount; i++) {
			if (enteredNumbers[i].equals(value)) {
				// Reset
				currentCount = 0;
				enteredNumbers = new String[maxSources];

				inputField.setDisable(false);
				addBtn.setDisable(false);

				displayArea.appendText("dupplicated \n");
				displayArea.appendText(" rest  \n");
				displayArea.appendText("─────────────\n");

				inputField.clear();
				return; 
			}
		}

		enteredNumbers[currentCount] = value;
		currentCount++;
		displayArea.appendText("  [" + currentCount + "] → " + value + "\n");

		if (currentCount == maxSources) {
			displayArea.appendText("─────────────\n✅ All " + maxSources + " values collected.\n");
			inputField.setDisable(true);
			addBtn.setDisable(true);
		}
		inputField.clear();
	}

	private void configureUserInterface(VBox container) {
		VBox step1 = new VBox(8, styled("Step 1 — Set how many LEDs", "lbl-sub"),
				new HBox(10, sourceLimitField, setLimitBtn));
		((HBox) step1.getChildren().get(1)).setAlignment(Pos.CENTER_LEFT);
		styleCard(step1);

		VBox step2 = new VBox(8, styled("Step 2 — Enter LED values", "lbl-sub"), new HBox(10, inputField, addBtn));
		((HBox) step2.getChildren().get(1)).setAlignment(Pos.CENTER_LEFT);
		styleCard(step2);

		container.getChildren().addAll(step1, step2, displayArea);
	}

	// ── File-input mode
	// ───────────────────────────────────────────────────────────

	private void configureFileInterface(VBox container) {
		displayArea.setEditable(false);
		displayArea.setPrefHeight(160);
		displayArea.getStyleClass().add("ta");

		Button selectFileBtn = new Button("📂  Select File");
		selectFileBtn.getStyleClass().add("btn-primary");

		selectFileBtn.setOnAction(e -> {
			FileChooser fc = new FileChooser();
			fc.setTitle("Open LED Data File");
			fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text / CSV Files", "*.txt", "*.csv"),
					new FileChooser.ExtensionFilter("All Files", "*.*"));
			String home = System.getProperty("user.home");
			File desk = new File(home + File.separator + "Desktop");
			if (desk.exists())
				fc.setInitialDirectory(desk);

			File file = fc.showOpenDialog(stage);
			if (file != null) {
				try {
					Scanner fs = new Scanner(file);
					StringBuilder sb = new StringBuilder();
					while (fs.hasNextLine())
						sb.append(fs.nextLine()).append(" ");
					fs.close();

					String result = fillArray(sb.toString());
					displayArea.setText(result + "\n─────────────\n");
					for (String val : enteredNumbers)
						displayArea.appendText("  " + val + "\n");
				} catch (FileNotFoundException ex) {
					displayArea.setText("❌ File not found.");
				}
			}
		});

		VBox card = new VBox(10, styled("Upload a file with comma/space-separated numbers:", "lbl-sub"), selectFileBtn,
				displayArea);
		styleCard(card);
		container.getChildren().add(card);
	}

//put number on fill and take only numbers 
	private String fillArray(String text) {
		currentCount = 0;
		if (text == null || text.isBlank())
			return ". . . Empty Input . . .";

		String[] temp = new String[1000];
		int tempCount = 0;
		int dupCount = 0;

		Scanner scan = new Scanner(text);
		scan.useDelimiter("[,\\s]+");

		while (scan.hasNext()) {
			String tok = scan.next().trim();
			if (tok.isEmpty())
				continue;

			char[] chars = tok.toCharArray();
			int validChars = 0;
			for (char c : chars)
				if (c >= '0' && c <= '9')
					validChars++;

			int value = 0;
			for (char c : chars)
				value = value * 10 + (c - '0');

			if (validChars != chars.length || value <= 0)
				continue;

			int foundAt = -1;
			for (int i = 0; i < tempCount; i++) {
				if (temp[i].equals(tok)) {
					foundAt = i;
					break;
				}
			}

			if (foundAt == -1) {
				temp[tempCount++] = tok;
			} else {
				dupCount++;
			}
		}
		scan.close();

		enteredNumbers = new String[tempCount];
		for (int i = 0; i < tempCount; i++)
			enteredNumbers[i] = temp[i];
		currentCount = tempCount;

		return dupCount > 0 ? "File processed — duplicates ignored. Items: " + currentCount
				: "File loaded successfully. Items: " + currentCount;
	}
	// ── LIS computation & result screen ──────────────────────────────────────────

	private void runLISAndShowResult() {
		if (currentCount == 0) {
			showAlert("No Data", "Please enter or load some numbers first.");
			return;
		}

		int n = currentCount;
		int[] L = new int[n];
		for (int i = 0; i < n; i++) {
			try {
				L[i] = Integer.parseInt(enteredNumbers[i]);
			} catch (NumberFormatException ex) {
				showAlert("Bad Input", "Non-integer value: " + enteredNumbers[i]);
				return;
			}
		}
		// make dp
		LIS lis = new LIS();
		int[] lisValues = lis.getLISValues(L);
		int[] lisIndices = lis.indexValueForTabel(L);
		// souce
		int[] lisFlag = new int[n];
		for (int idx : lisIndices)
			lisFlag[idx] = 1;

		int[] dp = lis.getDPTable(L);

		Stage resultStage = new Stage();
		resultStage.setTitle("LIS Result — Max LEDs: " + lisValues.length);

		Label summaryLbl = new Label("Max LEDs lit: " + lisValues.length);
		summaryLbl.setFont(Font.font("Monospace", FontWeight.BOLD, 20));
		summaryLbl.setTextFill(Color.web(ACCENT));

		StringBuilder lisStr = new StringBuilder("LED sequence: [ ");
		// lis dp
		for (int v : lisValues)
			lisStr.append(v).append("  ");
		lisStr.append("]");
		Label lisLbl = new Label(lisStr.toString());
		lisLbl.setFont(Font.font("Monospace", 14));
		lisLbl.setTextFill(Color.web(ACCENT2));

		Label tableTitle = new Label("DP Table");
		tableTitle.setFont(Font.font("Monospace", FontWeight.BOLD, 15));
		tableTitle.setTextFill(Color.web(MUTED));

		GridPane grid = new GridPane();
		grid.setHgap(2);
		grid.setVgap(2);
		grid.setPadding(new Insets(8));

		String[] headers = { "Index", "L[i]  (LED)", "Source", "dp[i]  (LIS len)", "LED" };
		for (int c = 0; c < headers.length; c++) {
			Label h = new Label("  " + headers[c] + "  ");
			h.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
			h.setTextFill(Color.web("#111"));
			h.setStyle("-fx-background-color: " + ACCENT + "; -fx-padding: 6 10;");
			h.setMaxWidth(Double.MAX_VALUE);
			GridPane.setHgrow(h, Priority.ALWAYS);
			grid.add(h, c, 0);
		}

		for (int i = 0; i < n; i++) {
			int inLIS = lisFlag[i];
			String rowBg = inLIS == 1 ? "#1e3a2f" : "#1a1d27";

			String[] cells = { String.valueOf(i + 1), // Index (1-based)
					String.valueOf(L[i]), // L[i] LED value
					String.valueOf(L[i]), // Source (mirrors LED value)
					String.valueOf(dp[i]), // dp[i] = LIS length ending at i
					inLIS == 1 ? "💡" : "—" // In LIS?
			};

			for (int c = 0; c < cells.length; c++) {
				Label cell = new Label("  " + cells[c] + "  ");
				cell.setFont(Font.font("Monospace", 13));
				cell.setTextFill(inLIS == 1 ? Color.web(ACCENT2) : Color.web(TEXT));
				cell.setStyle("-fx-background-color: " + rowBg + "; -fx-padding: 5 10;");
				cell.setMaxWidth(Double.MAX_VALUE);
				GridPane.setHgrow(cell, Priority.ALWAYS);
				grid.add(cell, c, i + 1);
			}
		}

		ScrollPane scroll = new ScrollPane(grid);
		scroll.setFitToWidth(true);
		scroll.getStyleClass().add("scroll-pane");
		scroll.setStyle("-fx-border-color: #252836; -fx-border-radius: 8; -fx-background-radius: 8;");
		scroll.setPrefHeight(320);

		Label vizTitle = new Label("💡  Circuit Visualisation");
		vizTitle.setFont(Font.font("Monospace", FontWeight.BOLD, 14));
		vizTitle.setTextFill(Color.web(MUTED));

		HBox leds = new HBox(12);
		leds.setAlignment(Pos.CENTER);
		leds.setPadding(new Insets(14));
		leds.setStyle("-fx-background-color: " + CARD + ";");
		for (int i = 0; i < n; i++) {
			VBox bulb = new VBox(4);
			bulb.setAlignment(Pos.CENTER);
			Label icon = new Label(lisFlag[i] == 1 ? "💡" : "⚫");
			icon.setFont(Font.font(24));
			Label val = new Label(String.valueOf(L[i]));
			val.setFont(Font.font("Monospace", FontWeight.BOLD, 12));
			val.setTextFill(lisFlag[i] == 1 ? Color.web(ACCENT) : Color.web(MUTED));
			bulb.getChildren().addAll(icon, val);
		leds.getChildren().add(bulb);
		}

		ScrollPane ledScroll = new ScrollPane(leds);
		ledScroll.setFitToWidth(true);
		ledScroll.getStyleClass().add("scroll-pane");
		ledScroll.setStyle("-fx-border-color: #252836; -fx-border-radius: 8; -fx-background-radius: 8;");
		ledScroll.setPrefHeight(95);

		Button closeBtn = new Button("Close Panel");
		closeBtn.getStyleClass().add("btn-primary");
		closeBtn.setOnAction(e -> resultStage.close());

		VBox root = new VBox(16, summaryLbl, lisLbl, new Separator(), tableTitle, scroll, new Separator(), vizTitle,
				ledScroll, closeBtn);
		root.setPadding(new Insets(24));
		root.setAlignment(Pos.TOP_CENTER);
		root.setStyle("-fx-background-color: " + BG + ";");

		Scene resultScene = new Scene(root, 680, 720);
		resultStage.setScene(resultScene);
		resultStage.show();
	}

	private void styleCard(VBox box) {
		box.setStyle("-fx-background-color: " + CARD + "; -fx-background-radius: 12; -fx-padding: 16;");
		box.setMaxWidth(Double.MAX_VALUE);
	}

	private void showAlert(String title, String msg) {
		Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
		a.setTitle(title);
		a.setHeaderText(null);
		a.showAndWait();
	}
}