package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Oceny implements HierarchicalController<MainController> {

    public TextField imie;
    public TextField nazwisko;
    public ComboBox ocena;
    public TextField opisOceny;
    public TableView<Student> tabelka;
    private MainController parentController;

    ObservableList<Double> mozliweOceny = FXCollections.observableArrayList(1.0, 2.0, 3.0, 4.0, 5.0);

    public void dodaj(ActionEvent actionEvent) {
        Student st = new Student();
        st.setName(imie.getText());
        st.setSurname(nazwisko.getText());
        st.setGrade((Double) ocena.getSelectionModel().getSelectedItem());
        st.setGradeDetailed(opisOceny.getText());
        tabelka.getItems().add(st);
    }

    public void setParentController(MainController parentController) {
        this.parentController = parentController;
        //tabelka.getItems().addAll(parentController.getDataContainer().getStudents());
        tabelka.setEditable(true);
        tabelka.setItems(parentController.getDataContainer().getStudents());
    }

    public MainController getParentController() {
        return parentController;
    }

    public void initialize() {

        ocena.setItems(mozliweOceny);

        for (TableColumn<Student, ?> studentTableColumn : tabelka.getColumns()) {
            if ("imie".equals(studentTableColumn.getId())) {
                TableColumn<Student, String> imieColumn = (TableColumn<Student, String>) studentTableColumn;
                imieColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                imieColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                imieColumn.setOnEditCommit((val) -> {
                    val.getTableView().getItems().get(val.getTablePosition().getRow()).setName(val.getNewValue());
                });
            } else if ("nazwisko".equals(studentTableColumn.getId())) {
                TableColumn<Student, String> nazwiskoColumn = (TableColumn<Student, String>) studentTableColumn;
                nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
                nazwiskoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                nazwiskoColumn.setOnEditCommit((val) -> {
                    val.getTableView().getItems().get(val.getTablePosition().getRow()).setSurname(val.getNewValue());
                });
            } else if ("ocena".equals(studentTableColumn.getId())) {
                TableColumn<Student, Double> ocenaColumn = (TableColumn<Student, Double>) studentTableColumn;
                ocenaColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
                ocenaColumn.setCellFactory(ComboBoxTableCell.forTableColumn(mozliweOceny));
                ocenaColumn.setOnEditCommit((val) -> {
                    val.getTableView().getItems().get(val.getTablePosition().getRow()).setGrade(val.getNewValue());
                });
            } else if ("opisOceny".equals(studentTableColumn.getId())) {
                TableColumn<Student, String> opisColumn = (TableColumn<Student, String>) studentTableColumn;
                opisColumn.setCellValueFactory(new PropertyValueFactory<>("gradeDetailed"));
                opisColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                opisColumn.setOnEditCommit((val) -> {
                    val.getTableView().getItems().get(val.getTablePosition().getRow()).setGradeDetailed(val.getNewValue());
                });
            }
        }

    }

    public void synchronizuj(ActionEvent actionEvent) {
        parentController.getDataContainer().setStudents(tabelka.getItems());
    }

    public void dodajJesliEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            dodaj(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
        }
    }
}