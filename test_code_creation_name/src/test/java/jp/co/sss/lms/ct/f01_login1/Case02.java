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
		//URLにアクセス
		goTo("http://localhost:8080/lms");

		// ログイン画面のタイトルを確認
		assertEquals("ログイン | LMS", webDriver.getTitle());

		// ログインID欄が空欄であることを確認
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		assertEquals("", loginId.getAttribute("value"));

		// パスワード欄が空欄であることを確認
		WebElement password = webDriver.findElement(By.id("password"));
		assertEquals("", password.getAttribute("value"));

		// エビデンス取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {
		// ユーザーID入力
		webDriver.findElement(By.id("loginId")).sendKeys("testLogin");

		// パスワード入力
		webDriver.findElement(By.id("password")).sendKeys("testPassword");

		// ログインボタンを押下
		webDriver.findElement(By.className("btn-primary")).click();

		// ログイン失敗後、ログイン画面に戻ることを確認
		assertEquals("http://localhost:8080/lms/login", webDriver.getCurrentUrl());

		// ログイン失敗メッセージを確認
		WebElement errorMessage = webDriver.findElement(By.cssSelector("span.help-inline.error"));

		assertEquals("* ログインに失敗しました。", errorMessage.getText());

		// エビデンス取得
		getEvidence(new Object() {
		});
	}

}
