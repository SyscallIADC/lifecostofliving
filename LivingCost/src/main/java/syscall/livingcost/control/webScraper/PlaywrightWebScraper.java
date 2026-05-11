package syscall.livingcost.control.webScraper;

import com.microsoft.playwright.*;

import java.util.Random;

public class PlaywrightWebScraper implements WebScraper, AutoCloseable{
    private static final String STANDARD_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private final Playwright playwright;
    private final Browser browser;
    private final Random randomGenerator;

    public PlaywrightWebScraper() {
        this.playwright = Playwright.create();
        this.browser = launchHeadlessBrowser();
        this.randomGenerator = new Random();
    }

    public String getHtmlContent(String url) {
        BrowserContext context = createAnonymousContext();
        Page page = context.newPage();

        try {
            navigateToUrl(page, url);
            simulateHumanReadingTime(page);
            return page.content();
        } finally {
            page.context().close();
            page.close();
        }
    }

    private Browser launchHeadlessBrowser() {
        return playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );
    }

    private BrowserContext createAnonymousContext() {
        return browser.newContext(
                new Browser.NewContextOptions()
                        .setUserAgent(STANDARD_USER_AGENT)
                        .setViewportSize(1920,1080)
        );
    }

    private void simulateHumanReadingTime(Page page) {
        int randomDelayInMilliseconds = 3000 + randomGenerator.nextInt(4000);
        page.waitForTimeout(randomDelayInMilliseconds);
    }

    private void navigateToUrl(Page page, String url) {
        Response response = page.navigate(url, new Page.NavigateOptions().setTimeout(10000));
        page.waitForLoadState();

        if (response != null) {
            int status = response.status();
            if (status == 403 || status == 429) {
                throw new IllegalStateException("HTTP " + status + " en: " + url);
            }
        }

        String title = page.title().toLowerCase();
        if (title.contains("just a moment") || title.contains("access denied") ||
                title.contains("attention required") || title.contains("security check")) {
            throw new IllegalStateException("Título de Anti-Bot detectado en: " + url);
        }

        String[] botSelectors = {
                "#cf-wrapper",
                "div#challenge-error-title",
                "#px-captcha",
                "div.geetest_captcha",
                "iframe[src*='datadome']",
                "div.g-recaptcha"
        };

        for (String selector : botSelectors) {
            if (page.locator(selector).count() > 0) {
                throw new IllegalStateException("Selector Anti-Bot (" + selector + ") detectado en: " + url);
            }
        }
    }

    @Override
    public void close() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
