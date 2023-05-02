package nz.ac.canterbury.seng440.geoimage.data

import nz.ac.canterbury.seng440.geoimage.R
import nz.ac.canterbury.seng440.geoimage.model.LocationPoint
import nz.ac.canterbury.seng440.geoimage.model.Photo
import kotlin.random.Random

object DataProvider{

    val photoList = listOf(
        Photo(
            name = "test1",
            imageId = R.drawable.test1
        ),
        Photo(
            name = "test2",
            imageId = R.drawable.test2
        ),
        Photo(
            name = "test3",
            imageId = R.drawable.test3
        ),
        Photo(
            name = "test4",
            imageId = R.drawable.test4
        ),
        Photo(
            name = "test5",
            imageId = R.drawable.test5
        ),
        Photo(
            name = "test6",
            imageId = R.drawable.test6
        ),
        Photo(
            name = "test1",
            imageId = R.drawable.test1
        ),
        Photo(
            name = "test2",
            imageId = R.drawable.test2
        ),
        Photo(
            name = "test3",
            imageId = R.drawable.test3
        ),
        Photo(
            name = "test4",
            imageId = R.drawable.test4
        ),
        Photo(
            name = "test5",
            imageId = R.drawable.test5
        ),
        Photo(
            name = "test6",
            imageId = R.drawable.test6
        )
    )

    val coordinates : Collection<LocationPoint> = List(100) {
        LocationPoint(Random.nextDouble(-90.0,90.0), Random.nextDouble(-90.0, 90.0))
    }

}