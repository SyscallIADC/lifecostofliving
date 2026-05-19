package org.syscall.business.control.network;

import java.util.List;

public interface Subscriber {
    void start(List<String> topicNames);
    void stop();
}
