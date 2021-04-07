package labs.bamboo.newsapp;

public class NewsView {

    private int newsImageView;

    private String newsTitle;

    private String newsPaper;

    public NewsView( int newsImageView, String newsTitle, String newsPaper) {
        this.newsImageView = newsImageView;
        this.newsTitle = newsTitle;
        this.newsPaper = newsPaper;
    }

    public int getNewsImageView() {
        return  newsImageView;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsPaper() {
        return newsPaper;
    }
}
