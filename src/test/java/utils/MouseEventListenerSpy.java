package utils;

import mouse.MouseEventListener;
import mouse.MouseEventType;

import java.util.ArrayList;
import java.util.List;

public class MouseEventListenerSpy implements MouseEventListener {
    public List<MouseEventType> eventReceived = new ArrayList<>();

    @Override
    public void handleMouseEvent(MouseEventType eventType) {
        eventReceived.add(eventType);
    }

    @Override
    public void handleError(Throwable error) {
        // Handle the error
    }

    @Override
    public void handleCompletion() {
        eventReceived = new ArrayList<>();
    }

    public boolean isSingleClick() {
        return getLastEvent() == MouseEventType.SingleClick;
    }

    private MouseEventType getLastEvent() {
        if (this.eventReceived.isEmpty())
            return null;
        return this.eventReceived.get(this.eventReceived.size() - 1);
    }

    public boolean isDoubleClick() {
        return getLastEvent() == MouseEventType.DoubleClick;
    }

    public boolean isTripleClick() {
        return getLastEvent() == MouseEventType.TripleClick;
    }

    public boolean isDrag() {
        return getLastEvent() == MouseEventType.Drag;
    }

    public boolean isDrop() {
        return getLastEvent() == MouseEventType.Drop;
    }
}
