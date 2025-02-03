package org.programmers.cocktail.elasticsearch.service

import co.elastic.clients.elasticsearch.core.BulkRequest
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation
import co.elastic.clients.elasticsearch.core.BulkResponse
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import jakarta.transaction.Transactional
import org.programmers.cocktail.elasticsearch.dto.ElasticsearchRequest
import org.programmers.cocktail.elasticsearch.elasticsearch.ElasticsearchClientComponent
import org.programmers.cocktail.entity.CocktailLikes
import org.programmers.cocktail.entity.Comments
import org.programmers.cocktail.repository.cocktail_likes.CocktailLikesRepository
import org.programmers.cocktail.repository.cocktails.CocktailsRepository
import org.programmers.cocktail.repository.comments.CommentsRepository
import org.programmers.cocktail.repository.users.UsersRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserSyncService(
    private val elasticsearchClientComponent: ElasticsearchClientComponent,
    private val usersRepository: UsersRepository,
    private val cocktailsRepository: CocktailsRepository,
    private val cocktailLikesRepository: CocktailLikesRepository,
    private val commentsRepository: CommentsRepository,
) {
    private var lastSyncTime: LocalDateTime? = null

    companion object {
        private val logger = LoggerFactory.getLogger(UserSyncService::class.java)
        private const val BATCH_SIZE = 10_000
        private const val SYNC_INTERVAL = 60_000L // 1 minute
    }

    val objectMapper = ObjectMapper().apply {
        registerModule(JavaTimeModule())
    }

    @Transactional
    @Scheduled(fixedRate = SYNC_INTERVAL)
    fun syncToElasticsearch() {
        try {
            lastSyncTime ?: findMostRecentUpdatedAt().also { lastSyncTime = it }

            syncLikes()
            syncComments()

            lastSyncTime = LocalDateTime.now()
            logger.info("Successfully synced all data to Elasticsearch.")
        } catch (e: Exception) {
            logger.error("Failed to sync data to Elasticsearch", e)
            throw RuntimeException("Failed to sync data to Elasticsearch", e)
        }
    }

    private fun syncLikes() {
        val likes = cocktailLikesRepository.findByUpdatedAtAfter(lastSyncTime)
            ?: return

        likes.chunked(BATCH_SIZE).forEachIndexed { index, batch ->
            logger.info("Processing likes batch ${index + 1}")

            val bulkRequest = BulkRequest.Builder()
                .operations(batch.map { like ->
                    BulkOperation.Builder()
                        .index { idx ->
                            idx.index("cocktail_interactions")
                                .id("${like?.users?.id}_${like?.cocktails?.id}_like")
                                .document(like?.let { convertLikeToDTO(it) })
                        }.build()
                }).build()

            executeBulkRequest(bulkRequest)
        }

        logger.info("Finished syncing likes")
    }

    private fun syncComments() {
        val comments = commentsRepository.findByUpdatedAtAfter(lastSyncTime)
            ?: return

        comments.chunked(BATCH_SIZE).forEachIndexed { index, batch ->
            logger.info("Processing comments batch ${index + 1}")

            val bulkRequest = BulkRequest.Builder()
                .operations(batch.map { comment ->
                    BulkOperation.Builder()
                        .index { idx ->
                            idx.index("cocktail_interactions")
                                .id("${comment?.users?.id}_${comment?.cocktails?.id}_comment_${comment?.id}")
                                .document(comment?.let { convertCommentToDTO(it) })
                        }.build()
                }).build()

            executeBulkRequest(bulkRequest)
        }

        logger.info("Finished syncing comments")
    }

    private fun executeBulkRequest(bulkRequest: BulkRequest) {
        val response = elasticsearchClientComponent.client.bulk(bulkRequest)

        if (response.errors()) {
            response.items().filter { it.error() != null }.forEach { item ->
                logger.error("Error syncing document with ID ${item.id()}: ${item.error()?.reason()}")
            }
            throw RuntimeException("Some documents failed to sync.")
        }
    }

    private fun convertLikeToDTO(like: CocktailLikes) = like.createdAt?.let {
        ElasticsearchRequest(it).apply {
        userId = like.users.id
        age = like.users.age
        gender = like.users.gender
        cocktailId = like.cocktails.id
        cocktailName = like.cocktails.name
        category = like.cocktails.category
        ingredients = like.cocktails.ingredients
        interactionType = "like"
    }
    }

    private fun convertCommentToDTO(comment: Comments) = comment.createdAt?.let {
        ElasticsearchRequest(it).apply {
        userId = comment.users.id
        age = comment.users.age
        gender = comment.users.gender
        cocktailId = comment.cocktails.id
        cocktailName = comment.cocktails.name
        category = comment.cocktails.category
        ingredients = comment.cocktails.ingredients
        interactionType = "comment"
        commentContent = comment.content
    }
    }

    private fun findMostRecentUpdatedAt(): LocalDateTime {
        val mostRecentLikes = cocktailLikesRepository.findMostRecentUpdatedAt()
        val mostRecentComments = commentsRepository.findMostRecentUpdatedAt()
        return maxOf(mostRecentLikes ?: LocalDateTime.MIN, mostRecentComments ?: LocalDateTime.MIN)
    }
}