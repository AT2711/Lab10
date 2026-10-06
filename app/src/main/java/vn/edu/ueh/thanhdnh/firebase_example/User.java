package vn.edu.ueh.thanhdnh.firebase_example;

class Article {

  private String documentId;
  private String title;
  private String content;
  private String img_cover;
  private long views;

  public Article(
          String documentId,
          String title,
          String content,
          String img_cover,
          long views) {

    this.documentId = documentId;
    this.title = title;
    this.content = content;
    this.img_cover = img_cover;
    this.views = views;
  }

  public String getDocumentId() {
    return documentId;
  }

  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getImg_cover() {
    return img_cover;
  }

  public void setImg_cover(String img_cover) {
    this.img_cover = img_cover;
  }

  public long getViews() {
    return views;
  }

  public void setViews(long views) {
    this.views = views;
  }
}