package edu.ntnu.idatx1002;

import edu.ntnu.idatx1002.priority.*;
import edu.ntnu.idatx1002.status.ClosedStatus;
import edu.ntnu.idatx1002.status.OpenStatus;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This class maintains the GUI and all methods to run the application
 *  @author KOHORT 1.7
 *  @version 2020-04-26
 */
public class JavaFX extends Application implements EventHandler<ActionEvent> {
    private List mainList;
    private ArrayList<String> categoryList = new ArrayList<>();
    private Task activeTask;
    private FileManagement fileMana;
    private String activeCategory;
    private Stage publicStage;
    private Scene publicScene, newTaskScene, todayScene, upcomingScene, calendarScene, highPriorityScene,
            mediumPriorityScene, lowPriorityScene, categoriesScene, redigerTestScene,byDayScene, byCategoryScene;
    private Button newTaskButton;
    private Button todayButton;
    private Button upcomingButton;
    private Button calendarButton;
    private Button highPriorityButton;
    private Button mediumPriorityButton;
    private Button lowPriorityButton;
    private Button add1;
    private Button add1Edit;
    private Button deleteTask;
    private Button backToCalendarButton;
    private Button helpSettings;
    private Button back;
    private Button backEdit;
    private MenuButton categoriesMenuButton;
    private TextField taskName2;
    private TextField taskName2Edit;
    private TextArea description2, description2Edit;
    private ComboBox category2, box, category2Edit, boxEdit, openClosedBox;
    private DatePicker picker1, picker1Edit, datePickerCalendar;
    private AnchorPane menuAnchorPane;
    private ScrollPane menuScrollPane;
    private ContextMenu contextMenu;
    private TableView<Task> todayTableView, upcomingTableView, highTableView, mediumTableView, lowTableView,
            byDayTableView, byCategoryTableView;
    private TableColumn<Task, String> taskNameColumn, descriptionColumn, categoryColumn,dateColumn;
    private LocalDate chosen;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        initializer();
        publicStage = stage;

        //Setting a starting scene and stage initialisation
        try {
            stage.getIcons().add(new Image("file:src/main/resources/icon.png"));
            stage.setTitle("2DO");
            stage.setResizable(false);
            stage.setScene(todayScene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startupFileChecker(){
        fileMana = new FileManagement();
        mainList = fileMana.getMainList();
    }

    //Adding, removing and editing methods
    /**
     * Add task method 2.0, gets information from add task window and creates new task.
     * @param taskName Text field task name
     * @param taskDesc Text area task description
     * @param cat Combo boc task category
     * @param start Date picker start date
     * @param priority Combo box priority(High/Medium/Low)
     */
    private void addTaskBetter(TextField taskName, TextArea taskDesc, ComboBox cat, DatePicker start, ComboBox priority){
        Task t = new Task("No", "Data", "Entered");
        String taskNameS;
        String taskDescS = taskDesc.getText();

        String catS = "Default";
        LocalDate startD = start.getValue();

        if (!(taskName.getText().isEmpty())){
            taskNameS = taskName.getText();
            if (!(cat.getValue() == null)){
                catS = cat.getValue().toString();
            }
            if (!(priority.getValue().toString().isEmpty())){
                if (priority.getValue().toString().equalsIgnoreCase("low")){
                    t = new Task(taskNameS, taskDescS, catS, startD, new LowPriority());
                }
                if (priority.getValue().toString().equalsIgnoreCase("medium")){
                    t = new Task(taskNameS, taskDescS, catS, startD, new MediumPriority());
                }
                if (priority.getValue().toString().equalsIgnoreCase("high")){
                    t = new Task(taskNameS, taskDescS, catS, startD, new HighPriority());
                }
            } else {
                t = new Task(taskNameS, taskDescS, catS, startD);
            }
            mainList.addTask(t);
        }
        fileMana.save(mainList);

    }
    /**
     * Sets active task to chosen task
     * Initializes leftMenu and editTask view
     * Sets parameters for editTask view from active task
     * @param l Table view item that is selected
     */
    private void editTaskWindow(TableView<Task> l){
        if (l.getSelectionModel().getSelectedItem() instanceof Task) {

            activeTask = (Task) l.getSelectionModel().getSelectedItem();

            leftMenuInit();
            editTaskInit();

            taskName2Edit.setText(activeTask.getTaskName());
            description2Edit.setText(activeTask.getTaskDescriptionFull());
            category2Edit.setValue(activeTask.getCategory());

            picker1Edit.setValue(activeTask.getStartDate());
            boxEdit.setValue(activeTask.getPriority().toString());
            if (activeTask.getStatus().getStatus()){
                openClosedBox.setValue("Open");
            }else{
                openClosedBox.setValue("Closed");
            }

            publicStage.setScene(redigerTestScene);
        }
    }
    private void editTaskCommit(){
        activeTask.setTaskName(taskName2Edit.getText());
        activeTask.setTaskDescription(description2Edit.getText());
        activeTask.setCategory(category2Edit.getValue().toString());
        activeTask.setStartDate(picker1Edit.getValue());

        if(boxEdit.getValue().toString().equalsIgnoreCase("low")) {
            activeTask.setPriority(new LowPriority());
        }

        if(boxEdit.getValue().toString().equalsIgnoreCase("medium")) {
            activeTask.setPriority(new MediumPriority());
        }

        if(boxEdit.getValue().toString().equalsIgnoreCase("high")) {
            activeTask.setPriority(new HighPriority());
        }

        if (openClosedBox.getValue().toString().equalsIgnoreCase("closed")){
            activeTask.setStatus(new ClosedStatus());
        }

        if (openClosedBox.getValue().toString().equalsIgnoreCase("open")){
            activeTask.setStatus(new OpenStatus());
        }

    }

    /**
     * Updates and refreshes program lists
     */
    private void refresh(){
        todayTableView.getItems().clear();
        for(Task task: mainList.getTodaysTasks()){
            todayTableView.getItems().add(task);
        }

        upcomingTableView.getItems().clear();
        for(Task task: mainList.getUpcomingTasks()){
            upcomingTableView.getItems().add(task);
        }

        highTableView.getItems().clear();
        for(Task task: mainList.getTasksByPriority(new HighPriority())){
            highTableView.getItems().add(task);
        }

        mediumTableView.getItems().clear();
        for(Task task: mainList.getTasksByPriority(new MediumPriority())){
            mediumTableView.getItems().add(task);
        }

        lowTableView.getItems().clear();
        for(Task task: mainList.getTasksByPriority(new LowPriority())){
            lowTableView.getItems().add(task);
        }
        byDayTableView.getItems().clear();
        for(Task task: mainList.getTaskByDay(chosen)){
            byDayTableView.getItems().add(task);
        }

        byCategoryTableView.getItems().clear();
        for (Task task : mainList.getTasksByCategory(activeCategory)){
            byCategoryTableView.getItems().add(task);
        }

    }

    /**
     *Styling and non-page specific methods
     */
    private void contextMenu(TableView<Task> c){
        contextMenu = new ContextMenu();


        MenuItem addTaskMI = new MenuItem("Add Task");
        MenuItem editMI = new MenuItem("Edit");
        MenuItem removeMI = new MenuItem("Remove");
        MenuItem closeTaskMI = new MenuItem("Finish / re-open");


        
        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(addTaskMI, editMI, removeMI, closeTaskMI);


        c.setOnContextMenuRequested(event -> {
            if (c.getSelectionModel().getSelectedItem() instanceof Task) {
                if (c.getSelectionModel().getSelectedItem().getStatus().getStatus()) {
                    closeTaskMI.setText("Finish");
                } else if (!(c.getSelectionModel().getSelectedItem().getStatus().getStatus())) {
                    closeTaskMI.setText("Re-open");
                }
            }
            contextMenu.show(c.getScene().getWindow(), event.getScreenX(), event.getScreenY());
        });

        //Action for the menuItems
        addTaskMI.setOnAction(ActionEvent -> {
            leftMenuInit();
            addTaskInit();
            publicStage.setScene(newTaskScene);
            menuButtonStyling(newTaskButton);
        });

        editMI.setOnAction(ActionEvent -> editTaskWindow(c));

        removeMI.setOnAction(ActionEvent -> {
            if (c.getSelectionModel().getSelectedItem() instanceof Task){
                mainList.removeTask((Task) c.getSelectionModel().getSelectedItem());
                fileMana.save(mainList);
                refresh();
            }
        });

        closeTaskMI.setOnAction(ActionEvent -> {
            if (c.getSelectionModel().getSelectedItem() instanceof Task){
                activeTask = c.getSelectionModel().getSelectedItem();
                if (activeTask.getStatus().getStatus()){
                    activeTask.setStatus(new ClosedStatus());
                }else if (!(activeTask.getStatus().getStatus())){
                    activeTask.setStatus(new OpenStatus());
                }
                fileMana.save(mainList);
                refresh();
            }
        });

    }

    private void menuButtonStyling(Object btn) {
        if (btn instanceof Button || btn instanceof MenuButton) {
            newTaskButton.getStylesheets().removeAll("file:src/main/resources/PressedMenuButton.css");
            todayButton.getStylesheets().removeAll("file:src/main/resources/PressedMenuButton.css");
            upcomingButton.getStylesheets().removeAll("file:src/main/resources/PressedMenuButton.css");
            calendarButton.getStylesheets().removeAll("file:src/main/resources/PressedMenuButton.css");
            highPriorityButton.getStylesheets().removeAll("file:src/main/resources/PressedMenuButton.css");
            mediumPriorityButton.getStylesheets().removeAll("file:src/main/resources/PressedMenuButton.css");
            lowPriorityButton.getStylesheets().removeAll("file:src/main/resources/PressedMenuButton.css");
            categoriesMenuButton.getStylesheets().removeAll("file:src/main/resources/PressedMenuButton.css");
            helpSettings.getStylesheets().removeAll("file:src/main/resources/PressedMenuButton.css");
            if (btn instanceof Button){
                Button bta = (Button) btn;
                bta.getStylesheets().addAll("file:src/main/resources/PressedMenuButton.css");
            }else if (btn instanceof MenuButton){
                MenuButton bta = (MenuButton) btn;
                bta.getStylesheets().addAll("file:src/main/resources/PressedMenuButton.css");
            }
        }
    }

    /**
     * Initialization JavaFX
     */
    private void initializer(){
        startupFileChecker();
        menuButtonInit();
        leftMenuInit();
        addTaskInit();
        editTaskInit();
        tableViewColumnsInit();
        upcomingInit();
        highInit();
        medInit();
        lowInit();
        calendarInit();
        byDayInit(datePickerCalendar);
        todayInit();
        categoryInit();

        //Initial values needed for program and methods
        publicScene = todayScene;
        byCategoryTableView = new TableView<>();
    }
    private void menuButtonInit(){
        newTaskButton = new Button("New Task");
        todayButton = new Button("Today");
        upcomingButton = new Button("Upcoming");
        calendarButton = new Button("Calendar");
        highPriorityButton = new Button("High Priority");
        mediumPriorityButton = new Button("Medium Priority");
        lowPriorityButton = new Button("Low Priority");
        categoriesMenuButton = new MenuButton("        Categories");
        helpSettings = new Button("Settings/Help");

        newTaskButton.setLayoutY(0);
        todayButton.setLayoutY(47);
        upcomingButton.setLayoutY(94);
        calendarButton.setLayoutY(141);
        highPriorityButton.setLayoutY(220);
        mediumPriorityButton.setLayoutY(267);
        lowPriorityButton.setLayoutY(314);
        categoriesMenuButton.setLayoutY(361);
        helpSettings.setLayoutY(650);

        newTaskButton.getStylesheets().addAll("file:src/main/resources/MenuButton.css");
        todayButton.getStylesheets().addAll("file:src/main/resources/MenuButton.css");
        upcomingButton.getStylesheets().addAll("file:src/main/resources/MenuButton.css");
        calendarButton.getStylesheets().addAll("file:src/main/resources/MenuButton.css");
        highPriorityButton.getStylesheets().addAll("file:src/main/resources/MenuButton.css");
        mediumPriorityButton.getStylesheets().addAll("file:src/main/resources/MenuButton.css");
        lowPriorityButton.getStylesheets().addAll("file:src/main/resources/MenuButton.css");
        categoriesMenuButton.getStylesheets().addAll("file:src/main/resources/MenuButton.css");

        helpSettings.getStylesheets().addAll("file:src/main/resources/MenuButton.css");

        newTaskButton.setOnAction(this);
        todayButton.setOnAction(this);
        upcomingButton.setOnAction(this);
        calendarButton.setOnAction(this);
        highPriorityButton.setOnAction(this);
        mediumPriorityButton.setOnAction(this);
        lowPriorityButton.setOnAction(this);
        categoriesMenuButton.setOnAction(this);
        helpSettings.setOnAction(this);
    }

    private void leftMenuInit(){
        menuAnchorPane = new AnchorPane();
        menuScrollPane = new ScrollPane(menuAnchorPane);

            for (Task t : mainList.getTaskList()) {
                if (!(categoryList.contains(t.getCategory()))) {
                    MenuItem categoryMenuItem = new MenuItem(t.getCategory());
                    categoryMenuItem.setOnAction(e -> getCategoryList(t.getCategory()));
                    categoryMenuItem.setStyle("-fx-Pref-Width: 205;");
                    categoriesMenuButton.getItems().add(categoryMenuItem);
                    categoryList.add(t.getCategory());
                }
            }

        AnchorPane.setRightAnchor(menuAnchorPane, 220.0);
        menuAnchorPane.setPrefWidth(200);
        menuScrollPane.setPrefWidth(200);

        menuAnchorPane.getChildren().addAll(newTaskButton, todayButton, upcomingButton, calendarButton,
                highPriorityButton, mediumPriorityButton, lowPriorityButton, categoriesMenuButton, helpSettings);
    }
    private void addTaskInit(){
        ImageView backButtonImage = new ImageView(new Image("file:src/main/resources/arrow3.png"));
        backButtonImage.setFitHeight(24);
        backButtonImage.setPreserveRatio(true);

        back = new Button();
        back.setGraphic(backButtonImage);
        Text taskName1 = new Text("Task name");
        Text description1 = new Text("Description");
        Text category1 = new Text("Category");
        Text date1 = new Text("Date");
        Text date2 = new Text("Start");
        Text priority = new Text("Priority");

        taskName2 = new TextField();
        description2 = new TextArea();
        category2 = new ComboBox<String>();
        picker1 = new DatePicker();
        box = new ComboBox<String>();
        add1 = new Button("Add");

        category2.setEditable(true);
        picker1.setValue(LocalDate.now());

        box.getItems().addAll("low", "medium", "high");
        box.getSelectionModel().selectFirst();
        for (Task t:mainList.getTaskList()) {
            if (!(category2.getItems().contains(t.getCategory()))){
                category2.getItems().add(t.getCategory());
            }
        }

        taskName1.setLayoutX(200);
        taskName2.setLayoutX(400);
        description1.setLayoutX(200);
        description2.setLayoutX(400);
        category1.setLayoutX(200);
        category2.setLayoutX(400);
        date1.setLayoutX(200);
        date2.setLayoutX(340);
        picker1.setLayoutX(400);
        priority.setLayoutX(200);
        box.setLayoutX(400);
        add1.setLayoutX(400);

        taskName1.setLayoutY(150);
        taskName2.setLayoutY(127);
        description1.setLayoutY(210);
        description2.setLayoutY(187);
        category1.setLayoutY(335);
        category2.setLayoutY(315);
        date1.setLayoutY(395);
        date2.setLayoutY(395);
        picker1.setLayoutY(377);
        priority.setLayoutY(455);
        box.setLayoutY(437);
        add1.setLayoutY(495);

        taskName2.setPrefWidth(300);
        description2.setPrefWidth(300);
        category2.setPrefWidth(300);
        add1.setPrefWidth(300);
        back.setPrefWidth(90);

        taskName2.setPrefHeight(35);
        description2.setPrefHeight(100);
        category2.setPrefHeight(35);
        add1.setPrefHeight(35);

        add1.getStylesheets().addAll("file:src/main/resources/PressedMenuButton.css");
        back.getStylesheets().addAll("file:src/main/resources/PressedMenuButton.css");

        taskName1.setStyle("-fx-font: 22 arial");
        description1.setStyle("-fx-font: 22 arial");
        category1.setStyle("-fx-font: 22 arial");
        date1.setStyle("-fx-font: 22 arial");
        date2.setStyle("-fx-font: 19 arial");
        priority.setStyle("-fx-font: 22 arial");

        SplitPane newTaskSplitPane = new SplitPane();
        newTaskSplitPane.setDividerPositions(0.20);
        AnchorPane newTaskAnchorPane = new AnchorPane();
        ScrollPane newTaskScrollPane = new ScrollPane(newTaskAnchorPane);

        newTaskSplitPane.getItems().add(menuScrollPane);
        newTaskSplitPane.getItems().add(newTaskScrollPane);


        newTaskAnchorPane.getChildren().addAll(taskName1, taskName2, description1, description2, category1, category2
                , date1, picker1, priority, box, add1, back);

        newTaskScene = new Scene(newTaskSplitPane, 1150, 700);

        add1.setOnAction(this);
        back.setOnAction(this);

    }
    private void editTaskInit(){
        ImageView backButtonImageEdit = new ImageView(new Image("file:src/main/resources/arrow3.png"));
        backButtonImageEdit.setFitHeight(24);
        backButtonImageEdit.setPreserveRatio(true);

        Text taskNameEdit = new Text("Task name");
        Text descriptionEdit = new Text("Description");
        Text categoryEdit = new Text("Category");
        Text dateEdit = new Text("Date");
        Text date2Edit = new Text("Start");
        Text priorityEdit = new Text("Priority");
        Text openClosed = new Text("Open/closed");

        backEdit = new Button();
        backEdit.setGraphic(backButtonImageEdit);
        taskName2Edit = new TextField();
        description2Edit = new TextArea();
        category2Edit = new ComboBox<String>();
        picker1Edit = new DatePicker();
        boxEdit = new ComboBox<String>();
        openClosedBox = new ComboBox<String>();
        add1Edit = new Button("Apply");
        deleteTask = new Button("Delete");

        category2Edit.setEditable(true);
        for (Task t:mainList.getTaskList()) {
            if (!(category2Edit.getItems().contains(t.getCategory()))){
                category2Edit.getItems().add(t.getCategory());
            }
        }

        boxEdit.getItems().addAll("low", "medium", "high");
        boxEdit.getSelectionModel().selectFirst();

        SplitPane redigerTestSplitPane = new SplitPane();
        redigerTestSplitPane.setDividerPositions(0.20);
        AnchorPane redigerTestAnchorPane = new AnchorPane();
        ScrollPane redigerTestScrollPane = new ScrollPane(redigerTestAnchorPane);
        redigerTestSplitPane.getItems().add(menuAnchorPane);
        redigerTestSplitPane.getItems().add(redigerTestScrollPane);
        redigerTestScene = new Scene(redigerTestSplitPane, 1150, 700);

        openClosedBox.getItems().addAll("Open", "Closed");
        openClosedBox.getSelectionModel().selectFirst();

        taskNameEdit.setLayoutX(200);
        taskName2Edit.setLayoutX(400);
        descriptionEdit.setLayoutX(200);
        description2Edit.setLayoutX(400);
        categoryEdit.setLayoutX(200);
        category2Edit.setLayoutX(400);
        dateEdit.setLayoutX(200);
        date2Edit.setLayoutX(340);
        picker1Edit.setLayoutX(400);
        priorityEdit.setLayoutX(200);
        openClosed.setLayoutX(200);
        boxEdit.setLayoutX(400);
        openClosedBox.setLayoutX(400);
        add1Edit.setLayoutX(400);
        deleteTask.setLayoutX(560);

        taskNameEdit.setLayoutY(150);
        taskName2Edit.setLayoutY(127);
        descriptionEdit.setLayoutY(210);
        description2Edit.setLayoutY(187);
        categoryEdit.setLayoutY(335);
        category2Edit.setLayoutY(315);
        dateEdit.setLayoutY(395);
        date2Edit.setLayoutY(395);
        picker1Edit.setLayoutY(377);
        priorityEdit.setLayoutY(455);
        openClosed.setLayoutY(505);
        boxEdit.setLayoutY(437);
        openClosedBox.setLayoutY(487);
        add1Edit.setLayoutY(540);
        deleteTask.setLayoutY(540);

        taskName2Edit.setPrefWidth(300);
        description2Edit.setPrefWidth(300);
        category2Edit.setPrefWidth(300);
        add1Edit.setPrefWidth(150);
        deleteTask.setPrefWidth(150);
        backEdit.setPrefWidth(90);

        taskName2Edit.setPrefHeight(35);
        description2Edit.setPrefHeight(100);
        category2Edit.setPrefHeight(35);
        add1Edit.setPrefHeight(35);
        deleteTask.setPrefHeight(35);

        add1Edit.getStylesheets().addAll("file:src/main/resources/PressedMenuButton.css");
        deleteTask.getStylesheets().addAll("file:src/main/resources/DeleteButton.css");
        backEdit.getStylesheets().addAll("file:src/main/resources/PressedMenuButton.css");

        taskNameEdit.setStyle("-fx-font: 22 arial");
        descriptionEdit.setStyle("-fx-font: 22 arial");
        categoryEdit.setStyle("-fx-font: 22 arial");
        dateEdit.setStyle("-fx-font: 22 arial");
        date2Edit.setStyle("-fx-font: 19 arial");
        priorityEdit.setStyle("-fx-font: 22 arial");
        openClosed.setStyle("-fx-font: 22 arial");


        redigerTestAnchorPane.getChildren().addAll(taskNameEdit, taskName2Edit, descriptionEdit, description2Edit, categoryEdit, category2Edit
                , dateEdit, picker1Edit, priorityEdit, openClosed,openClosedBox, boxEdit, add1Edit, deleteTask, backEdit);


        add1Edit.setOnAction(this);
        deleteTask.setOnAction(this);
        backEdit.setOnAction(this);
    }
    private void tableViewColumnsInit(){
        taskNameColumn = new TableColumn<>("Task Name");
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        taskNameColumn.setPrefWidth(242);

        descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        descriptionColumn.setPrefWidth(322);

        categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setPrefWidth(162);

        dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        dateColumn.setPrefWidth(122);

    }
    private void todayInit(){
        AnchorPane todayAnchorPane = new AnchorPane();
        SplitPane todaySplitPane = new SplitPane();
        ScrollPane todayScrollPane = new ScrollPane(todayAnchorPane);

        todaySplitPane.setDividerPositions(0.20);

        todaySplitPane.getItems().add(menuScrollPane);
        todaySplitPane.getItems().add(todayScrollPane);

        todayTableView = new TableView<>();

        todayTableView.getColumns().addAll(taskNameColumn, descriptionColumn, categoryColumn, dateColumn);

        todayTableView.setPrefHeight(640);
        todayTableView.setLayoutX(10);
        todayTableView.setLayoutY(50);

        todayTableView.getStylesheets().addAll("file:src/main/resources/TableView.css");

        Text todayHeadline = new Text("Today");
        todayHeadline.setStyle("-fx-font: 30 fantasy");
        todayHeadline.setLayoutX(10);
        todayHeadline.setLayoutY(40);


        for(Task task: mainList.getTodaysTasks()){
            todayTableView.getItems().add(task);
        }
        todayTableView.setRowFactory(new Callback<TableView<Task>, TableRow<Task>>() {
            @Override
            public TableRow<Task> call(TableView<Task> taskStringTableColumn) {
                return new TableRow<Task>() {
                    @Override
                    protected void updateItem(Task task, boolean empty) {
                        super.updateItem(task, empty);
                        if (!empty) {
                            if ((task.getStatus().getStatus())&&(task.getPriority().getPriorityName().equals("Low"))) {
                                setStyle("");
                            } else if((task.getStatus().getStatus())&&(task.getPriority().getPriorityName().equals("Medium"))){
                                setStyle("-fx-background-color: moccasin");
                            }else if(task.getStatus().getStatus()&&task.getPriority().getPriorityName().equals("High")){
                                setStyle("-fx-background-color: pink");
                            }else if(!task.getStatus().getStatus()){
                                setStyle("-fx-background-color: lightGreen");
                            }
                        }else{
                            setStyle("");
                        }
                    }

                };
            }
        });

        todayAnchorPane.getChildren().addAll(todayHeadline, todayTableView);

        contextMenu(todayTableView);

        todayTableView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2 && click.getButton() == MouseButton.PRIMARY) {
                editTaskWindow(todayTableView);
            }
        });

        todayScene = new Scene(todaySplitPane, 1150, 700);
    }
    private void upcomingInit(){
        SplitPane upcomingSplitPane = new SplitPane();
        upcomingSplitPane.setDividerPositions(0.20);

        AnchorPane upcomingAnchorPane = new AnchorPane();
        ScrollPane upcomingScrollPane = new ScrollPane(upcomingAnchorPane);

        upcomingSplitPane.getItems().add(menuScrollPane);
        upcomingSplitPane.getItems().add(upcomingScrollPane);


        upcomingTableView = new TableView<>();

        upcomingTableView.getColumns().addAll(taskNameColumn, descriptionColumn, categoryColumn, dateColumn);

        upcomingTableView.setPrefHeight(640);
        upcomingTableView.setLayoutX(10);
        upcomingTableView.setLayoutY(50);
        Text upcomingHeadline = new Text("Upcoming");
        upcomingHeadline.setStyle("-fx-font: 30 fantasy");
        upcomingHeadline.setLayoutX(10);
        upcomingHeadline.setLayoutY(40);

        upcomingTableView.getStylesheets().addAll("file:src/main/resources/TableView.css");

        for(Task task: mainList.getUpcomingTasks()){
            upcomingTableView.getItems().add(task);
        }
        upcomingTableView.setRowFactory(new Callback<TableView<Task>, TableRow<Task>>() {
            @Override
            public TableRow<Task> call(TableView<Task> taskStringTableColumn) {
                return new TableRow<Task>() {
                    @Override
                    protected void updateItem(Task task, boolean empty) {
                        super.updateItem(task, empty);
                        if (!empty) {
                            if ((task.getStatus().getStatus())&&(task.getPriority().getPriorityName().equals("Low"))) {
                                setStyle("");
                            } else if((task.getStatus().getStatus())&&(task.getPriority().getPriorityName().equals("Medium"))){
                                setStyle("-fx-background-color: moccasin");
                            }else if(task.getStatus().getStatus()&&task.getPriority().getPriorityName().equals("High")){
                                setStyle("-fx-background-color: pink");
                            }
                        }else{
                            setStyle("");
                        }
                    }

                };
            }
        });

        upcomingAnchorPane.getChildren().addAll(upcomingHeadline, upcomingTableView);

        contextMenu(upcomingTableView);

        upcomingTableView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2 && click.getButton() == MouseButton.PRIMARY) {
                editTaskWindow(upcomingTableView);
            }
        });

        upcomingScene = new Scene(upcomingSplitPane, 1100, 700);
    }
    private void highInit(){
        SplitPane highPrioritySplitPane = new SplitPane();
        highPrioritySplitPane.setDividerPositions(0.20);

        AnchorPane highPriorityAnchorPane = new AnchorPane();

        ScrollPane highPriorityScrollPane = new ScrollPane(highPriorityAnchorPane);

        highPrioritySplitPane.getItems().add(menuScrollPane);
        highPrioritySplitPane.getItems().add(highPriorityScrollPane);

        highTableView = new TableView<>();

        highTableView.getColumns().addAll(taskNameColumn, descriptionColumn, categoryColumn, dateColumn);

        highTableView.setPrefHeight(640);
        highTableView.setLayoutX(10);
        highTableView.setLayoutY(50);
        Text highHeadline = new Text("High Priority");
        highHeadline.setStyle("-fx-font: 30 fantasy");
        highHeadline.setLayoutX(10);
        highHeadline.setLayoutY(40);

        highTableView.getStylesheets().addAll("file:src/main/resources/TableView.css");

        for(Task task: mainList.getTasksByPriority(new HighPriority())){
            highTableView.getItems().add(task);
        }
        highTableView.setRowFactory(new Callback<TableView<Task>, TableRow<Task>>() {
            @Override
            public TableRow<Task> call(TableView<Task> taskStringTableColumn) {
                return new TableRow<Task>() {
                    @Override
                    protected void updateItem(Task task, boolean empty) {
                        super.updateItem(task, empty);
                        if (!empty) {
                            setStyle("-fx-background-color: pink");
                        }else{
                            setStyle("");
                        }
                    }

                };
            }
        });
        highPriorityAnchorPane.getChildren().addAll(highHeadline,highTableView);

        contextMenu(highTableView);

        highTableView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2 && click.getButton() == MouseButton.PRIMARY) {
                editTaskWindow(highTableView);
            }
        });

        highPriorityScene = new Scene(highPrioritySplitPane, 1150, 700);

    }
    private void medInit(){
        SplitPane mediumPrioritySplitPane = new SplitPane();
        mediumPrioritySplitPane.setDividerPositions(0.20);

        AnchorPane mediumPriorityAnchorPane = new AnchorPane();

        ScrollPane mediumPriorityScrollPane = new ScrollPane(mediumPriorityAnchorPane);

        mediumPrioritySplitPane.getItems().add(menuScrollPane);
        mediumPrioritySplitPane.getItems().add(mediumPriorityScrollPane);

        mediumTableView = new TableView<>();

        mediumTableView.getColumns().addAll(taskNameColumn, descriptionColumn, categoryColumn, dateColumn);

        mediumTableView.setPrefHeight(640);
        mediumTableView.setLayoutX(10);
        mediumTableView.setLayoutY(50);
        Text mediumHeadline = new Text("Medium Priority");
        mediumHeadline.setStyle("-fx-font: 30 fantasy");
        mediumHeadline.setLayoutX(10);
        mediumHeadline.setLayoutY(40);

        mediumTableView.getStylesheets().addAll("file:src/main/resources/TableView.css");

        for(Task task: mainList.getTasksByPriority(new MediumPriority())){
            mediumTableView.getItems().add(task);
        }
        mediumTableView.setRowFactory(new Callback<TableView<Task>, TableRow<Task>>() {
            @Override
            public TableRow<Task> call(TableView<Task> taskStringTableColumn) {
                return new TableRow<Task>() {
                    @Override
                    protected void updateItem(Task task, boolean empty) {
                        super.updateItem(task, empty);
                        if (!empty) {
                            setStyle("-fx-background-color: moccasin");
                        }else{
                            setStyle("");
                        }
                    }

                };
            }
        });
        mediumPriorityAnchorPane.getChildren().addAll(mediumHeadline, mediumTableView);

        contextMenu(mediumTableView);

        mediumTableView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2 && click.getButton() == MouseButton.PRIMARY) {
                editTaskWindow(mediumTableView);
            }
        });

        mediumPriorityScene = new Scene(mediumPrioritySplitPane, 1150, 700);


    }
    private void lowInit(){
        SplitPane lowPrioritySplitPane = new SplitPane();
        lowPrioritySplitPane.setDividerPositions(0.20);

        AnchorPane lowPriorityAnchorPane = new AnchorPane();

        ScrollPane lowPriorityScrollPane = new ScrollPane(lowPriorityAnchorPane);

        lowPrioritySplitPane.getItems().add(menuScrollPane);
        lowPrioritySplitPane.getItems().add(lowPriorityScrollPane);

        lowTableView = new TableView<>();

        lowTableView.getColumns().addAll(taskNameColumn, descriptionColumn, categoryColumn, dateColumn);

        lowTableView.setPrefHeight(640);
        lowTableView.setLayoutX(10);
        lowTableView.setLayoutY(50);
        Text lowHeadline = new Text("Low Priority");
        lowHeadline.setStyle("-fx-font: 30 fantasy");
        lowHeadline.setLayoutX(10);
        lowHeadline.setLayoutY(40);

        lowTableView.getStylesheets().addAll("file:src/main/resources/TableView.css");


        for(Task task: mainList.getTasksByPriority(new LowPriority())){
            lowTableView.getItems().add(task);
        }

        lowPriorityAnchorPane.getChildren().addAll(lowHeadline,lowTableView);

        contextMenu(lowTableView);

        lowTableView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2 && click.getButton() == MouseButton.PRIMARY) {
                editTaskWindow(lowTableView);
            }
        });

        lowPriorityScene = new Scene(lowPrioritySplitPane, 1150, 700);

    }
    private void calendarInit(){
        AnchorPane calendarAnchorPane = new AnchorPane();


        calendarAnchorPane.getChildren().addAll();
        SplitPane calendarSplitPane = new SplitPane();
        calendarSplitPane.setDividerPositions(0.20);


        ScrollPane calendarScrollPane = new ScrollPane(calendarAnchorPane);
        calendarScrollPane.setStyle("fx-background-color: brown");

        calendarSplitPane.getItems().add(menuScrollPane);
        calendarSplitPane.getItems().add(calendarScrollPane);

        datePickerCalendar = new DatePicker();

        datePickerCalendar.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate localDate, boolean b) {
                        super.updateItem(localDate, b);

                        for (Task t:mainList.getTaskList()) {
                            if (!b && localDate != null){
                                if (t.getStartDate().isEqual(localDate)&&t.getStatus().getStatus()){
                                    this.setStyle("-fx-background-color: pink");
                                    break;
                                }else if (t.getStartDate().isEqual(localDate)&&!t.getStatus().getStatus()){
                                    setStyle("-fx-background-color: lightGreen");
                                }
                            }
                        }
                    }
                };
            }
        });

        DatePickerSkin calenderView = new DatePickerSkin(datePickerCalendar);

        Node popupCalender = calenderView.getPopupContent();
        popupCalender.setLayoutX(315);
        popupCalender.setLayoutY(236);
        popupCalender.scaleXProperty().set(2);
        popupCalender.scaleYProperty().set(2);


        calendarAnchorPane.getChildren().add(popupCalender);


        calendarScene = new Scene(calendarSplitPane, 1150, 700);
        datePickerCalendar.setOnAction(this);

    }
    private void byDayInit(DatePicker startTime){
        SplitPane byDaySplitPane = new SplitPane();
        byDaySplitPane.setDividerPositions(0.20);

        AnchorPane byDayAnchorPane = new AnchorPane();

        ScrollPane byDayScrollPane = new ScrollPane(byDayAnchorPane);

        byDaySplitPane.getItems().add(menuScrollPane);
        byDaySplitPane.getItems().add(byDayScrollPane);

        byDayTableView = new TableView<>();

        byDayTableView.getColumns().addAll(taskNameColumn, descriptionColumn, categoryColumn, dateColumn);

        byDayTableView.setPrefHeight(640);
        byDayTableView.setLayoutX(10);
        byDayTableView.setLayoutY(50);
        byDayTableView.getStylesheets().addAll("file:src/main/resources/TableView.css");

        backToCalendarButton = new Button("Go back to Calendar");
        backToCalendarButton.setStyle("-fx-font-size:20");
        backToCalendarButton.setLayoutY(10);
        backToCalendarButton.setLayoutX(10);
        backToCalendarButton.setPrefWidth(854);
        backToCalendarButton.setPrefHeight(40);

        chosen = startTime.getValue();
        for (Task task : mainList.getTaskByDay(chosen)){
            byDayTableView.getItems().add(task);
        }
        byDayTableView.setRowFactory(new Callback<TableView<Task>, TableRow<Task>>() {
            @Override
            public TableRow<Task> call(TableView<Task> taskStringTableColumn) {
                return new TableRow<Task>() {
                    @Override
                    protected void updateItem(Task task, boolean empty) {
                        super.updateItem(task, empty);
                        if (!empty) {
                            if ((task.getStatus().getStatus())&&(task.getPriority().getPriorityName().equals("Low"))) {
                                setStyle("");
                            } else if((task.getStatus().getStatus())&&(task.getPriority().getPriorityName().equals("Medium"))){
                                setStyle("-fx-background-color: moccasin");
                            }else if(task.getStatus().getStatus()&&task.getPriority().getPriorityName().equals("High")){
                                setStyle("-fx-background-color: pink");
                            }else if(!task.getStatus().getStatus()){
                                setStyle("-fx-background-color: lightGreen");
                            }
                        }else{
                            setStyle("");
                        }
                    }

                };
            }
        });

        byDayAnchorPane.getChildren().addAll(backToCalendarButton, byDayTableView);

        contextMenu(byDayTableView);

        byDayTableView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2 && click.getButton() == MouseButton.PRIMARY) {
                editTaskWindow(byDayTableView);
            }
        });

        byDayScene = new Scene(byDaySplitPane, 1150, 700);

        backToCalendarButton.setOnAction(this);

        

    }
    private void categoryInit(){
        SplitPane categoriesSplitPane = new SplitPane();
        categoriesSplitPane.setDividerPositions(0.20);

        AnchorPane categoriesAnchorPane = new AnchorPane();

        ScrollPane categoriesScrollPane = new ScrollPane(categoriesAnchorPane);

        categoriesSplitPane.getItems().add(menuScrollPane);
        categoriesSplitPane.getItems().add(categoriesScrollPane);

        categoriesScene = new Scene(categoriesSplitPane, 1150, 700);

        Text categoryName = new Text("Category name");
        TextField category3 = new TextField();
        Button categoryAdd = new Button("Add");

        categoryName.setStyle("-fx-font: 22 arial");

        categoryName.setLayoutX(200);
        category3.setLayoutX(400);
        categoryAdd.setLayoutX(400);

        categoryName.setLayoutY(150);
        category3.setLayoutY(127);
        categoryAdd.setLayoutY(200);

        category3.setPrefWidth(300);
        categoryAdd.setPrefWidth(300);

        category3.setPrefHeight(35);
        categoryAdd.setPrefHeight(35);

        categoriesAnchorPane.getChildren().addAll(categoryName, category3, categoryAdd);
    }

    /**
     * Sets user back to previous window when action is complete and scene should close
     */
    private void backToScene(){
        if (publicScene ==upcomingScene){
            leftMenuInit();
            upcomingInit();
            publicStage.setScene(upcomingScene);
            menuButtonStyling(upcomingButton);
            publicScene = upcomingScene;

        }
        else if (publicScene ==calendarScene){
            leftMenuInit();
            calendarInit();
            publicStage.setScene(calendarScene);
            menuButtonStyling(calendarButton);
            publicScene = calendarScene;
        }
        else if (publicScene == highPriorityScene){
            leftMenuInit();
            highInit();
            publicStage.setScene(highPriorityScene);
            menuButtonStyling(highPriorityButton);
            publicScene = highPriorityScene;
        }
        else if (publicScene == mediumPriorityScene){
            leftMenuInit();
            medInit();
            publicStage.setScene(mediumPriorityScene);
            menuButtonStyling(mediumPriorityButton);
            publicScene = mediumPriorityScene;

        }
        else if (publicScene == lowPriorityScene){
            leftMenuInit();
            lowInit();
            publicStage.setScene(lowPriorityScene);
            menuButtonStyling(lowPriorityButton);
            publicScene = lowPriorityScene;

        }
        else if (publicScene == byCategoryScene){
            leftMenuInit();
            getCategoryList(activeCategory);
            menuButtonStyling(categoriesMenuButton);
        }
        else {
            leftMenuInit();
            todayInit();
            publicStage.setScene(todayScene);
            menuButtonStyling(todayButton);
            publicScene = todayScene;
        }
    }

    private void helpSettingsWindow(){

        Text settingsText = new Text("Settings");
        Text helpText = new Text("Help");
        Text filePathCurrent = new Text("Filepath to current saveFile");
        Hyperlink documentation = new Hyperlink("User Manual");
        Hyperlink linkUserManual = new Hyperlink("file:src/main/resources/2Do%20User%20Manual%20PDF.pdf");
        documentation.setOnAction(actionEvent -> getHostServices().showDocument(linkUserManual.getText()));

        TextField filePathField = new TextField();

        filePathField.setText(fileMana.getFilePath());


        Button changeFilePath = new Button("Change folder");
        Button saveChanges = new Button("Apply changes");
        Button close = new Button("Close");
        Button reset = new Button("Reset");

        saveChanges.getStylesheets().add("file:src/main/resources/PressedMenuButton.css");
        changeFilePath.getStylesheets().add("file:src/main/resources/PressedMenuButton.css");
        close.getStylesheets().add("file:src/main/resources/PressedMenuButton.css");
        reset.getStylesheets().add("file:src/main/resources/DeleteButton.css");

        saveChanges.setPrefWidth(180);
        changeFilePath.setPrefWidth(180);
        close.setPrefWidth(180);
        reset.setPrefWidth(180);


        settingsText.setStyle("-fx-font: 22 arial");
        helpText.setStyle("-fx-font: 22 arial");
        filePathCurrent.setStyle("-fx-font: 18 arial");
        documentation.setStyle("-fx-font: 18 arial");

        settingsText.setLayoutY(40);
        settingsText.setLayoutX(10);

        filePathCurrent.setLayoutY(80);
        filePathCurrent.setLayoutX(20);

        helpText.setLayoutY(250);
        helpText.setLayoutX(10);

        documentation.setLayoutY(280);
        documentation.setLayoutX(10);

        filePathField.setPrefWidth(350);
        filePathField.setLayoutX(25);
        filePathField.setLayoutY(90);

        changeFilePath.setLayoutY(140);
        saveChanges.setLayoutY(140);
        close.setLayoutY(800);
        reset.setLayoutY(800);

        changeFilePath.setLayoutX(10);
        saveChanges.setLayoutX(210);
        close.setLayoutX(210);
        reset.setLayoutX(10);



        AnchorPane secondaryLayout = new AnchorPane();
        secondaryLayout.getChildren().addAll(filePathField, changeFilePath, saveChanges, close, filePathCurrent, documentation, helpText, settingsText, reset);


        Scene secondScene = new Scene(secondaryLayout, 400, 800);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Second Stage");
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setResizable(false);
        newWindow.setAlwaysOnTop(true);
        newWindow.setScene(secondScene);


        // Set position of second window, related to primary window.
        newWindow.setX(publicStage.getX() + 200);
        newWindow.setY(publicStage.getY());

        saveChanges.setOnAction(actionEvent -> {
            if (!(filePathField.getText().equalsIgnoreCase(""))){
                List backup = mainList;
                fileMana.saveFilePath(filePathField.getText());
                fileMana.startUpFile();
                mainList = backup;
                fileMana.save(mainList);
            }
        });
        changeFilePath.setOnAction(actionEvent -> {
            newWindow.close();
            fileChooserWindow(fileMana.getFilePath());
        });
        close.setOnAction(ActionEvent -> newWindow.close());
        reset.setOnAction(actionEvent -> {
            confirmResetWindow();
            newWindow.close();
        });

        newWindow.show();

    }
    private void confirmResetWindow(){
        Text warningText = new Text("Are you sure? Everything will be deleted!");

        Button confirm = new Button("Confirm");
        Button cancel = new Button("Cancel");


        warningText.setStyle("-fx-font: 15 arial");

        cancel.getStylesheets().add("file:src/main/resources/PressedMenuButton.css");
        confirm.getStylesheets().add("file:src/main/resources/DeleteButton.css");

        warningText.setLayoutY(40);
        warningText.setLayoutX(20);

        confirm.setLayoutX(30);
        confirm.setLayoutY(80);
        cancel.setLayoutX(160);
        cancel.setLayoutY(80);


        AnchorPane secondaryLayout = new AnchorPane();
        secondaryLayout.getChildren().addAll(warningText, confirm, cancel);


        Scene secondScene = new Scene(secondaryLayout, 300, 150);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Second Stage");
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setResizable(false);
        newWindow.setAlwaysOnTop(true);
        newWindow.setScene(secondScene);


        // Set position of second window, related to primary window.
        newWindow.setX(publicStage.getX() + 200);
        newWindow.setY(publicStage.getY());

        cancel.setOnAction(ActionEvent -> {
            newWindow.close();
            helpSettingsWindow();
        });
        confirm.setOnAction(actionEvent -> {
            mainList = new List("Main");
            fileMana.resetFilePath();
            fileMana = new FileManagement();
            fileMana.save(mainList);
            newWindow.close();
            refresh();
        });

        newWindow.show();
    }
    private void fileChooserWindow(String start){
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Second Stage");
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setResizable(false);
        newWindow.setAlwaysOnTop(true);
        newWindow.setWidth(500);
        newWindow.setHeight(500);

        File startFile = new File(start.replace("output", ""));

        // Set position of second window, related to primary window.
        newWindow.setX(publicStage.getX() + 200);
        newWindow.setY(publicStage.getY());

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose folder to save data");
        directoryChooser.setInitialDirectory(startFile);
        File selectedDirectory = directoryChooser.showDialog(newWindow);
        if (selectedDirectory != null) {
            fileMana.setFilePath(selectedDirectory.getAbsolutePath() + "\\output");
        }

        helpSettingsWindow();
    }
    private void missingTaskName() {
        Label taskNameLabel = new Label("The task must have a name");
        taskNameLabel.setStyle("-fx-font: 18 arial");
        taskNameLabel.setLayoutX(50);
        taskNameLabel.setLayoutY(70);


        ImageView warning = new ImageView(new Image("file:src/main/resources/warning.png"));
        warning.setFitHeight(40);
        warning.setPreserveRatio(true);
        warning.setLayoutX(290);
        warning.setLayoutY(63);

        AnchorPane taskNameLayout = new AnchorPane();
        taskNameLayout.getChildren().addAll(taskNameLabel, warning);

        Scene missingTaskScene = new Scene(taskNameLayout, 400, 200);

        // New window (Stage)
        Stage missingTaskName = new Stage();
        missingTaskName.setTitle("2DO");
        missingTaskName.initModality(Modality.APPLICATION_MODAL);
        missingTaskName.setResizable(false);
        missingTaskName.setAlwaysOnTop(true);
        missingTaskName.setScene(missingTaskScene);

        // Set position of second window, related to primary window.
        missingTaskName.setX(publicStage.getX() + 400);
        missingTaskName.setY(publicStage.getY() + 200);
        missingTaskName.getIcons().add(new Image("file:src/main/resources/icon.png"));

        missingTaskName.show();
    }

    /**
     * Special view and list created from chosen category
     * @param categoryName String from comboBox in left menu
     */
    private void getCategoryList(String categoryName){
        activeCategory = categoryName;
        Text categoryHeadline = new Text("Category: " + categoryName);
        menuButtonStyling(categoriesMenuButton);

        categoryHeadline.setStyle("-fx-font: 30 fantasy");
        categoryHeadline.setLayoutX(10);
        categoryHeadline.setLayoutY(40);

        SplitPane byCategorySplitPane = new SplitPane();
        byCategorySplitPane.setDividerPositions(0.20);

        AnchorPane byCategoryAnchorPane = new AnchorPane();

        ScrollPane byCategoryScrollPane = new ScrollPane(byCategoryAnchorPane);

        byCategorySplitPane.getItems().add(menuScrollPane);
        byCategorySplitPane.getItems().add(byCategoryScrollPane);

        byCategoryTableView = new TableView<>();

        byCategoryTableView.getColumns().addAll(taskNameColumn, descriptionColumn, categoryColumn, dateColumn);

        byCategoryTableView.setPrefHeight(640);
        byCategoryTableView.setLayoutX(10);
        byCategoryTableView.setLayoutY(50);

        byCategoryTableView.getStylesheets().addAll("file:src/main/resources/TableView.css");

        refresh();
        byCategoryTableView.setRowFactory(new Callback<TableView<Task>, TableRow<Task>>() {
            @Override
            public TableRow<Task> call(TableView<Task> taskStringTableColumn) {
                return new TableRow<Task>() {
                    @Override
                    protected void updateItem(Task task, boolean empty) {
                        super.updateItem(task, empty);
                        if (!empty) {
                            if ((task.getPriority().getPriorityName().equals("Low"))) {
                                setStyle("");
                            } else if((task.getPriority().getPriorityName().equals("Medium"))){
                                setStyle("-fx-background-color: moccasin");
                            }else if(task.getPriority().getPriorityName().equals("High")){
                                setStyle("-fx-background-color: pink");
                            }
                        }else{
                            setStyle("");
                        }
                    }
                };
            }
        });


        byCategoryAnchorPane.getChildren().addAll(byCategoryTableView, categoryHeadline);

        byCategoryTableView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2 && click.getButton() == MouseButton.PRIMARY) {
                editTaskWindow(byCategoryTableView);
            }
        });
        contextMenu(byCategoryTableView);

        byCategoryScene = new Scene(byCategorySplitPane, 1150, 700);
        publicStage.setScene(byCategoryScene);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if(actionEvent.getSource()==newTaskButton){
            leftMenuInit();
            addTaskInit();
            publicStage.setScene(newTaskScene);
            menuButtonStyling(newTaskButton);
        }
        if(actionEvent.getSource()==todayButton){
            leftMenuInit();
            todayInit();
            publicStage.setScene(todayScene);
            menuButtonStyling(todayButton);
            publicScene = todayScene;
        }
        if(actionEvent.getSource()==upcomingButton){
            leftMenuInit();
            upcomingInit();
            publicStage.setScene(upcomingScene);
            menuButtonStyling(upcomingButton);
            publicScene = upcomingScene;
        }
        if(actionEvent.getSource()==highPriorityButton){
            leftMenuInit();
            highInit();
            publicStage.setScene(highPriorityScene);
            menuButtonStyling(highPriorityButton);
            publicScene = highPriorityScene;
        }
        if(actionEvent.getSource()==mediumPriorityButton){
            leftMenuInit();
            medInit();
            publicStage.setScene(mediumPriorityScene);
            menuButtonStyling(mediumPriorityButton);
            publicScene = mediumPriorityScene;
        }
        if(actionEvent.getSource()==lowPriorityButton){
            leftMenuInit();
            lowInit();
            publicStage.setScene(lowPriorityScene);
            menuButtonStyling(lowPriorityButton);
            publicScene = lowPriorityScene;
        }
        if(actionEvent.getSource()==categoriesMenuButton){
            leftMenuInit();
            categoryInit();
            publicStage.setScene(categoriesScene);
            publicScene = categoriesScene;
        }
        if(actionEvent.getSource()==calendarButton||actionEvent.getSource()==backToCalendarButton){
            leftMenuInit();
            calendarInit();
            publicStage.setScene(calendarScene);
            menuButtonStyling(calendarButton);
            publicScene = calendarScene;
        }
        if(actionEvent.getSource()==add1){
            if (taskName2.getText() == null || taskName2.getText().equals("")) {
                missingTaskName();
            } else {
                addTaskBetter(taskName2, description2, category2, picker1, box);
                backToScene();
            }
        }
        if(actionEvent.getSource()==add1Edit){
            editTaskCommit();
            leftMenuInit();
            todayInit();
            fileMana.save(mainList);
            publicStage.setScene(todayScene);
            menuButtonStyling(todayButton);
            backToScene();
        }
        if (actionEvent.getSource()==deleteTask){
            mainList.removeTask(activeTask);
            leftMenuInit();
            todayInit();
            fileMana.save(mainList);
            publicStage.setScene(todayScene);
            menuButtonStyling(todayButton);
            backToScene();
        }if(actionEvent.getSource()== back){
            backToScene();
        }if(actionEvent.getSource()== backEdit){
            backToScene();
        }if (actionEvent.getSource()==datePickerCalendar){
            leftMenuInit();
            byDayInit(datePickerCalendar);
            publicStage.setScene(byDayScene);
        }
        if (actionEvent.getSource()==helpSettings){
            helpSettingsWindow();
        }
    }

}
