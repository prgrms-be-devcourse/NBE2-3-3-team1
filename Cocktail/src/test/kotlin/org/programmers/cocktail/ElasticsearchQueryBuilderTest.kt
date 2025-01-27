package org.programmers.cocktail

import org.junit.Assert
import org.junit.jupiter.api.Test
import org.programmers.cocktail.elasticsearch.elasticsearch.ElasticsearchClientComponent
import org.programmers.cocktail.elasticsearch.elasticsearch.ElasticsearchQueryBuilder
import java.io.IOException

class ElasticsearchQueryBuilderTest {
    @Test
    @Throws(IOException::class)
    fun testFetchWordCloudData() {
        // Mock ElasticsearchClientComponent
        val mockClient = ElasticsearchClientComponent()
        val queryBuilder = ElasticsearchQueryBuilder(mockClient)

        // Fetch word cloud data
        val result = queryBuilder.fetchWordCloudData("Male", "20")

        result.forEach { (word: String?, count: Long?) ->
            println("단어: $word, 빈도수: $count")
        }
        // Assertions
        Assert.assertNotNull(result)
        println("Fetched word cloud data: $result")
    }
}
