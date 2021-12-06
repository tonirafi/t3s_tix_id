package com.tes_tix_id.android.ui.home
import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tes_tix_id.android.configapp.di.appModule
import com.tes_tix_id.android.configapp.http.model.User
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.mockito.MockitoAnnotations
import io.reactivex.observers.TestObserver
import org.junit.*
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class HomeRepositoryTest: KoinTest {

    private val homeRepository: HomeRepository by inject()
    @Mock
    private lateinit var context: Application

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()


    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        startKoin {
            androidContext(context)
            modules(appModule)
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun getListUser() {
        val testObserver = TestObserver<ArrayList<User>>()
        homeRepository.loadDataUsers(10,1)?.subscribe(testObserver)
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)
    }
}