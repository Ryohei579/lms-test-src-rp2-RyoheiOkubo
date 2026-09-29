package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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

		// トップページURLにアクセス
		goTo("http://localhost:8080/lms/");

		// タイトルを取得
		final String title = webDriver.getTitle();

		// ログインID欄・パスワード欄を取得
		final WebElement loginIdElement = webDriver.findElement(By.id("loginId"));
		final WebElement passwordElement = webDriver.findElement(By.id("password"));

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 期待値確認
		assertEquals("ログイン | LMS", title,
				"タイトルが期待値通りであること");
		assertEquals("", loginIdElement.getAttribute("value"),
				"ログインID欄が空欄であること");
		assertEquals("", passwordElement.getAttribute("value"),
				"パスワード欄が空欄であること");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {

		// ログイン情報を入力
		final WebElement loginIdElement = webDriver.findElement(By.id("loginId"));
		final WebElement passwordElement = webDriver.findElement(By.id("password"));

		loginIdElement.sendKeys("StudentAA01");
		passwordElement.sendKeys("Sidhki658");

		// ログインボタンを押下
		webDriver.findElement(
				By.cssSelector("input[type='submit'][value='ログイン']")).click();

		// コース詳細画面が表示されるまで待機
		visibilityTimeout(By.id("open-all-panel"), 5);

		// タイトル取得
		final String title = webDriver.getTitle();

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 期待値確認
		assertEquals("コース詳細 | LMS", title,
				"コース詳細画面に遷移すること");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		// 「未提出」を含む行を取得
		final WebElement unsubmittedRow = webDriver.findElement(
				By.xpath("//tr[contains(.,'未提出')]"));

		// その行の「詳細」ボタンを取得
		final WebElement detailButton = unsubmittedRow.findElement(
				By.xpath(
						".//*[self::a or self::button or self::input]"
								+ "[normalize-space(text())='詳細' or @value='詳細']"));

		// 「詳細」ボタンがクリックしやすい位置までスクロール
		((org.openqa.selenium.JavascriptExecutor) webDriver)
				.executeScript(
						"arguments[0].scrollIntoView({block:'center'});",
						detailButton);

		// 「詳細」ボタンを押下
		detailButton.click();

		// セクション詳細画面が表示されるまで待機
		visibilityTimeout(By.tagName("h2"), 5);

		// タイトルを取得
		final String title = webDriver.getTitle();

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 期待値確認
		assertEquals(
				"セクション詳細 | LMS",
				title,
				"セクション詳細画面に遷移すること");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		// 「日報【デモ】を提出する」ボタンが表示されるまで待機
		visibilityTimeout(
				By.xpath(
						"//*[self::a or self::button or self::input]"
								+ "[normalize-space(text())='日報【デモ】を提出する' "
								+ "or @value='日報【デモ】を提出する']"),
				5);

		// 「日報【デモ】を提出する」ボタンを取得
		final WebElement reportButton = webDriver.findElement(
				By.xpath(
						"//*[self::a or self::button or self::input]"
								+ "[normalize-space(text())='日報【デモ】を提出する' "
								+ "or @value='日報【デモ】を提出する']"));

		// ボタンを押下
		reportButton.click();

		// 遷移先画面が表示されるまで待機
		visibilityTimeout(By.tagName("h2"), 5);

		// タイトルを取得
		final String title = webDriver.getTitle();

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 期待値確認
		assertTrue(
				title.contains("レポート"),
				"レポート登録画面に遷移すること");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {

		// 表示されているテキスト入力欄へ入力
		final List<WebElement> textInputs = webDriver.findElements(
				By.cssSelector("input[type='text']"));

		for (WebElement element : textInputs) {
			if (element.isDisplayed()
					&& element.isEnabled()
					&& element.getAttribute("value").isEmpty()) {

				element.sendKeys("テスト");
			}
		}

		// 表示されている数値入力欄へ入力
		final List<WebElement> numberInputs = webDriver.findElements(
				By.cssSelector("input[type='number']"));

		for (WebElement element : numberInputs) {
			if (element.isDisplayed()
					&& element.isEnabled()
					&& element.getAttribute("value").isEmpty()) {

				element.sendKeys("5");
			}
		}

		// 表示されているテキストエリアへ入力
		final List<WebElement> textareas = webDriver.findElements(By.tagName("textarea"));

		for (WebElement element : textareas) {
			if (element.isDisplayed()
					&& element.isEnabled()
					&& element.getAttribute("value").isEmpty()) {

				element.sendKeys("テスト報告内容");
			}
		}

		// セレクトボックスがある場合は先頭以外を選択
		final List<WebElement> selects = webDriver.findElements(By.tagName("select"));

		for (WebElement element : selects) {
			if (element.isDisplayed() && element.isEnabled()) {

				final Select select = new Select(element);

				if (select.getOptions().size() > 1) {
					select.selectByIndex(1);
				}
			}
		}

		// ラジオボタンがある場合は各グループの先頭を選択
		final List<WebElement> radioButtons = webDriver.findElements(
				By.cssSelector("input[type='radio']"));

		final Set<String> selectedNames = new HashSet<>();

		for (WebElement radio : radioButtons) {

			final String name = radio.getAttribute("name");

			if (radio.isDisplayed()
					&& radio.isEnabled()
					&& !selectedNames.contains(name)) {

				radio.click();
				selectedNames.add(name);
			}
		}

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 「提出する」ボタンを押下
		webDriver.findElement(
				By.xpath(
						"//*[self::button or self::input]"
								+ "[normalize-space(text())='提出する' "
								+ "or @value='提出する']"))
				.click();

		// セクション詳細画面が表示されるまで待機
		visibilityTimeout(By.tagName("h2"), 5);

		// タイトルを取得
		final String title = webDriver.getTitle();

		// セクション詳細画面へ戻ったことを確認
		assertEquals(
				"セクション詳細 | LMS",
				title,
				"提出後にセクション詳細画面へ遷移すること");

		// 「日報を提出する」ボタンがなくなっていることを確認
		final List<WebElement> beforeSubmitButtons = webDriver.findElements(
				By.xpath(
						"//*[self::a or self::button or self::input]"
								+ "[contains(normalize-space(.),'日報を提出') "
								+ "or contains(@value,'日報を提出')]"));

		assertTrue(
				beforeSubmitButtons.isEmpty(),
				"日報提出後にボタン名が更新されること");

		// 提出後のエビデンス取得
		getEvidence(new Object() {
		}, "after");
	}
}