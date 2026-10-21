package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("StudyPilot", appName)
  }

  @Test
  fun `test repository default data and language toggle`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = com.example.data.repository.StudyPilotRepository(context)

    assertEquals(5, repo.subjects.value.size)
    assertEquals("Physics", repo.subjects.value[0].name)
    assertEquals(4, repo.sessions.value.size)

    repo.setLanguage("bn")
    assertEquals("bn", repo.language.value)

    repo.setLanguage("en")
    assertEquals("en", repo.language.value)
  }
}
