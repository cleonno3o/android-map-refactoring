package campus.tech.kakao.map

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import campus.tech.kakao.map.data.repository.HistoryRepositoryImpl
import campus.tech.kakao.map.data.repository.LastLocationRepositoryImpl
import campus.tech.kakao.map.data.source.MapDatabase
import campus.tech.kakao.map.data.source.MapDbHelper
import campus.tech.kakao.map.domain.model.Location
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LastLocationTest {
    lateinit var mapDatabase: MapDatabase
    lateinit var lastLocationRepositoryImpl: LastLocationRepositoryImpl

    @Before
    fun init() {
        mapDatabase = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MapDatabase::class.java,
            "map"
        ).build()
        lastLocationRepositoryImpl = LastLocationRepositoryImpl(mapDatabase)
    }

    @Test
    fun `마지막 위치가 존재하지 않는 경우 요청시 null을 반환한다`() = runTest {
        val lastLocation = lastLocationRepositoryImpl.getLastLocation()
        assertNull(lastLocation)
    }

    @Test
    fun `마지막 위치는 항상 이전 장소를 지우고 저장하여 1개만 가진다`() = runTest {
        val history = mutableListOf<Location>()
        for (i in 1..10) {
            val testLoc = Location(
                "$i",
                "testName$i",
                "TEST",
                "Test시 Test구 Test로 $i",
                i + 11.0,i + 100.0)
            history.add(testLoc)
            lastLocationRepositoryImpl.insertLastLocation(testLoc)
        }
        val lastLocation = lastLocationRepositoryImpl.getLastLocation()
        assertEquals(lastLocation, history.last())
    }
}