package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {

		// トップページへアクセス
		goTo("http://localhost:8080/lms/");

		// ログイン画面が表示されていることを確認
		WebElement title = webDriver.findElement(By.tagName("h2"));
		assertEquals("ログイン", title.getText());
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {

		// DBに登録されていないログインIDを入力
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		loginId.sendKeys("not_registered_user");

		// パスワードを入力
		WebElement password = webDriver.findElement(By.id("password"));
		password.sendKeys("invalid_password");

		// ログインボタンを押下
		webDriver.findElement(
				By.cssSelector("input[type='submit'][value='ログイン']")).click();

		// 認証失敗メッセージが表示されるまで待機
		By errorLocator = By.cssSelector("form.form-horizontal > span.help-inline.error");
		visibilityTimeout(errorLocator, 5);

		// 認証失敗メッセージを確認
		WebElement errorMessage = webDriver.findElement(errorLocator);
		assertTrue(errorMessage.getText().contains("ログインに失敗しました。"));

		// ログイン画面に留まっていることを確認
		WebElement title = webDriver.findElement(By.tagName("h2"));
		assertEquals("ログイン", title.getText());

		// エビデンス取得
		getEvidence(new Object() {
		});
	}
}
