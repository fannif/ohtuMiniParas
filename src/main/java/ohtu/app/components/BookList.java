package ohtu.app.components;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import ohtu.app.FileSelector;
import ohtu.objects.Audiobook;
import ohtu.objects.Book;
import ohtu.objects.BookSuper;
import ohtu.objects.Bookmarks;

public class BookList extends GridPane {

    private Bookmarks bookmarks;

    private TextField authorInput = new TextField();
    private TextField titleInput = new TextField();

    private ListView<BookSuper> bookListView = new ListView<>();

    // private Label bookInfoAuthor = new Label("");
    // private Label bookInfoTitle = new Label("");

    private TextField editAuthorField = new TextField();
    private TextField editTitleField = new TextField();
    private FileSelector fileSelector;

    public BookList(Bookmarks bookmarks, FileSelector fileSelector) {

        this.bookmarks = bookmarks;
        this.fileSelector = fileSelector;

        Label authorLabel = new Label("Author");
        authorInput.setId("author_input");
        authorInput.setPromptText("Author");

        Label titleLabel = new Label("Title");
        titleInput.setId("title_input");
        titleInput.setPromptText("Title");

        HBox addButtons = new HBox(10);
        Button addBookButton = new Button("Add book");
        Button addAudiobookButton = new Button("Add audiobook");
        addBookButton.setId("submit_button");
        addButtons.getChildren().addAll(addBookButton, addAudiobookButton);

        // Display for selected book
        GridPane selectedBookDisplay = new GridPane();
        selectedBookDisplay.setPadding(new Insets(10, 10, 10, 10));

        Button deleteBookButton = new Button("Delete book");
        deleteBookButton.setId("delete_button");
        Button editBookButton = new Button("Save change");
        editBookButton.setId("edit_button");

        editAuthorField.setPromptText("Set new Author");
        editAuthorField.setId("edit_author");
        editTitleField.setPromptText("Set new Title");
        editTitleField.setId("edit_title");

        selectedBookDisplay.add(new Label("Author"), 0, 0);
        selectedBookDisplay.add(new Label("Title"), 0, 1);
        selectedBookDisplay.add(editAuthorField, 1, 0);
        selectedBookDisplay.add(editTitleField, 1, 1);
        selectedBookDisplay.add(deleteBookButton, 0, 2);
        selectedBookDisplay.add(editBookButton, 1, 2);

        // Setting size for the pane
        this.setMinSize(400, 200);

        // Setting the padding
        this.setPadding(new Insets(10, 10, 10, 10));

        // Setting the vertical and horizontal gaps between the columns
        this.setVgap(5);
        this.setHgap(5);

        bookListView.setId("bookList");

        // Arranging all the nodes in the grid
        this.add(titleLabel, 0, 0);
        this.add(titleInput, 1, 0);
        this.add(authorLabel, 0, 1);
        this.add(authorInput, 1, 1);
        this.add(addButtons, 0, 2);
        this.add(bookListView, 0, 3);
        this.add(selectedBookDisplay, 1, 3);

        // Set actions for buttons and listview
        addBookButton.setOnAction(this::addBookAction);
        addAudiobookButton.setOnAction(this::addAudiobookAction);
        authorInput.setOnKeyPressed(this::onEnterKeyPress);
        titleInput.setOnKeyPressed(this::onEnterKeyPress);
        deleteBookButton.setOnAction(this::deleteBookAction);
        editBookButton.setOnAction(this::editBookAction);
        bookListView.setOnMouseClicked(this::bookSelectedAction);

        refreshBookmarks();
    }

    private void bookSelectedAction(javafx.scene.input.MouseEvent e) {
        BookSuper selectedBook = getSelectedBook();
        if (selectedBook == null)
            return;
        setBookInfoText(selectedBook.getAuthor(), selectedBook.getTitle());
    }

    private void onEnterKeyPress(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            addBookAction(e);
        }
    }

    private void addAudiobookAction(Event e) {
        File mp3 = fileSelector.openFileBrowser();
        Audiobook audiobook = new Audiobook(titleInput.getText(), authorInput.getText(), mp3);
        if (!audiobook.isEmpty()) {
            System.out.println("yes lol");
            // if (checkBook(audiobook)) {
            // refreshBookmarks();
            // clearBookInput();
            // } else {
            // showNewAlert("Book exists", "The database already contains this book");
            // }
        }
        // Media hit = new Media(file.toURI().toString());
        // MediaPlayer mediaPlayer = new MediaPlayer(hit);
        // mediaPlayer.play();
    }

    private void addBookAction(Event e) {
        Book book = new Book(titleInput.getText(), authorInput.getText());
        if (!book.isEmpty()) {
            if (checkBook(book)) {
                refreshBookmarks();
                clearBookInput();
            } else {
                showNewAlert("Book exists", "The database already contains this book");
            }
        }
    }

    private void editBookAction(ActionEvent e) {
        BookSuper selectedBook = getSelectedBook();

        if (selectedBook == null) {
            showNewAlert("Not selected", "No book has been selected");
        } else if (editTitleField.getText().isBlank() && editAuthorField.getText().isBlank()) {
            showNewAlert("Fields are empty", "Title and/or author missing");
        } else {
            // If a field is empty, old value is kept
            String newTitle = !editTitleField.getText().isBlank() ? editTitleField.getText() : selectedBook.getTitle();
            String newAuthor = !editAuthorField.getText().isBlank() ? editAuthorField.getText()
                    : selectedBook.getAuthor();
            Book newBook = new Book(newTitle, newAuthor);

            editBook(selectedBook, newBook);
            refreshBookmarks();
            setBookInfoText(newBook.getAuthor(), newBook.getTitle());
            // clearBookEditInput();
        }
    }

    private void deleteBookAction(ActionEvent e) {
        BookSuper selectedBook = getSelectedBook();
        if (selectedBook == null) {
            showNewAlert("Not selected", "No book has been selected");
        } else if (deleteBook(selectedBook)) {
            refreshBookmarks();
            setBookInfoText("", "");
        }
    }

    // private void clearBookEditInput() {
    // editAuthorField.setText("");
    // editTitleField.setText("");
    // }

    private void clearBookInput() {
        authorInput.setText("");
        titleInput.setText("");
    }

    private void setBookInfoText(String author, String title) {
        editAuthorField.setText(author);
        editTitleField.setText(title);
    }

    private BookSuper getSelectedBook() {
        return bookListView.getSelectionModel().getSelectedItem();
    }

    private void showNewAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void refreshBookmarks() {
        bookListView.getItems().clear();
        bookListView.getItems().addAll(bookmarks.getBookmarks());
    }

    private Boolean checkBook(BookSuper book) {
        if (!bookmarks.contains(book)) {
            bookmarks.addBookmark(book);
            return true;
        }
        return false;
    }

    private Boolean deleteBook(BookSuper selectedBook) {
        if (bookmarks.contains(selectedBook)) {
            bookmarks.removeBookmark(selectedBook);
            return true;
        }
        return false;
    }

    private void editBook(BookSuper selectedBook, Book updatedBook) {
        bookmarks.updateBookmark(selectedBook, updatedBook);
    }

    public Bookmarks getBookmarks() {
        return bookmarks;
    }
}