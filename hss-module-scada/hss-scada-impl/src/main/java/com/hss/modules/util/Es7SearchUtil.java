package com.hss.modules.util;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * ES7搜索高亮全文检索工具类
 */
@Slf4j
@Component
public class Es7SearchUtil {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 高亮搜索
     *
     * @param indexName    索引名称
     * @param keyword      搜索关键词
     * @param fields       搜索字段
     * @param highlightFields 高亮字段
     * @param pageNum      页码
     * @param pageSize     每页大小
     * @param clazz        返回结果类型
     * @param <T>          泛型
     * @return 搜索结果
     */
    public <T> Map<String, Object> searchHighlight(String indexName, String keyword, String[] fields,
                                                   String[] highlightFields, Integer pageNum, Integer pageSize,
                                                   Class<T> clazz) {
        try {
            // 创建搜索请求
            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            // 构建查询条件
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            if (keyword != null && !keyword.isEmpty()) {
                for (String field : fields) {
                    boolQueryBuilder.should(QueryBuilders.matchQuery(field, keyword));
                }
                // 至少匹配一个should条件
                boolQueryBuilder.minimumShouldMatch(1);
            } else {
                boolQueryBuilder.must(QueryBuilders.matchAllQuery());
            }

            // 设置查询条件
            searchSourceBuilder.query(boolQueryBuilder);

            // 设置分页
            if (pageNum != null && pageSize != null) {
                searchSourceBuilder.from((pageNum - 1) * pageSize);
                searchSourceBuilder.size(pageSize);
            }

            // 设置高亮
            if (highlightFields != null && highlightFields.length > 0) {
                HighlightBuilder highlightBuilder = new HighlightBuilder();
                // 设置高亮标签
                highlightBuilder.preTags("<span style='color:red'>");
                highlightBuilder.postTags("</span>");

                // 设置高亮字段
                for (String highlightField : highlightFields) {
                    highlightBuilder.field(highlightField);
                }

                // 多个字段高亮需要设置为true
                highlightBuilder.requireFieldMatch(false);
                searchSourceBuilder.highlighter(highlightBuilder);
            }

            // 执行搜索
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // 解析结果
            return parseSearchResponse(searchResponse, clazz, highlightFields);
        } catch (Exception e) {
            log.error("ES高亮搜索异常", e);
            return null;
        }
    }

    /**
     * 高级搜索（支持自定义查询条件和排序）
     *
     * @param indexName       索引名称
     * @param queryBuilder    查询条件构建器
     * @param sortBuilders    排序构建器列表
     * @param highlightFields 高亮字段
     * @param pageNum         页码
     * @param pageSize        每页大小
     * @param clazz           返回结果类型
     * @param <T>             泛型
     * @return 搜索结果
     */
    public <T> Map<String, Object> advancedSearch(String indexName, QueryBuilder queryBuilder,
                                                  List<SortBuilder<?>> sortBuilders, String[] highlightFields,
                                                  Integer pageNum, Integer pageSize, Class<T> clazz) {
        try {
            // 创建搜索请求
            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            // 设置查询条件
            searchSourceBuilder.query(queryBuilder);

            // 设置排序
            if (sortBuilders != null && !sortBuilders.isEmpty()) {
                for (SortBuilder<?> sortBuilder : sortBuilders) {
                    searchSourceBuilder.sort(sortBuilder);
                }
            }

            // 设置分页
            if (pageNum != null && pageSize != null) {
                searchSourceBuilder.from((pageNum - 1) * pageSize);
                searchSourceBuilder.size(pageSize);
            }

            // 设置高亮
            if (highlightFields != null && highlightFields.length > 0) {
                HighlightBuilder highlightBuilder = new HighlightBuilder();
                // 设置高亮标签
                highlightBuilder.preTags("<span style='color:red'>");
                highlightBuilder.postTags("</span>");

                // 设置高亮字段
                for (String highlightField : highlightFields) {
                    highlightBuilder.field(highlightField);
                }

                // 多个字段高亮需要设置为true
                highlightBuilder.requireFieldMatch(false);
                searchSourceBuilder.highlighter(highlightBuilder);
            }

            // 执行搜索
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // 解析结果
            return parseSearchResponse(searchResponse, clazz, highlightFields);
        } catch (Exception e) {
            log.error("ES高级搜索异常", e);
            return null;
        }
    }

    /**
     * 解析搜索响应结果
     *
     * @param searchResponse  搜索响应
     * @param clazz           返回结果类型
     * @param highlightFields 高亮字段
     * @param <T>             泛型
     * @return 解析后的结果
     */
    private <T> Map<String, Object> parseSearchResponse(SearchResponse searchResponse, Class<T> clazz, String[] highlightFields) {
        Map<String, Object> result = new HashMap<>();
        List<T> list = new ArrayList<>();

        // 获取命中的文档
        SearchHit[] hits = searchResponse.getHits().getHits();
        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                // 获取文档源
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                // 处理高亮字段
                Map<String, HighlightField> highlightFields1 = hit.getHighlightFields();
                if (highlightFields != null && highlightFields.length > 0 && highlightFields1 != null) {
                    for (String field : highlightFields) {
                        HighlightField highlightField = highlightFields1.get(field);
                        if (highlightField != null) {
                            Text[] fragments = highlightField.getFragments();
                            if (fragments != null && fragments.length > 0) {
                                StringBuilder sb = new StringBuilder();
                                for (Text text : fragments) {
                                    sb.append(text);
                                }
                                sourceAsMap.put(field, sb.toString());
                            }
                        }
                    }
                }

                // 将Map转换为对象
                T item = mapToObject(sourceAsMap, clazz);
                list.add(item);
            }
        }

        // 设置返回结果
        result.put("list", list);
        result.put("total", searchResponse.getHits().getTotalHits().value);
        return result;
    }

    /**
     * 将Map转换为对象
     *
     * @param map   Map数据
     * @param clazz 目标类型
     * @param <T>   泛型
     * @return 转换后的对象
     */
    private <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (map.containsKey(fieldName)) {
                    Object value = map.get(fieldName);
                    if (value != null) {
                        // 处理类型转换
                        if (field.getType() == Long.class && value instanceof Integer) {
                            field.set(obj, Long.valueOf((Integer) value));
                        } else {
                            field.set(obj, value);
                        }
                    }
                }
            }
            return obj;
        } catch (Exception e) {
            log.error("Map转对象异常", e);
            return null;
        }
    }
}
