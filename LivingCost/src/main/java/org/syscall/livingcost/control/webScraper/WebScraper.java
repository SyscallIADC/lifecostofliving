package org.syscall.livingcost.control.webScraper;

public interface WebScraper extends AutoCloseable{
    String getHtmlContent(String url);

    @Override
    void close();
}
