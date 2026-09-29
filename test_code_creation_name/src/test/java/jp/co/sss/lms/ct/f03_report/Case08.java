package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

	/** 修正内容確認用文字列 */
	private static final String UPDATE_TEXT = "【Case08修正】";

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
		assertEquals(
				"ログイン | LMS",
				title,
				"ログイン画面が表示されること");

		assertEquals(
				"",
				loginIdElement.getAttribute("value"),
				"ログインID欄が空欄であること");

		assertEquals(
				"",
				passwordElement.getAttribute("value"),
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
		assertEquals(
				"コース詳細 | LMS",
				title,
				"コース詳細画面に遷移すること");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		// 2022年10月2日(日)の行を取得
		final WebElement submittedRow = webDriver.findElement(
				By.xpath("//tr[contains(.,'2022年10月2日(日)')]"));

		// 該当行の「詳細」ボタンを取得
		final WebElement detailButton = submittedRow.findElement(
				By.xpath(
						".//*[self::a or self::button or self::input]"
								+ "[normalize-space(text())='詳細' or @value='詳細']"));

		// クリックしやすい位置までスクロール
		((org.openqa.selenium.JavascriptExecutor) webDriver)
				.executeScript(
						"arguments[0].scrollIntoView({block:'center'});",
						detailButton);

		// 「詳細」ボタンを押下
		detailButton.click();

		// セクション詳細画面が表示されるまで待機
		visibilityTimeout(By.tagName("h2"), 5);

		// タイトル取得
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
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		// 「提出済み週報【デモ】を確認する」ボタンを取得
		final WebElement confirmButton = webDriver.findElement(
				By.xpath(
						"//*[self::a or self::button or self::input]"
								+ "[normalize-space(text())='提出済み週報【デモ】を確認する' "
								+ "or @value='提出済み週報【デモ】を確認する']"));

		// クリックしやすい位置までスクロール
		((org.openqa.selenium.JavascriptExecutor) webDriver)
				.executeScript(
						"arguments[0].scrollIntoView({block:'center'});",
						confirmButton);

		// ボタンを押下
		confirmButton.click();

		// レポート登録画面が表示されるまで待機
		visibilityTimeout(By.tagName("h2"), 5);

		// タイトル取得
		final String title = webDriver.getTitle();

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 期待値確認
		assertEquals(
				"レポート登録 | LMS",
				title,
				"レポート登録画面に遷移すること");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {

		// テキストエリアを取得
		final List<WebElement> textareas = webDriver.findElements(By.tagName("textarea"));

		// 週報画面には
		// 0:目標の達成度
		// 1:所感
		// 2:一週間の振り返り
		// が表示されていることを確認
		assertTrue(
				textareas.size() >= 3,
				"週報の入力欄が表示されていること");

		// 「所感」に修正確認用の文字列を追記
		final WebElement impressionElement = textareas.get(1);
		impressionElement.sendKeys(UPDATE_TEXT);

		// 修正後のエビデンス取得
		getEvidence(new Object() {
		});

		// 「提出する」ボタンを取得
		final WebElement submitButton = webDriver.findElement(
				By.xpath(
						"//*[self::button or self::input]"
								+ "[normalize-space(text())='提出する' "
								+ "or @value='提出する']"));

		// 「提出する」ボタンがクリックしやすい位置までスクロール
		((org.openqa.selenium.JavascriptExecutor) webDriver)
				.executeScript(
						"arguments[0].scrollIntoView({block:'center'});",
						submitButton);

		// 「提出する」ボタンを押下
		submitButton.click();

		// セクション詳細画面が表示されるまで待機
		visibilityTimeout(By.tagName("h2"), 5);

		// タイトル取得
		final String title = webDriver.getTitle();

		// 期待値確認
		assertEquals(
				"セクション詳細 | LMS",
				title,
				"提出後にセクション詳細画面へ遷移すること");

		// 提出後のエビデンス取得
		getEvidence(new Object() {
		}, "after");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {

		// 「ようこそ○○さん」リンクを取得
		final WebElement userLink = webDriver.findElement(
				By.xpath("//a[contains(.,'ようこそ')]"));

		// リンクを押下
		userLink.click();

		// ユーザー詳細画面が表示されるまで待機
		visibilityTimeout(By.tagName("h2"), 5);

		// タイトル取得
		final String title = webDriver.getTitle();

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 期待値確認
		assertTrue(
				title.contains("ユーザー"),
				"ユーザー詳細画面に遷移すること");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {

		// 週報を含む行を取得
		final WebElement weeklyReportRow = webDriver.findElement(
				By.xpath("//tr[contains(.,'週報')]"));

		// 該当行の「詳細」ボタンを取得
		final WebElement detailButton = weeklyReportRow.findElement(
				By.xpath(
						".//*[self::a or self::button or self::input]"
								+ "[normalize-space(text())='詳細' or @value='詳細']"));

		// クリックしやすい位置までスクロール
		((org.openqa.selenium.JavascriptExecutor) webDriver)
				.executeScript(
						"arguments[0].scrollIntoView({block:'center'});",
						detailButton);

		// 「詳細」ボタンを押下
		detailButton.click();

		// レポート詳細画面が表示されるまで待機
		visibilityTimeout(By.tagName("h2"), 5);

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 修正内容が反映されていることを確認
		assertTrue(
				webDriver.getPageSource().contains(UPDATE_TEXT),
				"修正した報告内容がレポート詳細画面に反映されること");
	}
}
