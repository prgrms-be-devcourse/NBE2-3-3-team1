package org.programmers.cocktail.list.service

import org.programmers.cocktail.list.dto.ListCocktail
import org.programmers.cocktail.repository.cocktails.CocktailsRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ListService(private val cocktailsRepository: CocktailsRepository) {

    // 로거 선언
    private val logger: Logger = LoggerFactory.getLogger(ListService::class.java)

    fun showList(): List<ListCocktail> {
        val result: MutableList<ListCocktail> = ArrayList()
        val cocktails = cocktailsRepository.findAllByOrderByLikesDesc()

        // 안전한 null 체크 및 각 칵테일 처리
        cocktails?.forEach { cocktail ->
            val listCocktail = ListCocktail(
                id = cocktail?.id,            // 생성자에서 값을 전달
                name = cocktail?.name,
                image_url = cocktail?.image_url,
                likes = cocktail?.likes
            )

            result.add(listCocktail)
        }

        // 로그 출력
        logger.info(result.toString())

        return result
    }

    // ID로 칵테일 가져오기
    fun getCocktailById(id: Long): ListCocktail {
        val optionalCocktail = cocktailsRepository.findById(id)

        if (optionalCocktail.isPresent) {
            val cocktail = optionalCocktail.get()

            // 생성자에서 값을 전달
            return ListCocktail(
                id = cocktail.id ?: 0L,       // null일 경우 기본값 처리
                name = cocktail.name,
                image_url = cocktail.image_url,
                likes = cocktail.likes
            )
        } else {
            throw RuntimeException("칵테일을 찾을 수 없습니다. ID: $id")
        }
    }
}