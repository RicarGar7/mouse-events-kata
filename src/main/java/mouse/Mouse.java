package mouse;

import java.util.ArrayList;
import java.util.List;

public class Mouse {
    private static final long TIME_WINDOW_IN_MILLISECONDS_FOR_DOUBLE_CLICK = 500;
    private static final long NO_ACTION_TIME = -1;
    private final List<MouseEventListener> listeners = new ArrayList<>();
    private long lastActionButtonTime = NO_ACTION_TIME;
    private int simultaneousPressedTimes;
    private MouseInnerState state = MouseInnerState.Idle;

    public void pressLeftButton(long currentTimeInMilliseconds) {
        if (state == MouseInnerState.Idle) {
            state = MouseInnerState.Pressed;
            checkForSimultaneousLeftButtonPress(currentTimeInMilliseconds);
            lastActionButtonTime = currentTimeInMilliseconds;
        }
    }

    private void checkForSimultaneousLeftButtonPress(long currentTimeInMilliseconds) {
        if (lastActionButtonTime != -1) {
            if (isInDoubleClickTimeWindow(currentTimeInMilliseconds)) {
                simultaneousPressedTimes++;
            }
        }
    }

    public void releaseLeftButton(long currentTimeInMilliseconds) {
        if (state == MouseInnerState.Moving) {
            notifySubscribers(MouseEventType.Drop);
        } else if (state == MouseInnerState.Pressed) {
            handleButtonRelease(currentTimeInMilliseconds);
        }
    }

    private boolean isInDoubleClickTimeWindow(long currentTimeInMilliseconds) {
        return (currentTimeInMilliseconds - lastActionButtonTime) < TIME_WINDOW_IN_MILLISECONDS_FOR_DOUBLE_CLICK;
    }

    private void handleButtonRelease(long currentTimeInMilliseconds) {
        if (!isInDoubleClickTimeWindow(currentTimeInMilliseconds)) {
            simultaneousPressedTimes = 0;
        }
        if (isInDoubleClickTimeWindow(currentTimeInMilliseconds) && simultaneousPressedTimes > 0) {
            handleSimultaneousClicks();
        } else {
            notifySubscribers(MouseEventType.SingleClick);
        }
        state = MouseInnerState.Idle;
    }

    private void handleSimultaneousClicks() {
        if (simultaneousPressedTimes == 1) {
            notifySubscribers(MouseEventType.DoubleClick);
        }
        if (simultaneousPressedTimes == 2) {
            notifySubscribers(MouseEventType.TripleClick);
        }
    }

    public void move(Coordinates coordinates, long currentTimeInMilliseconds) {
        if (state == MouseInnerState.Pressed) {
            notifySubscribers(MouseEventType.Drag);
        }
        state = MouseInnerState.Moving;
    }

    public void subscribe(MouseEventListener listener) {
        listeners.add(listener);
    }

    private void notifySubscribers(MouseEventType eventType) {
        listeners.forEach(listener -> listener.handleMouseEvent(eventType));
    }
}
