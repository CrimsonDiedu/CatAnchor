package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.util.Log;
//{"photos":{
// "page":1,"pages":157210,"perpage":2,"total":"314420","photo":
// [
// {"id":"45903982832","owner":"158146325@N03","secret":"29609ba2f5","server":"4814","farm":5,"title":"Kitty Sitter Wellington Florida","ispublic":1,"isfriend":0,"isfamily":0}
// ]},"stat":"ok"}
public class FlickrResults {
    public Photos photos;

    public Photos.Photo getPhoto(int i){
        Log.e("JIf",photos.photo.length+"");

        return photos.photo[i];
    }

    public class Photos {
        public int page = 1,pages, perpage = 3;
        String total;
        public Photo[] photo;

        public class Photo {
            public String owner,id, secret, title;
            int farm, server, ispublic, isfriend, isfamily;
            public Photo()
            {
                Log.i("New Photo Stored", this.title+", "+id);
            }
        }
        //
        //    public FlickrPhoto(String secret, char size,
        //                       int farm_id, int server_id, int photo_id)

    }
    String stat;
}






/*
{ "photos": { "page": 1, "pages": "14809", "perpage": 10, "total": "148089",
    "photo": [
      { "id": "45898834652", "owner": "70986564@N00", "secret": "7c7a9e9f2c", "server": "4876", "farm": 5, "title": "The Pywacket low", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
      { "id": "31009060627", "owner": "103533263@N07", "secret": "e3c6a8f298", "server": "4862", "farm": 5, "title": "Hard or Soft (1\/2)", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
      { "id": "45035802415", "owner": "103533263@N07", "secret": "7df89fe879", "server": "4909", "farm": 5, "title": "Hard or Soft (2\/2)", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
      { "id": "45035692585", "owner": "156284396@N03", "secret": "ede7632ba5", "server": "4808", "farm": 5, "title": "fullsizeoutput_2cb", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
      { "id": "45035692005", "owner": "156284396@N03", "secret": "8aedb98bd6", "server": "4872", "farm": 5, "title": "fullsizeoutput_2ca", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
      { "id": "45035690985", "owner": "156284396@N03", "secret": "a9890bd4f9", "server": "4821", "farm": 5, "title": "fullsizeoutput_2cc", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
      { "id": "45035690525", "owner": "156284396@N03", "secret": "f53b70c44b", "server": "4898", "farm": 5, "title": "fullsizeoutput_2cd", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
      { "id": "45898703132", "owner": "21852074@N07", "secret": "fe4255cc6a", "server": "4874", "farm": 5, "title": "181118 - Cat", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
      { "id": "45223983564", "owner": "21852074@N07", "secret": "7e61244a80", "server": "4882", "farm": 5, "title": "181118 - Cat", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
      { "id": "45223982784", "owner": "21852074@N07", "secret": "9b1b006763", "server": "4817", "farm": 5, "title": "181118 - Cat", "ispublic": 1, "isfriend": 0, "isfamily": 0 }
    ] }, "stat": "ok" }
 */
