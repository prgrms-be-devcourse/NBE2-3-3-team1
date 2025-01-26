package org.programmers.cocktail.elasticsearch.elasticsearch

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import lombok.Getter
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.springframework.stereotype.Component

@Getter
@Component
class ElasticsearchClientComponent {
    final val client: ElasticsearchClient


    init {
        val elasticIp = System.getenv("ELASTIC_IP")
        val elasticPort = System.getenv("ELASTIC_PORT").toInt()
        val restClient = RestClient.builder(
            HttpHost(elasticIp, elasticPort, "http")
        ).build()

        val transport = RestClientTransport(restClient, JacksonJsonpMapper())

        this.client = ElasticsearchClient(transport)
    }
}
