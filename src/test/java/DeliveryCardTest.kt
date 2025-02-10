import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.Selenide
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.openqa.selenium.Keys
import ru.netology.delivery.data.DataGenerator
import java.time.Duration

internal class DeliveryTest {
    @BeforeEach
    fun setup() {
        Selenide.open("http://localhost:9999")
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    fun shouldSuccessfulPlanAndReplanMeeting() {
        val validUser = DataGenerator.Registration.generateUser("ru")
        val daysToAddForFirstMeeting = 4
        val firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting)
        val daysToAddForSecondMeeting = 7
        val secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting)
        Selenide.`$`("[data-test-id=city] input").setValue(validUser.city)
        Selenide.`$`("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE)
        Selenide.`$`("[data-test-id=date] input").setValue(firstMeetingDate)
        Selenide.`$`("[data-test-id=name] input").setValue(validUser.name)
        Selenide.`$`("[data-test-id=phone] input").setValue(validUser.phone)
        Selenide.`$`("[data-test-id=agreement]").click()
        Selenide.`$`(Selectors.byText("Запланировать")).click()
        Selenide.`$`(Selectors.byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15))
        Selenide.`$`("[data-test-id='success-notification'] .notification__content")
            .shouldHave(Condition.exactText("Встреча успешно запланирована на $firstMeetingDate"))
            .shouldBe(Condition.visible)
        Selenide.`$`("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE)
        Selenide.`$`("[data-test-id=date] input").setValue(secondMeetingDate)
        Selenide.`$`(Selectors.byText("Запланировать")).click()
        Selenide.`$`("[data-test-id='replan-notification'] .notification__content")
            .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
            .shouldBe(Condition.visible)
        Selenide.`$`("[data-test-id='replan-notification'] button").click()
        Selenide.`$`("[data-test-id='success-notification'] .notification__content")
            .shouldHave(Condition.exactText("Встреча успешно запланирована на $secondMeetingDate"))
            .shouldBe(Condition.visible)
    }
}