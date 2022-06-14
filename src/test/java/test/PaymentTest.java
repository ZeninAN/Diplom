package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.Card;
import data.DbUtils;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.CreditPage;
import page.StartPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        // Configuration.headless = true;
        open("http://localhost:8080");
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
        DbUtils.deleteTables();
    }
    @Test
    void shouldPaymentWithApprovedCard() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(1), getYearCard(2), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.successfulPaymentDebitCard();
        String actual = DbUtils.getStatusPayment();
        assertEquals("APPROVED", actual);
    }
    @Test
    void shouldPaymentWithApprovedCardExpires() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(0), getYearCard(0), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.successfulPaymentDebitCard();
        String actual = DbUtils.getStatusPayment();
        assertEquals("APPROVED", actual);
    }

    @Test
    void shouldPaymentWithDeclinedCard() {
        var startPage = new StartPage();
        Card card = new Card(
                getSecondCardNumber(), getMonthCard(0), getYearCard(1), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.invalidPaymentDebitCard();
        String actual = DbUtils.getStatusPayment();
        assertEquals("DECLINED", actual);
    }

    @Test
    void shouldPaymentWithDeclinedCardExpires() {
        var startPage = new StartPage();
        Card card = new Card(
                getSecondCardNumber(), getMonthCard(0), getYearCard(0), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.invalidPaymentDebitCard();
        String actual = DbUtils.getStatusPayment();
        assertEquals("DECLINED", actual);
    }

    @Test
    void shouldPaymentWithInvalidCardNumber() {
        var startPage = new StartPage();
        Card card = new Card(
                getInvalidCardNumber(), getMonthCard(2), getYearCard(1), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldPaymentWithInvalidCardNumberShort() {
        var startPage = new StartPage();
        Card card = new Card(
                getInvalidShortCardNumber(), getMonthCard(2), getYearCard(1), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldPaymentExpiredCard() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(0), getYearCard(-1), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkCardExpired();
    }

    @Test
    void shouldPaymentIncorrectCardExpirationDate() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(-1), getYearCard(0), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldPaymentCardValidMoreThanFiveYears() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(1), getYearCard(6), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldPaymentCardInvalidYearOneDigit() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(2), getInvalidYearCard(), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldPaymentInvalidOwnerCard() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(1), getYearCard(3), getInvalidOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidOwner();
    }

    @Test
    void shouldPaymentInvalidOwnerCardInCyrillic() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(1), getYearCard(3),
                getInvalidOwnerCardCyrillic(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.incorrectOwner();
    }

    @Test
    void shouldPaymentInvalidOwnerCardWithNumbers() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(1), getYearCard(3),
                getInvalidOwnerCardWithNumbers(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.incorrectOwner();
    }

    @Test
    void shouldPaymentInvalidOwnerCardOneLetterName() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(1), getYearCard(3),
                getInvalidOwnerCardOneLetterName(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.incorrectOwner();
    }


    @Test
    void shouldPaymentCardInvalidCvc() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(1), getYearCard(2), getOwnerCard(), getInvalidCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldPaymentCardInvalidMonthOneDigit() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getInvalidMonthCardOneDigit(), getYearCard(2), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldPaymentCardInvalidMonthInvalidPeriod() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getInvalidMonthCardInvalidPeriod(), getYearCard(2), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldPaymentCardInvalidMonth() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getInvalidMonthCard(), getYearCard(2), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldPaymentCardInvalidYear() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(1), getInvalidYearCard(), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldPaymentEmptyFieldNumberCard() {
        var startPage = new StartPage();
        Card card = new Card(
                null, getMonthCard(1), getYearCard(2), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldPaymentEmptyFieldMonth() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), null, getYearCard(2), getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldPaymentEmptyFieldYears() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(2), null, getOwnerCard(), getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldPaymentEmptyFieldOwner() {
        var startPage = new StartPage();
         Card card = new Card(
                getFirstCardNumber(), getMonthCard(2), getYearCard(3), null, getCvc());
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkEmptyField();
    }

    @Test
    void shouldPaymentEmptyFieldCvc() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(2), getYearCard(3), getOwnerCard(), null);
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkEmptyField();
    }

    @Test
    void shouldPaymentEmptyAllField() {
        var startPage = new StartPage();
        Card card = new Card(
                null, null, null, null, null);
        var paymentPage = startPage.payment();
        paymentPage.getFillCardDetails(card);
        paymentPage.checkAllFieldsAreRequired();
    }
}