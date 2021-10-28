package controller;

import library.Books;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;

import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TextField idField;
    
    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField yearField;

    @FXML
    private TextField pagesField;

    @FXML
    private Button insertButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Books> tableView;
    
    @FXML
    private TableColumn<Books, Integer> idColumn;
    
    @FXML
    private TableColumn<Books, String> titleColumn;

    @FXML
    private TableColumn<Books, String> authorColumn;

    @FXML
    private TableColumn<Books, Integer> yearColumn;

    @FXML
    private TableColumn<Books, Integer> pagesColumn;

    @FXML
    private void insertButton() {
        try {
            PreparedStatement ps=getConnection().prepareStatement("INSERT INTO books values(NULL, ?, ?, ?, ?)");
            ps.setString(1, titleField.getText());
            ps.setString(2, authorField.getText());
            ps.setInt(3, Integer.parseInt(yearField.getText()));
            ps.setInt(4, Integer.parseInt(pagesField.getText()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	showBooks();
    }
    
    @FXML 
    private void updateButton() {
        /*String query = "UPDATE books SET Title='"+titleField.getText()+"',Author='"+authorField.getText()+"',Year="+yearField.getText()+",Pages="+pagesField.getText()+" WHERE ID="+idField.getText()+"";
        executeQuery(query);*/
        try {
            PreparedStatement ps=getConnection().prepareStatement("UPDATE books SET Title=?, Author=?, Year=?, Pages=? WHERE ID=?");
            ps.setString(1, titleField.getText());
            ps.setString(2, authorField.getText());
            ps.setInt(3, Integer.parseInt(yearField.getText()));
            ps.setInt(4, Integer.parseInt(pagesField.getText()));
            ps.setInt(5, Integer.parseInt(idField.getText()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        showBooks();
    }
    
    @FXML
    private void deleteButton() {
        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE FROM books WHERE ID=?");
            ps.setInt(1, Integer.parseInt(idField.getText()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        showBooks();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	showBooks();
    }
    
    public Connection getConnection() {
    	Connection conn;
    	try {
    		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","");
    		return conn;
    	}
    	catch (Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public ObservableList<Books> getBooksList(){
    	ObservableList<Books> booksList = FXCollections.observableArrayList();
    	Connection connection = getConnection();
    	String query = "SELECT * FROM books ";
    	Statement st;
    	ResultSet rs;
    	
    	try {
			st = connection.createStatement();
			rs = st.executeQuery(query);
			Books books;
			while(rs.next()) {
				books = new Books(rs.getInt("Id"),rs.getString("Title"),rs.getString("Author"),rs.getInt("Year"),rs.getInt("Pages"));
				booksList.add(books);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return booksList;
    }
    
    // I had to change ArrayList to ObservableList I didn't find another option to do this but this works :)
    public void showBooks() {
    	ObservableList<Books> list = getBooksList();
    	
    	idColumn.setCellValueFactory(new PropertyValueFactory<Books,Integer>("id"));
    	titleColumn.setCellValueFactory(new PropertyValueFactory<Books,String>("title"));
    	authorColumn.setCellValueFactory(new PropertyValueFactory<Books,String>("author"));
    	yearColumn.setCellValueFactory(new PropertyValueFactory<Books,Integer>("year"));
    	pagesColumn.setCellValueFactory(new PropertyValueFactory<Books,Integer>("pages"));
    	
    	tableView.setItems(list);
    }

    public void callbackClicTabla(javafx.scene.input.MouseEvent mouseEvent) {
        Books libro = (Books) tableView.getSelectionModel().getSelectedItem();
        idField.setText(String.valueOf(libro.getId()));
        titleField.setText(libro.getTitle());
        authorField.setText(libro.getAuthor());
        yearField.setText(String.valueOf(libro.getYear()));
        pagesField.setText(String.valueOf(libro.getPages()));
    }

}
