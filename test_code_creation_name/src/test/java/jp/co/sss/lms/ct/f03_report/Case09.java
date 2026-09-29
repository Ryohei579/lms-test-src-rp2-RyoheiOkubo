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
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

	/** 正常値 */
	private static final String LEARNING_ITEM = "ITリテラシー①";
	private static final String GOAL = "5";

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
		webDriver.findElement(By.id("loginId"))
				.sendKeys("StudentAA01");

		webDriver.findElement(By.id("password"))
				.sendKeys("Sidhki658");

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
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {

		// 「ようこそ○○さん」リンクを取得
		final WebElement userLink = webDriver.findElement(
				By.xpath("//a[contains(.,'ようこそ')]"));

		// リンクを押下
		userLink.click();

		// ユーザー詳細画面が表示されるまで待機
		visibilityTimeout(
				By.xpath("//h2[contains(.,'ユーザー詳細')]"),
				5);

		// 見出しを取得
		final WebElement heading = webDriver.findElement(
				By.xpath("//h2[contains(.,'ユーザー詳細')]"));

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 期待値確認
		assertTrue(
				heading.getText().contains("ユーザー詳細"),
				"ユーザー詳細画面に遷移すること");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		// 「2022年10月2日(日)」「週報【デモ】」を含む行を取得
		final WebElement weeklyReportRow = webDriver.findElement(
				By.xpath(
						"//tr[contains(.,'2022年10月2日(日)') "
								+ "and contains(.,'週報【デモ】')]"));

		// 該当行の「修正する」ボタンを取得
		final WebElement updateButton = weeklyReportRow.findElement(
				By.xpath(
						".//*[self::a or self::button or self::input]"
								+ "[normalize-space(text())='修正する' "
								+ "or @value='修正する']"));

		// クリックしやすい位置までスクロール
		((org.openqa.selenium.JavascriptExecutor) webDriver)
				.executeScript(
						"arguments[0].scrollIntoView({block:'center'});",
						updateButton);

		// 「修正する」ボタンを押下
		updateButton.click();

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
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {

		// 学習項目を取得
		final WebElement learningItem = webDriver.findElement(By.cssSelector("input[type='text']"));

		// 学習項目を未入力にする
		learningItem.clear();

		// 「提出する」ボタンを押下
		clickSubmitButton();

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 入力エラー確認
		assertInputError(
				"学習項目が未入力の場合にエラーが表示されること");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {

		// 学習項目を正常値へ戻す
		final WebElement learningItem = webDriver.findElement(By.cssSelector("input[type='text']"));

		learningItem.clear();
		learningItem.sendKeys(LEARNING_ITEM);

		// 理解度を取得
		final Select understanding = new Select(
				webDriver.findElement(By.tagName("select")));

		// 理解度を未選択にする
		understanding.selectByIndex(0);

		// 「提出する」ボタンを押下
		clickSubmitButton();

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 入力エラー確認
		assertInputError1(
				"理解度が未入力の場合にエラーが表示されること");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {

		// 理解度を正常値「2」に戻す
		final Select understanding = new Select(
				webDriver.findElement(By.tagName("select")));

		understanding.selectByVisibleText("2");

		// テキストエリアを取得
		final List<WebElement> textareas = webDriver.findElements(By.tagName("textarea"));

		// 0番目 = 目標の達成度
		final WebElement goalElement = textareas.get(0);

		// 数値以外を入力
		goalElement.clear();
		goalElement.sendKeys("abc");

		// 「提出する」ボタンを押下
		clickSubmitButton();

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 入力エラー確認
		assertInputError1(
				"目標の達成度が数値以外の場合にエラーが表示されること");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {

		// テキストエリアを取得
		final List<WebElement> textareas = webDriver.findElements(By.tagName("textarea"));

		// 0番目 = 目標の達成度
		final WebElement goalElement = textareas.get(0);

		// 1～10の範囲外「11」を入力
		goalElement.clear();
		goalElement.sendKeys("11");

		// 「提出する」ボタンを押下
		clickSubmitButton();

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 入力エラー確認
		assertInputError1(
				"目標の達成度が1～10の範囲外の場合にエラーが表示されること");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {

		// テキストエリアを取得
		final List<WebElement> textareas = webDriver.findElements(By.tagName("textarea"));

		// 0番目 = 目標の達成度
		final WebElement goalElement = textareas.get(0);

		// 1番目 = 所感
		final WebElement impressionElement = textareas.get(1);

		// 目標の達成度を未入力
		goalElement.clear();

		// 所感を未入力
		impressionElement.clear();

		// 「提出する」ボタンを押下
		clickSubmitButton();

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 入力エラー確認
		assertInputError(
				"目標の達成度・所感が未入力の場合にエラーが表示されること");
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {

		// テキストエリアを取得
		final List<WebElement> textareas = webDriver.findElements(By.tagName("textarea"));

		// 0番目 = 目標の達成度
		final WebElement goalElement = textareas.get(0);

		// 1番目 = 所感
		final WebElement impressionElement = textareas.get(1);

		// 2番目 = 一週間の振り返り
		final WebElement weeklyReviewElement = textareas.get(2);

		// 目標の達成度を正常値へ戻す
		goalElement.clear();
		goalElement.sendKeys(GOAL);

		// 2001文字を作成
		final String over2000 = "あ".repeat(2001);

		// 所感を2000文字超にする
		impressionElement.clear();
		impressionElement.sendKeys(over2000);

		// 一週間の振り返りを2000文字超にする
		weeklyReviewElement.clear();
		weeklyReviewElement.sendKeys(over2000);

		// 「提出する」ボタンを押下
		clickSubmitButton();

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 入力エラー確認
		assertInputError1(
				"所感・一週間の振り返りが2000文字を超えた場合にエラーが表示されること");
	}

	/**
	 * 「提出する」ボタンを押下
	 */
	private void clickSubmitButton() {

		// 「提出する」ボタンを取得
		final WebElement submitButton = webDriver.findElement(
				By.xpath(
						"//*[self::button or self::input]"
								+ "[normalize-space(text())='提出する' "
								+ "or @value='提出する']"));

		// ボタンがクリックしやすい位置までスクロール
		((org.openqa.selenium.JavascriptExecutor) webDriver)
				.executeScript(
						"arguments[0].scrollIntoView({block:'center'});",
						submitButton);

		// 「提出する」ボタンを押下
		submitButton.click();
	}

	/**
	 * サーバー側の入力エラーが表示されたことを確認
	 */
	private void assertInputError1(String message) {

		// 入力エラーなのでレポート登録画面に残ることを確認
		assertEquals(
				"レポート登録 | LMS",
				webDriver.getTitle(),
				message);

		// 入力エラー表示を取得
		final List<WebElement> errors = webDriver.findElements(
				By.cssSelector(
						".help-inline, "
								+ ".help-block, "
								+ ".text-danger, "
								+ ".error, "
								+ "[class*='error']"));

		// 表示されているエラーメッセージが存在することを確認
		assertTrue(
				errors.stream().anyMatch(
						element -> element.isDisplayed()
								&& !element.getText().trim().isEmpty()),
				message);
	}

	/**
	 * 入力エラーが表示されたことを確認
	 */
	private void assertInputError(String message) {

		// 入力エラーなのでレポート登録画面に残ることを確認
		assertEquals(
				"レポート登録 | LMS",
				webDriver.getTitle(),
				message);

		// エラー状態になっている要素を取得
		final List<WebElement> errors = webDriver.findElements(
				By.cssSelector(
						".errorInput, "
								+ ".help-inline, "
								+ ".help-block, "
								+ ".text-danger, "
								+ ".error"));

		// エラー状態の要素が1つ以上存在することを確認
		assertFalse(
				errors.isEmpty(),
				message);
	}

}
