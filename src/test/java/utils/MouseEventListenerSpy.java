package utils;

import mouse.MouseEventListener;
import mouse.MouseEventType;

public class MouseEventListenerSpy implements MouseEventListener {
    public MouseEventType eventReceived;

    @Override
    public void handleMouseEvent(MouseEventType eventType) {
        eventReceived = eventType;
    }

    public boolean isSingleClick() {
        return this.eventReceived == MouseEventType.SingleClick;
    }

    public boolean isDoubleClick() {
        return this.eventReceived == MouseEventType.DoubleClick;
    }

    public boolean isTripleClick() {
        return this.eventReceived == MouseEventType.TripleClick;
    }

    public boolean isDrag() {
        return this.eventReceived == MouseEventType.Drag;
    }

    public boolean isDrop() {
        return this.eventReceived == MouseEventType.Drop;
    }
}
