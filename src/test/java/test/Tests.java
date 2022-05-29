package test;

import data.Card;
import org.apache.commons.dbutils.DbUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.CreditPage;
import page.StartPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }

    @Test
    void test(){
        open("http://localhost:8080");
    }

    @Test
    void shouldBuyInCreditGate() throws SQLException {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkSuccessNotification();
        assertEquals("APPROVED",data.DbUtils.getCreditStatus());
    }
}