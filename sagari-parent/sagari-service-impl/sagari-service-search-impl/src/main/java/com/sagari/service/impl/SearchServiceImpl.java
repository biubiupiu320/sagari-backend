package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.service.SearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author biubiupiu~
 */
@RestController
public class SearchServiceImpl extends BaseApiService<JSONObject> implements SearchService {

    @Autowired
    private RestHighLevelClient client;
    private final String ARTICLE_INDEX = "article_latest";
    private final String TAG_INDEX = "tag_latest";
    private final String USER_INDEX = "user_latest";
    @Value("${elasticsearch.fragmentSize}")
    private Integer fragmentSize;
    @Value("${elasticsearch.preTag}")
    private String preTag;
    @Value("${elasticsearch.postTag}")
    private String postTag;

    @Override
    public BaseResponse<JSONObject> searchArticle(String search, Integer page, Integer size) throws IOException {
        if (page < 0) {
            page = 0;
        }
        if (size < 10) {
            size = 10;
        }
        SearchRequest request = new SearchRequest(ARTICLE_INDEX);
        CountRequest countRequest = new CountRequest(ARTICLE_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.multiMatchQuery(search, "title", "content", "tags"));
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDel", false));
        sourceBuilder.query(boolQueryBuilder);
        countRequest.source(sourceBuilder);
        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(new HighlightBuilder.Field("title"));
        highlightBuilder.field(new HighlightBuilder.Field("content"));
        highlightBuilder.fragmentSize(fragmentSize);
        highlightBuilder.preTags(preTag);
        highlightBuilder.postTags(postTag);
        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.from(page * size);
        sourceBuilder.size(size);
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        JSONObject result = new JSONObject();
        List<JSONObject> list = Arrays.stream(hits).
                map(o -> JSON.parseObject(o.toString())).
                collect(Collectors.toList());
        result.put("result", JSON.toJSON(list));
        result.put("total", countResponse.getCount());
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> searchArticleByTitle(String title, Integer page, Integer size) throws IOException {
        SearchRequest request = new SearchRequest(ARTICLE_INDEX);
        CountRequest countRequest = new CountRequest(ARTICLE_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.multiMatchQuery(title, "title"));
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDel", false));
        sourceBuilder.query(boolQueryBuilder);
        countRequest.source(sourceBuilder);
        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(new HighlightBuilder.Field("title"));
        highlightBuilder.fragmentSize(fragmentSize);
        highlightBuilder.preTags(preTag);
        highlightBuilder.postTags(postTag);
        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.from(page * size);
        sourceBuilder.size(size);
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        JSONObject result = new JSONObject();
        List<JSONObject> list = Arrays.stream(hits).
                map(o -> JSON.parseObject(o.toString())).
                collect(Collectors.toList());
        result.put("result", JSON.toJSON(list));
        result.put("total", countResponse.getCount());
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> searchArticleByContent(String content, Integer page, Integer size) throws IOException {
        SearchRequest request = new SearchRequest(ARTICLE_INDEX);
        CountRequest countRequest = new CountRequest(ARTICLE_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.multiMatchQuery(content, "content"));
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDel", false));
        sourceBuilder.query(boolQueryBuilder);
        countRequest.source(sourceBuilder);
        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(new HighlightBuilder.Field("content"));
        highlightBuilder.fragmentSize(fragmentSize);
        highlightBuilder.preTags(preTag);
        highlightBuilder.postTags(postTag);
        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.from(page * size);
        sourceBuilder.size(size);
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        JSONObject result = new JSONObject();
        List<JSONObject> list = Arrays.stream(hits).
                map(o -> JSON.parseObject(o.toString())).
                collect(Collectors.toList());
        result.put("result", JSON.toJSON(list));
        result.put("total", countResponse.getCount());
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> searchAtBar(String search) throws IOException {
        SearchRequest request = new SearchRequest(ARTICLE_INDEX);
        CompletionSuggestionBuilder suggestion = new CompletionSuggestionBuilder("titleSuggest");
        suggestion.prefix(search);
        suggestion.size(10);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("suggestion", suggestion);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.suggest(suggestBuilder);
        sourceBuilder.fetchSource(false);
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        Suggest suggest = response.getSuggest();
        return setResultSuccess(JSON.parseObject(suggest.toString()));
    }
}
