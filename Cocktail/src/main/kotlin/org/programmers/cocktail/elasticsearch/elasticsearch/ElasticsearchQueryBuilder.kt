package org.programmers.cocktail.elasticsearch.elasticsearch

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation
import co.elastic.clients.elasticsearch._types.query_dsl.*
import co.elastic.clients.elasticsearch.core.SearchRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

class ElasticsearchQueryBuilder(private val elasticsearchClientComponent: ElasticsearchClientComponent) {
    @Throws(IOException::class)
    fun fetchWordCloudData(gender: String?, ageGroup: String): Map<String, Long> {
        val ageStart = ageGroup.toInt()
        val ageEnd = ageStart + 9

        log.info("Fetching word cloud data")

        val response = elasticsearchClientComponent.client.search(
            { s: SearchRequest.Builder ->
                s
                    .index("cocktail_interactions") // Elasticsearch 인덱스
                    .size(0) // 문서 제외, 집계만 반환
                    .query { q: Query.Builder ->
                        q.bool { b: BoolQuery.Builder ->
                            b
                                .filter { f: Query.Builder ->
                                    f.term { t: TermQuery.Builder ->
                                        t
                                            .field("gender.keyword") // 성별 필드
                                            .value(gender)
                                    } // 성별 값
                                }
                                .filter { f: Query.Builder ->
                                    f.range { r: RangeQuery.Builder ->
                                        r
                                            .number { n: NumberRangeQuery.Builder ->
                                                n
                                                    .field("age") // 연령 필드
                                                    .gte(ageStart.toDouble()) // 시작 연령
                                                    .lte(ageEnd.toDouble())
                                            } // 종료 연령
                                    }
                                }
                        }
                    }
                    .aggregations(
                        "wordcloud"
                    ) { a: Aggregation.Builder ->
                        a
                            .terms { t: TermsAggregation.Builder ->
                                t
                                    .field("cocktailName.keyword") // 댓글 키워드
                                    .size(100)
                            } // 상위 100개
                    }
            },
            Void::class.java
        )

        log.info("Search response received. Parsing results...")

        val wordCloudData: MutableMap<String, Long> = HashMap()
        val wordCloudAgg = response.aggregations()["wordcloud"]!!.sterms()

        for (bucket in wordCloudAgg.buckets().array()) {
            val key = bucket.key().stringValue()
            val docCount = bucket.docCount()
            wordCloudData[key] = docCount

            log.debug("Word: {}, Count: {}", key, docCount)
        }

        log.info("Word cloud data fetching completed. Total words: {}", wordCloudData.size)
        return wordCloudData
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(
            ElasticsearchQueryBuilder::class.java
        )
    }
}
