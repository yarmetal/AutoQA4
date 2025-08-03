import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class RegistrationTest {
    public static String getLocalDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy",
                new Locale("ru")));
    }


    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
        boolean holdBrowserOpen = true;
    }

    @Test
    void shouldTestValidCity() {
        String meetingDate = getLocalDate(19);
        $("[placeholder=Город]").setValue("Ярославль");
        $("[data-test-id=\"date\"] span.input__box [placeholder=\"Дата встречи\"")
                .doubleClick().sendKeys(meetingDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").
                shouldHave(Condition.text("Встреча успешно забронирована на " + meetingDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldTestInvalidCity() {
        String planningDate = getLocalDate(5);
        $x("//input[@placeholder=\"Город\"]").val("Тутаев");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иванов Иван");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("Доставка в выбранный город недоступна")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestCityInEnglish() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Yaroslavl");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("Доставка в выбранный город недоступна")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestCityIsEmpty() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("Поле обязательно")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestCityWithSymbols() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль!");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("недоступна")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNameWithE() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Семён Семёнов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("только русские буквы, пробелы и дефисы")).should(visible);
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestDoubleSurname() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов-Петров");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").
                shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNameWithNum() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("12345 6789");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("только русские буквы, пробелы и дефисы")).should(visible);
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNameInEnglish() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Ivan Ivanov");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("только русские буквы, пробелы и дефисы")).should(visible);
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNameWithSymbols() {
        String planningDate = getLocalDate(6);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов?");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("только русские буквы, пробелы и дефисы")).should(visible);
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPhoneWithoutPlus() {
        String planningDate = getLocalDate(90);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPhoneWithoutOneNumber() {
        String planningDate = getLocalDate(90);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+7999888776");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPhoneWithSymbols() {
        String planningDate = getLocalDate(90);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+7(999)-888-77 66");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPhoneIsEmpty() {
        String planningDate = getLocalDate(90);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("обязательно для заполнения")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNextDayMeeting() {
        String planningDate = getLocalDate(1);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("дату невозможен")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPlusNulldays() {
        String planningDate = getLocalDate(0);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("дату невозможен")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestMinusFiveDays() {
        String planningDate = getLocalDate(-5);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("дату невозможен")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestUncheckedBox() {
        String planningDate = getLocalDate(4);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").doubleClick();
        $x("//*[@class=\"button__text\"]").click();
        $(".input_invalid[data-test-id=\"agreement\"]").should(exist);
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));

    }

    @Test
    void shouldTestCheckedCheckBox() {
        String planningDate = getLocalDate(4);
        $x("//input[@placeholder=\"Город\"]").val("Ярославль");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79998887766");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $("[data-test-id=agreement].checkbox_checked").should(exist);
        $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").
                shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15));
    }


}
