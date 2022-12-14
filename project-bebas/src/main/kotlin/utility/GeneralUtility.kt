package utility

import io.smallrye.mutiny.Uni
import java.util.stream.Collectors
import javax.persistence.TypedQuery

object GeneralUtility {
    fun<T> uniDataItemList(namedQuery: TypedQuery<T>) : Uni<List<T>> {
        return Uni.createFrom().item(Unit)
            .map { namedQuery.resultStream.collect(Collectors.toList()) }
            .onFailure().recoverWithItem{ _ -> listOf<T>()}
    }
}