package com.crimbear.voi.sabianmcelroy.catanchornews;

import java.util.ArrayList;

public class SourceList {
    String status;
    Source[] sources;//,sourcesSubSet;




    Source GetSourceAt(int i){
        return sources[i];
    }

    public Source[] GetSourcesFromCountry(String Country){
        ArrayList<Source> sourceArrayList = new ArrayList<Source>();
        for(int i = 0; i < sources.length; i++) {
            if(Country.contains(sources[i].country))
            sourceArrayList.add(sources[i]);
        }
        sources = (Source[])sourceArrayList.toArray(sources);

        return sources;
    }
    public Source[] GetSourcesWithLanguage(String Language){
        ArrayList<Source> sourceArrayList = new ArrayList<Source>();
        for(int i = 0; i < sources.length; i++) {
            if(Language.contains(sources[i].language))
                sourceArrayList.add(sources[i]);
        }
        sources = (Source[])sourceArrayList.toArray(sources);

        return sources;
    }
    public Source[] GetSourcesLanguageCountry(String Language,String Country ){
        ArrayList<Source> sourceArrayList = new ArrayList<Source>();
        if(sources.length == 0)
            return null;
        for(int i = 0; i < sources.length; i++) {
            if((Language ==""||Language.contains(sources[i].language)) && (Country==""||Country.contains(sources[i].country)))
                sourceArrayList.add(sources[i]);

        }
        sources = sourceArrayList.toArray(sources);

        return sources;
    }
    class Source{
        String id,name,description,url,category,language,country;
    }
}
