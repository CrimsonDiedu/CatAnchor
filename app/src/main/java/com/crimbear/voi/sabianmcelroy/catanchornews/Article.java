package com.crimbear.voi.sabianmcelroy.catanchornews;

public class Article {
    public Source source;
    public String author,title,description,url,urlToImage,publishedAt,content;
    class Source{
        public String id,name;
    }
    public Article(String id,String name, String author, String title, String description, String url, String urlToImage, String publishedAt, String content)
    {
        source = new Source();

        this.source.id = id;
        this.source.name = name;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        SetContent(content);
    }
    void SetContent(String content){
        String editContent = "";
        Character c;
        for(int i = 0; i < content.length(); i++){
            c = content.charAt(i);
            if(c == '[')
                break;
            editContent+= c;
        }
        if(url != null){
            editContent+= "\nTo read more, go to "+ url;
        }

        this.content = editContent;
    }
}
