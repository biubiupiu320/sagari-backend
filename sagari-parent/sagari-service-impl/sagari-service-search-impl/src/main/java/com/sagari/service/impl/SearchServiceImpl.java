package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.service.SearchService;
import com.sagari.service.feign.ArticleServiceFeign;
import com.sagari.service.feign.TagServiceFeign;
import com.sagari.service.feign.UserServiceFeign;
import com.xxl.sso.core.login.SsoTokenLoginHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author biubiupiu~
 */
@RestController
public class SearchServiceImpl extends BaseApiService<JSONObject> implements SearchService {

    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private UserServiceFeign userServiceFeign;
    @Autowired
    private TagServiceFeign tagServiceFeign;
    @Autowired
    private ArticleServiceFeign articleServiceFeign;
    @Autowired
    private HttpServletRequest request;
    private static final String ARTICLE_INDEX = "article_latest";
    private static final String TAG_INDEX = "tag_latest";
    private static final String USER_INDEX = "user_latest";
    @Value("${elasticsearch.fragmentSize}")
    private Integer fragmentSize;
    @Value("${elasticsearch.preTag}")
    private String preTag;
    @Value("${elasticsearch.postTag}")
    private String postTag;

    @Override
    public BaseResponse<JSONObject> search(String search, Integer page) throws IOException {
        if (page < 0) {
            page = 0;
        }

        JSONObject articleJson = searchArticle(search, page, 10, 1);
        JSONObject tagJson = searchTag(page, 10, search);
        JSONObject userJson = searchUser(page, 10, search);

        Integer total = articleJson.getInteger("total");
        total += tagJson.getInteger("total");
        total += userJson.getInteger("total");

        JSONArray resultArray = new JSONArray(30);
        JSONArray articleArray = articleJson.getJSONArray("result");
        if (!articleArray.isEmpty()) {
            resultArray.addAll(articleArray);
        }
        JSONArray tagArray = tagJson.getJSONArray("result");
        if (!tagArray.isEmpty()) {
            resultArray.addAll(tagArray);
        }
        JSONArray userArray = userJson.getJSONArray("result");
        if (!userArray.isEmpty()) {
            resultArray.addAll(userArray);
        }

        JSONObject result = new JSONObject();
        result.put("result", resultArray);
        result.put("total", total);

        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> searchArticle(String search, Integer page, Integer size) throws IOException {
        if (page < 0) {
            page = 0;
        }
        if (size < 10) {
            size = 10;
        }
        return setResultSuccess(searchArticle(search, page, size, 1));
    }

    @Override
    public BaseResponse<JSONObject> searchArticleByTitle(String title, Integer page, Integer size) throws IOException {
        if (page < 0) {
            page = 0;
        }
        if (size < 10) {
            size = 10;
        }
        return setResultSuccess(searchArticle(title, page, size, 2));
    }

    @Override
    public BaseResponse<JSONObject> searchArticleByContent(String content, Integer page, Integer size) throws IOException {
        if (page < 0) {
            page = 0;
        }
        if (size < 10) {
            size = 10;
        }
        return setResultSuccess(searchArticle(content, page, size, 3));
    }

    @Override
    public BaseResponse<JSONObject> searchAtBar(String search) throws IOException {
        SearchRequest searchRequest = new SearchRequest(ARTICLE_INDEX);
        CompletionSuggestionBuilder suggestion = new CompletionSuggestionBuilder("titleSuggest");
        suggestion.prefix(search);
        suggestion.size(10);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("suggestion", suggestion);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.suggest(suggestBuilder);
        sourceBuilder.fetchSource(false);
        searchRequest.source(sourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        Suggest suggest = response.getSuggest();
        return setResultSuccess(JSON.parseObject(suggest.toString()));
    }

    @Override
    public BaseResponse<JSONObject> searchTag(String search, Integer page, Integer size) throws IOException {
        if (page < 0) {
            page = 0;
        }
        if (size < 10) {
            size = 10;
        }
        return setResultSuccess(searchTag(page, size, search));
    }

    @Override
    public BaseResponse<JSONObject> searchUser(String search, Integer page, Integer size) throws IOException {
        if (page < 0) {
            page = 0;
        }
        if (size < 0) {
            size = 10;
        }
        return setResultSuccess(searchUser(page, size, search));
    }

    @Override
    public BaseResponse<JSONObject> getHomeArticle(Integer page, Integer size) throws IOException {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        JSONArray tagArray = new JSONArray();
        if (xxlUser != null) {
            Integer userId = Integer.valueOf(xxlUser.getUserid());
            tagArray = tagServiceFeign.getFollowTags(userId).getData().getJSONArray("tags");
        }

        JSONObject result = getIndexArticle(tagArray, page, size);
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> getArticleByCategory(Integer categoryId, Integer page, Integer size) throws IOException {
        JSONArray tagArray = tagServiceFeign.getTagsByCategory(categoryId).getData().getJSONArray("tags");
        if (tagArray == null) {
            tagArray = new JSONArray();
        }

        JSONObject result = getIndexArticle(tagArray, page, size);
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> getRelateArticle(Integer articleId) throws IOException {
        String tagString = articleServiceFeign.getArticleTags(articleId);
        SearchRequest searchRequest = new SearchRequest(ARTICLE_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(QueryBuilders.matchQuery("tags", tagString));

        boolQueryBuilder.filter(QueryBuilders.termQuery("isDel", false));
        sourceBuilder.query(boolQueryBuilder);

        Script script = new Script("Math.random()");
        ScriptSortBuilder scriptBuilder = SortBuilders.scriptSort(script, ScriptSortBuilder.ScriptSortType.NUMBER);
        sourceBuilder.sort(scriptBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        String[] excludes = new String[]{"content", "titleSuggest"};
        sourceBuilder.fetchSource(null, excludes);
        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(searchRequest.toString());
        SearchHit[] hits = response.getHits().getHits();
        JSONObject result = new JSONObject();
        List<JSONObject> list = Arrays.stream(hits)
                .map(o -> JSON.parseObject(o.toString()))
                .collect(Collectors.toList());
        result.put("relates", JSON.toJSON(list));
        return setResultSuccess(result);
    }

    @Override
    public Boolean deleteArticle(Integer articleId, Integer userId) throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("author", userId));
        boolQueryBuilder.must(QueryBuilders.matchQuery("id", articleId));
        UpdateByQueryRequest updateRequest = new UpdateByQueryRequest(ARTICLE_INDEX);
        updateRequest.setQuery(boolQueryBuilder);
        Map<String, Object> params = new HashMap<>();
        params.put("updateTime", System.currentTimeMillis());
        ScriptType type = ScriptType.INLINE;
        String lang = "painless";
        String code = "ctx._source.isDel=true;ctx._source.updateTime=params.updateTime";
        Script script = new Script(type, lang, code, params);
        updateRequest.setScript(script);

        BulkByScrollResponse updateResponse = client.updateByQuery(updateRequest, RequestOptions.DEFAULT);
        return updateResponse.getUpdated() > 0;
    }

    private JSONObject searchArticle(String search, Integer page, Integer size, Integer type) throws IOException {
        SearchRequest searchRequest = new SearchRequest(ARTICLE_INDEX);
        CountRequest countRequest = new CountRequest(ARTICLE_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (type.equals(1)) {
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(search, "title", "content"));
            highlightBuilder.field(new HighlightBuilder.Field("title"));
            highlightBuilder.field(new HighlightBuilder.Field("content"));
        } else if (type.equals(2)) {
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(search, "title"));
            highlightBuilder.field(new HighlightBuilder.Field("title"));
        } else if (type.equals(3)) {
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(search, "content"));
            highlightBuilder.field(new HighlightBuilder.Field("content"));
        } else {
            return new JSONObject();
        }

        boolQueryBuilder.filter(QueryBuilders.termQuery("isDel", false));
        sourceBuilder.query(boolQueryBuilder);
        countRequest.source(sourceBuilder);
        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);

        highlightBuilder.fragmentSize(fragmentSize);
        highlightBuilder.preTags(preTag);
        highlightBuilder.postTags(postTag);

        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.from(page * size);
        sourceBuilder.size(size);
        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        JSONObject result = new JSONObject();
        List<JSONObject> list = Arrays.stream(hits).
                map(o -> JSON.parseObject(o.toString())).
                collect(Collectors.toList());
        List<Integer> userIds = list.stream().map(o -> {
            JSONObject source = o.getJSONObject("_source");
            return source.getInteger("author");
        }).collect(Collectors.toList());
        JSONArray userArray = userServiceFeign.getSimpleUserByList(userIds).getData().getJSONArray("users");
        Map<Integer, JSONObject> userMap = userArray.stream()
                .map(o -> (JSONObject) JSON.toJSON(o))
                .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o));
        JSONArray articleArray = (JSONArray) JSON.toJSON(list);
        for (int i = 0; i < articleArray.size(); i++) {
            JSONObject article = articleArray.getJSONObject(i);
            JSONObject source = article.getJSONObject("_source");
            source.put("user", userMap.get(source.getInteger("author")));
        }
        result.put("result", articleArray);
        result.put("total", countResponse.getCount());
        return result;
    }

    private JSONObject searchTag(Integer page, Integer size, String search) throws IOException {
        SearchRequest searchRequest = new SearchRequest(TAG_INDEX);
        CountRequest countRequest = new CountRequest(TAG_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(QueryBuilders.multiMatchQuery(search, "title", "description"));

        boolQueryBuilder.filter(QueryBuilders.termQuery("isDel", false));
        sourceBuilder.query(boolQueryBuilder);
        countRequest.source(sourceBuilder);
        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);

        sourceBuilder.from(page * size);
        sourceBuilder.size(size);
        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        JSONObject result = new JSONObject();
        List<JSONObject> list = Arrays.stream(hits).
                map(o -> JSON.parseObject(o.toString())).
                collect(Collectors.toList());
        result.put("result", JSON.toJSON(list));
        result.put("total", countResponse.getCount());
        return result;
    }

    private JSONObject searchUser(Integer page, Integer size, String search) throws IOException {
        SearchRequest searchRequest = new SearchRequest(USER_INDEX);
        CountRequest countRequest = new CountRequest(USER_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.should(QueryBuilders.multiMatchQuery(search, "username", "introduction"));
        boolQueryBuilder.should(QueryBuilders.matchPhrasePrefixQuery("username", search));

        sourceBuilder.query(boolQueryBuilder);
        countRequest.source(sourceBuilder);
        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);

        sourceBuilder.from(page * size);
        sourceBuilder.size(size);
        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        JSONObject result = new JSONObject();
        List<JSONObject> list = Arrays.stream(hits).
                map(o -> JSON.parseObject(o.toString())).
                collect(Collectors.toList());
        result.put("result", JSON.toJSON(list));
        result.put("total", countResponse.getCount());
        return result;
    }

    private JSONObject getIndexArticle(JSONArray tagArray, Integer page, Integer size) throws IOException {
        SearchRequest searchRequest = new SearchRequest(ARTICLE_INDEX);
        CountRequest countRequest = new CountRequest(ARTICLE_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        String tagString = tagArray.toJSONString().replaceAll("\\[", "");
        tagString = tagString.replaceAll("]", "");
        if (StringUtils.isNotBlank(tagString)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("tags", tagString));
        }

        boolQueryBuilder.filter(QueryBuilders.termQuery("isDel", false));
        sourceBuilder.query(boolQueryBuilder);
        countRequest.source(sourceBuilder);
        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);

        sourceBuilder.sort("createTime", SortOrder.DESC);
        sourceBuilder.from(page * size);
        sourceBuilder.size(size);
        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        JSONObject result = new JSONObject();
        List<JSONObject> list = Arrays.stream(hits)
                .map(o -> JSON.parseObject(o.toString()))
                .collect(Collectors.toList());
        List<Integer> userIds = list.stream().map(o -> {
            JSONObject source = o.getJSONObject("_source");
            return source.getInteger("author");
        }).distinct().collect(Collectors.toList());
        if (!userIds.isEmpty()) {
            JSONArray userArray = userServiceFeign.getSimpleUserByList(userIds).getData().getJSONArray("users");
            Map<Integer, JSONObject> userMap = userArray.stream()
                    .map(o -> (JSONObject) JSON.toJSON(o))
                    .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o));
            JSONArray articleArray = (JSONArray) JSON.toJSON(list);
            for (int i = 0; i < articleArray.size(); i++) {
                JSONObject article = articleArray.getJSONObject(i);
                JSONObject source = article.getJSONObject("_source");
                source.put("user", userMap.get(source.getInteger("author")));
            }
            result.put("result", articleArray);
        } else {
            result.put("result", new JSONArray());
        }
        result.put("total", countResponse.getCount());
        return result;
    }
}
