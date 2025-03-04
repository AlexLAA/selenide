package com.codeborne.selenide.commands;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.DriverStub;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.Alias;
import com.codeborne.selenide.impl.WebElementSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class ToStringCommandTest {
  private final SelenideElement proxy = mock();
  private final Driver driver = new DriverStub();
  private final WebElementSource locator = mock();
  private final WebElement mockedFoundElement = mock();
  private final ToString toStringCommand = new ToString();

  @BeforeEach
  void setup() {
    when(locator.driver()).thenReturn(driver);
    when(locator.getWebElement()).thenReturn(mockedFoundElement);
  }

  @Test
  void executeMethod() {
    when(mockedFoundElement.getTagName()).thenReturn("option");
    when(mockedFoundElement.isSelected()).thenReturn(true);
    when(mockedFoundElement.isDisplayed()).thenReturn(true);
    when(mockedFoundElement.getText()).thenReturn("Cinderella");
    String elementString = toStringCommand.execute(proxy, locator, new Object[]{});
    assertThat(elementString)
      .isEqualTo("<option selected:true>Cinderella</option>");
  }

  @Test
  void executeMethodWhenWebDriverDriverExceptionIsThrown() {
    doThrow(new WebDriverException()).when(locator).getWebElement();
    String elementString = toStringCommand.execute(proxy, locator, new Object[]{});
    assertThat(elementString)
      .contains("WebDriverException");
  }

  @Test
  void executeMethodWhenElementNotFoundIsThrown() {
    doThrow(new ElementNotFound(Alias.NONE, By.name("q"), Condition.visible)).when(locator).getWebElement();
    String elementString = toStringCommand.execute(proxy, locator, new Object[]{});
    assertThat(elementString)
      .isEqualTo(String.format("Element not found {By.name: q}%n" +
        "Expected: visible%n" +
        "Timeout: 0 ms."));
  }

  @Test
  void executeMethodWhenIndexOutOfBoundExceptionIsThrown() {
    doThrow(new IndexOutOfBoundsException()).when(locator).getWebElement();
    String elementString = toStringCommand.execute(proxy, locator, new Object[]{});
    assertThat(elementString)
      .isEqualTo("java.lang.IndexOutOfBoundsException");
  }
}
