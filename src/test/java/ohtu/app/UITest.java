package ohtu.app;
import ohtu.objects.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.*;
import ohtu.dao.BookDao;
import ohtu.objects.Bookmarks;

import org.testfx.api.FxAssert;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.ListViewMatchers;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

public class UITest extends ApplicationTest {
  static Bookmarks bookmarks;
  static BookDao bookDao = new BookDao("test.db");

  @BeforeClass
  public static void setUpClass() {

    bookmarks = new Bookmarks(bookDao);
    bookmarks.init();
  }

  @Before
  public void setUp() {
    try {
      bookDao.emptyTable();
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  /**
   * Will be called with {@code @Before} semantics, i. e. before each test method.
   */
  @Override
  public void start(Stage stage) {

    UI ui = new UI(bookmarks);
    ui.start(stage);
  }

  /*@Test
  public void containsButtonToAddBook() {
    FxAssert.verifyThat(".button", LabeledMatchers.hasText("Add book"));
  }*/

  @Test
  public void bookGetsAddedToDatabase() {
    TextField titleText = find("#title_input");
    titleText.setText("a");
        
    TextField authorText = find("#author_input");
    authorText.setText("a");
        
    clickOn("#submit_button");
    ListView listview = find("#bookList");
    System.out.println(listview.getItems());   
    assertThat(listview, ListViewMatchers.hasListCell(new Book("a", "a")));
    }
  
  public <T extends Node> T find(final String query) {
        /** TestFX provides many operations to retrieve elements from the loaded GUI. */
        return lookup(query).query();
    }
}