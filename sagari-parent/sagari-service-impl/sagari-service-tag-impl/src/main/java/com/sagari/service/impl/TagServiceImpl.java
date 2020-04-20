package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.TagInputDTO;
import com.sagari.service.TagService;
import com.sagari.service.entity.CategoryVO;
import com.sagari.service.entity.Tag;
import com.sagari.service.entity.TagVO;
import com.sagari.service.mapper.CategoryMapper;
import com.sagari.service.mapper.TagFollowMapper;
import com.sagari.service.mapper.TagMapper;
import com.xxl.sso.core.login.SsoTokenLoginHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author biubiupiu~
 */
@RestController
@EnableScheduling
public class TagServiceImpl extends BaseApiService<JSONObject> implements TagService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagFollowMapper tagFollowMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${hot.tag.count}")
    private Integer hotCount;
    private static Logger log = LoggerFactory.getLogger(TagServiceImpl.class);

    @Override
    public BaseResponse<JSONObject> createTag(@RequestBody @Valid TagInputDTO tagInputDTO,
                                              BindingResult bindingResult) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("您需要登录之后才可以创建标签");
        }
        if (bindingResult.hasErrors()) {
            String errorMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return setResultError(errorMsg);
        }
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagInputDTO, tag);
        tag.setArticleCount(0);
        tag.setCreateTime(System.currentTimeMillis());
        tag.setUpdateTime(tag.getCreateTime());
        tag.setDel(false);
        tag.setDescription("");
        if (tagMapper.createTag(tag) > 0) {
            categoryMapper.incrementTagCount(tag.getCategoryId());
            cacheSyncDB();
            return setResultSuccess("创建标签成功");
        }
        return setResultError("创建标签失败");
    }

    @Override
    public BaseResponse<JSONObject> getTag() {
        JSONObject cache = getCache();
        if (!cache.isEmpty() && !cache.getJSONArray("category").isEmpty()) {
            return setResultSuccess(cache);
        }
        List<CategoryVO> categoryList = getAllCategory();
        List<TagVO> tagList = getTags();
        JSONObject result = new JSONObject();
        JSONArray categoryArray = (JSONArray) JSON.toJSON(categoryList);
        if (tagList == null || tagList.isEmpty()) {
            for (int i = 0; i < categoryArray.size(); i++) {
                JSONObject category = categoryArray.getJSONObject(i);
                category.put("tags", new ArrayList<>());
            }
            result.put("category", categoryArray);
            return setResultSuccess(result);
        }
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser != null) {
            Integer userId = Integer.valueOf(xxlUser.getUserid());
            Set<Integer> follow = new HashSet<>(tagFollowMapper.getFollow(userId));
            for (TagVO vo : tagList) {
                if (follow.contains(vo.getId())) {
                    vo.setFollow(true);
                } else {
                    vo.setFollow(false);
                }
            }
        }
        Map<Integer, List<TagVO>> tagMap = tagList.stream().collect(Collectors.groupingBy(TagVO::getCategoryId));
        for (int i = 0; i < categoryArray.size(); i++) {
            JSONObject category = categoryArray.getJSONObject(i);
            List<TagVO> tags = tagMap.get(category.getInteger("id"));
            if (tags == null || tags.isEmpty()) {
                category.put("tags", new ArrayList<>());
            } else {
                category.put("tags", tags);
            }
        }
        result.put("category", categoryArray);
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> getHotTag() {
        JSONObject result = new JSONObject();
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();
        Set<String> tags = opsForZSet.reverseRange("tags", 0, hotCount - 1);
        if (tags != null && !tags.isEmpty()) {
            List<JSONObject> tagList = tags.stream().map(JSON::parseObject).collect(Collectors.toList());
            result.put("hotTags", JSON.toJSON(tagList));
            return setResultSuccess(result);
        }
        List<TagVO> hotTag = tagMapper.getHotTag();
        if (hotTag == null || hotTag.isEmpty()) {
            result.put("hotTags", new ArrayList<>());
        } else {
            result.put("hotTags", JSON.toJSON(hotTag));
        }
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> getTagBatch(@RequestBody List<Integer> tagIds) {
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();
        Set<String> tagSet = opsForZSet.range("tags", 0, -1);
        JSONObject result = new JSONObject();
        if (tagSet != null && !tagSet.isEmpty()) {
            List<JSONObject> tagList = tagSet.stream()
                    .map(JSON::parseObject)
                    .filter(o -> tagIds.contains(o.getInteger("id")))
                    .collect(Collectors.toList());
            if (!tagList.isEmpty()) {
                result.put("tags", JSON.toJSON(tagList));
                return setResultSuccess(result);
            }
        }
        List<TagVO> tagVOList = tagMapper.getTagBatch(tagIds);
        result.put("tags", JSON.toJSON(tagVOList));
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> incrArticleCount(@RequestBody List<Integer> tagIds) {
        if (tagMapper.incrArticleCount(tagIds) > 0) {
            return setResultSuccess();
        }
        return setResultError("increment article_count failed");
    }

    @Override
    public BaseResponse<JSONObject> getCategory() {
        JSONObject result = new JSONObject();
        ListOperations<String, String> opsForList = redisTemplate.opsForList();
        List<String> category = opsForList.range("category", 0, -1);
        if (category != null && !category.isEmpty()) {
            List<JSONObject> list = category.stream().map(JSON::parseObject).collect(Collectors.toList());
            result.put("category", JSON.toJSON(list));
            return setResultSuccess(result);
        }
        List<CategoryVO> allCategory = getAllCategory();
        result.put("category", JSON.toJSON(allCategory));
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> toggleFollowTag(Integer tagId) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("您需要登录之后才可以关注该标签");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (tagFollowMapper.isFollow(tagId, userId) > 0) {
            if (tagFollowMapper.cancelFollow(tagId, userId) > 0) {
                return setResultSuccess();
            }
        } else {
            if (tagFollowMapper.follow(tagId, userId, System.currentTimeMillis()) > 0) {
                return setResultSuccess();
            }
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> getFollowTags(Integer userId) {
        List<Integer> follow = tagFollowMapper.getFollow(userId);
        JSONObject result = new JSONObject();
        result.put("tags", JSON.toJSON(follow));
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> getTagsByCategory(Integer categoryId) {
        List<Integer> tags = tagMapper.getTagsByCategory(categoryId);
        JSONObject result = new JSONObject();
        result.put("tags", JSON.toJSON(tags));
        return setResultSuccess(result);
    }

    @Scheduled(fixedRate = 300000)
    private void cacheSyncDB() {
        log.info("cache sync started");
        log.info("cache clearing....");
        Boolean flag1 = redisTemplate.delete("category");
        Boolean flag2 = redisTemplate.delete("tags");
        if (flag1 != null && flag1 && flag2 != null && flag2) {
            log.info("cache cleared");
        } else {
            log.warn("an error occurred while clearing the cache");
        }
        List<CategoryVO> category = getAllCategory();
        if (category == null || category.isEmpty()) {
            log.error("cache sync occur a error -> category is null or empty");
        }
        ListOperations<String, String> opsForList = redisTemplate.opsForList();
        for (CategoryVO categoryVO : category) {
            opsForList.rightPush("category", JSON.toJSONString(categoryVO));
        }
        List<TagVO> tag = getTags();
        if (tag == null || tag.isEmpty()) {
            log.error("cache sync occur a error -> tag is null or empty");
        }
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();
        for (TagVO tagVO : tag) {
            opsForZSet.add("tags", JSON.toJSONString(tagVO), Double.valueOf(tagVO.getArticleCount()));
        }
        log.info("cache sync finished");
    }

    private JSONObject getCache() {
        ListOperations<String, String> opsForList = redisTemplate.opsForList();
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();
        List<String> category = opsForList.range("category", 0, -1);
        if (category == null || category.isEmpty()) {
            return new JSONObject();
        }
        Set<String> tags = opsForZSet.range("tags", 0, -1);
        if (tags == null || tags.isEmpty()) {
            return new JSONObject();
        }
        JSONArray categoryArray = (JSONArray) JSON.toJSON(category);
        List<JSONObject> categoryList = categoryArray.stream()
                .map(o -> JSON.parseObject((String) o))
                .collect(Collectors.toList());
        JSONArray tagsArray = (JSONArray) JSON.toJSON(tags);
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        List<JSONObject> tagObject = new ArrayList<>(tags.size());
        if (xxlUser != null) {
            Integer userId = Integer.valueOf(xxlUser.getUserid());
            Set<Integer> follow = new HashSet<>(tagFollowMapper.getFollow(userId));
            for (int i = 0; i < tagsArray.size(); i++) {
                JSONObject tag = JSON.parseObject(tagsArray.getString(i));
                if (follow.contains(tag.getInteger("id"))) {
                    tag.put("follow", true);
                } else {
                    tag.put("follow", false);
                }
                tagObject.add(tag);
            }
        }
        Map<Integer, List<JSONObject>> tagMap = tagObject.stream()
                .collect(Collectors.groupingBy(o -> o.getInteger("categoryId")));
        for (JSONObject object : categoryList) {
            List<JSONObject> tagVoGroup = tagMap.get(object.getInteger("id"));
            if (tagVoGroup == null || tagVoGroup.isEmpty()) {
                object.put("tags", new ArrayList<>());
            } else {
                object.put("tags", tagVoGroup);
            }
        }
        JSONObject cache = new JSONObject();
        cache.put("category", JSON.toJSON(categoryList));
        return cache;
    }

    private List<CategoryVO> getAllCategory() {
        return categoryMapper.getCategory();
    }

    private List<TagVO> getTags() {
        return tagMapper.getTag();
    }
}
