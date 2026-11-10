package vn.edu.vlu.lab09

data class PhotoItem(
    val id: Int,
    val title: String,
    val author: String,
    val imageUrl: String
)

object PhotoDataSource {
    fun getPhotos(): List<PhotoItem> = (1..10).map { id ->
        PhotoItem(
            id = id,
            title = "Photo $id",
            author = "Lorem Picsum",
            imageUrl = "https://picsum.photos/seed/lab09-$id/1200/900"
        )
    }
}