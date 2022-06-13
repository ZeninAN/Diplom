package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import data.Card;
import data.DbUtils;
import page.StartPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static data.DataGenerator.*;

public class CreditPage {

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
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.successfulPaymentCreditCard();
            String actual = DbUtils.getStatusCredit();
            assertEquals("APPROVED", actual);
        }
        @Test
        void shouldPaymentWithApprovedCardExpires() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getMonthCard(0), getYearCard(0), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.successfulPaymentCreditCard();
            String actual = DbUtils.getStatusCredit();
            assertEquals("APPROVED", actual);
        }

        @Test
        void shouldPaymentWithDeclinedCard() {
            var startPage = new StartPage();
            Card card = new Card(
                    getSecondCardNumber(), getMonthCard(0), getYearCard(1), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.invalidPaymentCreditCard();
            String actual = DbUtils.getStatusCredit();
            assertEquals("DECLINED", actual);
        }

        @Test
        void shouldPaymentWithDeclinedCardExpires() {
            var startPage = new StartPage();
            Card card = new Card(
                    getSecondCardNumber(), getMonthCard(0), getYearCard(0), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.invalidPaymentCreditCard();
            String actual = DbUtils.getStatusCredit();
            assertEquals("DECLINED", actual);
        }

        @Test
        void shouldPaymentWithInvalidCardNumber() {
            var startPage = new StartPage();
            Card card = new Card(
                    getInvalidCardNumber(), getMonthCard(2), getYearCard(1), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkInvalidCardValidityPeriod();
        }

        @Test
        void shouldPaymentWithInvalidCardNumberShort() {
            var startPage = new StartPage();
            Card card = new Card(
                    getInvalidShortCardNumber(), getMonthCard(2), getYearCard(1), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkInvalidFormat();
        }

        @Test
        void shouldPaymentExpiredCard() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getMonthCard(0), getYearCard(-1), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkCardExpired();
        }

        @Test
        void shouldPaymentIncorrectCardExpirationDate() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getMonthCard(-1), getYearCard(0), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkInvalidCardValidityPeriod();
        }

        @Test
        void shouldPaymentCardValidMoreThanFiveYears() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getMonthCard(1), getYearCard(6), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkInvalidCardValidityPeriod();
        }

        @Test
        void shouldPaymentCardInvalidYearOneDigit() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getMonthCard(2), getInvalidYearCard(), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkInvalidFormat();
        }

        @Test
        void shouldPaymentInvalidOwnerCard() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getMonthCard(1), getYearCard(3), getInvalidOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkInvalidOwner();
        }

        @Test
        void shouldPaymentInvalidOwnerCardInCyrillic() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getMonthCard(1), getYearCard(3),
                    getInvalidOwnerCardCyrillic(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.incorrectOwner();
        }

        @Test
        void shouldPaymentInvalidOwnerCardWithNumbers() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getMonthCard(1), getYearCard(3),
                    getInvalidOwnerCardWithNumbers(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.incorrectOwner();
        }

        @Test
        void shouldPaymentInvalidOwnerCardOneLetterName() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getMonthCard(1), getYearCard(3),
                    getInvalidOwnerCardOneLetterName(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.incorrectOwner();
        }


        @Test
        void shouldPaymentCardInvalidCvc() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getMonthCard(1), getYearCard(2), getOwnerCard(), getInvalidCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkInvalidFormat();
        }

        @Test
        void shouldPaymentCardInvalidMonthOneDigit() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getInvalidMonthCardOneDigit(), getYearCard(2), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkInvalidFormat();
        }

        @Test
        void shouldPaymentCardInvalidMonthInvalidPeriod() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getInvalidMonthCardInvalidPeriod(), getYearCard(2), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkInvalidCardValidityPeriod();
        }

        @Test
        void shouldPaymentCardInvalidMonth() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getInvalidMonthCard(), getYearCard(2), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkInvalidCardValidityPeriod();
        }

    @Test
    void shouldPaymentCardInvalidYear() {
        var startPage = new StartPage();
        Card card = new Card(
                getFirstCardNumber(), getMonthCard(1), getInvalidYearCard(), getOwnerCard(), getCvc());
        var creditPage = startPage.paymentOnCredit();
        creditPage.getFillCardDetails(card);
        creditPage.checkInvalidCardValidityPeriod();
    }

        @Test
        void shouldPaymentEmptyFieldNumberCard() {
            var startPage = new StartPage();
            Card card = new Card(
                    null, getMonthCard(1), getYearCard(2), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkInvalidFormat();
        }

        @Test
        void shouldPaymentEmptyFieldMonth() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), null, getYearCard(2), getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkInvalidFormat();
        }

        @Test
        void shouldPaymentEmptyFieldYears() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getMonthCard(2), null, getOwnerCard(), getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkInvalidFormat();
        }

        @Test
        void shouldPaymentEmptyFieldOwner() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getMonthCard(2), getYearCard(3), null, getCvc());
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkEmptyField();
        }

        @Test
        void shouldPaymentEmptyFieldCvc() {
            var startPage = new StartPage();
            Card card = new Card(
                    getFirstCardNumber(), getMonthCard(2), getYearCard(3), getOwnerCard(), null);
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkEmptyField();
        }

        @Test
        void shouldPaymentEmptyAllField() {
            var startPage = new StartPage();
            Card card = new Card(
                    null, null, null, null, null);
            var creditPage = startPage.paymentOnCredit();
            creditPage.getFillCardDetails(card);
            creditPage.checkAllFieldsAreRequired();
        }

    }
