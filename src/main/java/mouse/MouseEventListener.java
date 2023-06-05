package mouse;

public interface MouseEventListener {
    void handleMouseEvent(MouseEventType eventType);

    void handleError(Throwable error);

    void handleCompletion();
}