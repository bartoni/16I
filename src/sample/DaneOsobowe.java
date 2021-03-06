package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DaneOsobowe implements HierarchicalController<MainController> {

    public TextField imie;
    public TextField nazwisko;
    public TextField pesel;
    public TextField indeks;
    public TableView<Student> tabelka;
    private MainController parentController;

    public void dodaj(ActionEvent actionEvent) {
        Student st = new Student();
        st.setName(imie.getText());
        st.setSurname(nazwisko.getText());
        st.setPesel(pesel.getText());
        st.setIdx(indeks.getText());
        tabelka.getItems().add(st);
    }

    public void setParentController(MainController parentController) {
        this.parentController = parentController;
        tabelka.setEditable(true);
        tabelka.setItems(parentController.getDataContainer().getStudents());
    }

    public void usunZmiany() {
        tabelka.getItems().clear();
        tabelka.getItems().addAll(parentController.getDataContainer().getStudents());
    }

    public MainController getParentController() {
        return parentController;
    }

    public void initialize() {
        for (TableColumn<Student, ?> studentTableColumn : tabelka.getColumns()) {
            if ("imie".equals(studentTableColumn.getId())) {
                TableColumn<Student, String> imieColumn = (TableColumn<Student, String>) studentTableColumn;
                imieColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                imieColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                imieColumn.setOnEditCommit((val) -> val.getTableView().getItems().get(val.getTablePosition().getRow()).setName(val.getNewValue()));
            } else if ("nazwisko".equals(studentTableColumn.getId())) {
                TableColumn<Student, String> nazwiskoColumn = (TableColumn<Student, String>) studentTableColumn;
                studentTableColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
                nazwiskoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                nazwiskoColumn.setOnEditCommit((val) -> {
                    val.getTableView().getItems().get(val.getTablePosition().getRow()).setSurname(val.getNewValue());
                });
            } else if ("pesel".equals(studentTableColumn.getId())) {
                TableColumn<Student, String> peselColumn = (TableColumn<Student, String>) studentTableColumn;
                studentTableColumn.setCellValueFactory(new PropertyValueFactory<>("pesel"));
                peselColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                peselColumn.setOnEditCommit((val) -> {
                    val.getTableView().getItems().get(val.getTablePosition().getRow()).setPesel(val.getNewValue());
                });
            } else if ("indeks".equals(studentTableColumn.getId())) {
                TableColumn<Student, String> indeksColumn = (TableColumn<Student, String>) studentTableColumn;
                studentTableColumn.setCellValueFactory(new PropertyValueFactory<>("idx"));
                indeksColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                indeksColumn.setOnEditCommit((val) -> {
                    val.getTableView().getItems().get(val.getTablePosition().getRow()).setIdx(val.getNewValue());
                });
            }
        }

    }

    public void zapisz(ActionEvent actionEvent) {
        //definicja naglowkow
        String[] headers = new String[]{"Imię", "Nazwisko", "Ocena", "Uzasadnienie", "PESEL", "Numer indeksu"};
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Studenci");
        //stworzenie pierwszego wiersza dla naglowkow
        Row headerRow = sheet.createRow(0);
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        //definicja wlasnego fontu dla boldowanych naglowkow
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);

        style.setFont(font);

        //teraz fonty pod kolorki

        XSSFCellStyle red = wb.createCellStyle();
        red.setFillForegroundColor(IndexedColors.RED.getIndex());
        red.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCellStyle yellow = wb.createCellStyle();
        yellow.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        yellow.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCellStyle green = wb.createCellStyle();
        green.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        green.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //w jednej petli wpisuje kolejno naglowki i zmieniam ich font na moj wlasny (bold)
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
            headerRow.getCell(i).setCellStyle(style);
        }

        int row = 1;
        for (Student student : tabelka.getItems()) {
            XSSFRow r = sheet.createRow(row);
            r.createCell(0).setCellValue(student.getName());
            r.createCell(1).setCellValue(student.getSurname());
            if (student.getGrade() != null) {
                r.createCell(2).setCellValue(student.getGrade());
                if (student.getGrade() <= 3) {
                    r.getCell(2).setCellStyle(yellow);
                } else {
                    r.getCell(2).setCellStyle(green);
                }
            } else {
                r.createCell(2);
                r.getCell(2).setCellStyle(red);
            }
            r.createCell(3).setCellValue(student.getGradeDetailed());
            r.createCell(4).setCellValue(student.getIdx());
            r.createCell(5).setCellValue(student.getPesel());
            row++;
        }
        try (FileOutputStream fos = new FileOutputStream("data.xlsx")) {
            wb.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Uwaga na serializację: https://sekurak.pl/java-vs-deserializacja-niezaufanych-danych-i-zdalne-wykonanie-kodu-czesc-i/
     */
    public void wczytaj(ActionEvent actionEvent) {
        ArrayList<Student> studentsList = new ArrayList<>();
        try (FileInputStream ois = new FileInputStream("data.xlsx")) {
            XSSFWorkbook wb = new XSSFWorkbook(ois);
            XSSFSheet sheet = wb.getSheet("Studenci");
            //UWAGA int=1 zeby ominac wiersz z naglowkami
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                Student student = new Student();
                student.setName(row.getCell(0).getStringCellValue());
                student.setSurname(row.getCell(1).getStringCellValue());
                //trzeba dodac missingcellpolicy, bo jak nie, to podczas wczytywania z automatu daje 0.0 w miejsce wczesniejszego braku oceny
                //a RETURN BLANK AS NULL sprawia, ze zwraca pusta komorke, gdy wczesniej byl null
                if (row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null) {
                    student.setGrade(row.getCell(2).getNumericCellValue());
                }
                student.setGradeDetailed(row.getCell(3).getStringCellValue());
                student.setIdx(row.getCell(4).getStringCellValue());
                student.setPesel(row.getCell(5).getStringCellValue());
                studentsList.add(student);
            }
            tabelka.getItems().clear();
            tabelka.getItems().addAll(studentsList);
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}