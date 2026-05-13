package org.syscall.control.subscriber;

import java.util.List;

public interface Subscriber {
    void start(List<String> topics);
    void stop();
}
