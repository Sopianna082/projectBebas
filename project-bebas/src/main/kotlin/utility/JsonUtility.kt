package utility

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object JsonUtility {
    fun createJacksonMapper() : ObjectMapper {
        return jacksonObjectMapper().apply {
            propertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            registerModule(JavaTimeModule())
        }
    }

    fun <T> toJson(jsonObject: T) = createJacksonMapper().writeValueAsString(jsonObject)
    inline fun <reified T> fromJson(json: String) = createJacksonMapper().readValue<T>(json)

}