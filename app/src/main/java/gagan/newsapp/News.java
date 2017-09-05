package gagan.newsapp;

/**
 * Created by Gagan on 7/11/2017.
 */
public class News {
    private String title;
    private String weburl;
    private String section;
    private String date;

    public News(String title_, String section_, String url_, String date_) {
        title = title_;
        section = section_;
        weburl = url_;
        date = date_;
    }

    public String getTitle() {
        return title;
    }

    public String getWeburl() {
        return weburl;
    }

    public String getSection() {
        return section;
    }

    public String getDate() {
        return date;
    }
}
