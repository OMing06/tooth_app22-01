package com.example.tooset_test02;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

public class SearchModel {
    public static RequestQueue requestQueue;
    public static String CLIENT_ID = "PiLiJOcYH2JyD7C734rQ";
    public static String CLIENT_SECRET = "ZQOwdRVfqg";
    public static String HOST = "https://openapi.naver.com/v1/search/shop.json";

    public class SearchItem {
        String lastBuildDate; //검색 결과를 생성한 시간이다.
        String total; //검색 결과 문서의 총 개수를 의미한다.
        String start; //검색 결과 문서 중, 문서의 시작점을 의미한다.
        String display; //검색된 검색 결과의 개수이다.

        ArrayList<SearchItems> items = new ArrayList<SearchItems>();
    }

    public class SearchItems {
        String title; //검색 결과 문서의 제목을 나타낸다. 제목에서 검색어와 일치하는 부분은 태그로 감싸져 있다.
        String lprice; //최저가 정보이다. 최저가 정보가 없는 경우 0으로 표시되며, 가격비교 데이터가 없는 경우 이 필드는 가격을 나타낸다.
        String image; //썸네일 이미지의 URL이다. 이미지가 있는 경우만 나타난다.
        String link; //검색 결과 문서의 하이퍼텍스트 link를 나타낸다.

    }

}
