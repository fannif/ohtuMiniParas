package ohtu.objects;

public class Book {
  private String author;
  private String title;

  public Book(String title, String author) {
    this.title = title;
    this.author = author;

  }

  /**
   * @return the author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  @Override
  public String toString() {
    return title + " by: " + author;
  }
}