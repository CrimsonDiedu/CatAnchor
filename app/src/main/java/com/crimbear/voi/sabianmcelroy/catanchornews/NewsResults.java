package com.crimbear.voi.sabianmcelroy.catanchornews;

public class NewsResults {
    String status;
    int totalResults;
    public Articles[] articles;


    public class Articles{

        public Source source;
        public String author,title,description,url,urlToImage,publishedAt,content;
        class Source{
            public String id,name;
        }
    }
}
