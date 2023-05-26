package mouse;

import java.util.ArrayList;
import java.util.List;

public class Mouse {
    private static final long TIME_WINDOW_IN_MILLISECONDS_FOR_DOUBLE_CLICK = 500;
    private static final long NO_PRESS_TIME = -1;
    private static final int NO_SIMULTANEOUS_CLICKS = 0;
    private final List<MouseEventListener> listeners = new ArrayList<>();
    private final Coordinates initialPosition;
    private long lastPressTime = NO_PRESS_TIME;
    private int simultaneousPressedTimes = NO_SIMULTANEOUS_CLICKS;
    private MouseInnerState state = MouseInnerState.Idle;

    public Mouse(Coordinates initialPosition) {
        this.initialPosition = initialPosition;
    }

    public void pressLeftButton(long currentTimeInMilliseconds) {
        if (state == MouseInnerState.Idle) {
            state = MouseInnerState.Pressed;
            checkForSimultaneousLeftButtonPress(currentTimeInMilliseconds);
            lastPressTime = currentTimeInMilliseconds;
        }
    }

    public void releaseLeftButton(long currentTimeInMilliseconds) {
        if (isMoving()) {
            notifySubscribers(MouseEventType.Drop);
        }
        if (isPressed()) {
            handleButtonRelease(currentTimeInMilliseconds);
        }
        state = MouseInnerState.Idle;
    }

    public void move(Coordinates coordinates, long currentTimeInMilliseconds) {
        resetMultiplePressCounters();

        if (isPressed()) {
            if (!coordinates.equals(initialPosition)) {
                notifySubscribers(MouseEventType.Drag);
                state = MouseInnerState.Moving;
            }
        }
        if (isMoving()){
            if (coordinates.equals(initialPosition)) {
                notifySubscribers(null);
                state = MouseInnerState.Pressed;
            }
        }
    }

    private boolean isInDoubleClickTimeWindow(long currentTimeInMilliseconds) {
        return (currentTimeInMilliseconds - lastPressTime) < TIME_WINDOW_IN_MILLISECONDS_FOR_DOUBLE_CLICK;
    }

    private void handleButtonRelease(long currentTimeInMilliseconds) {
        if (!isInDoubleClickTimeWindow(currentTimeInMilliseconds)) {
            simultaneousPressedTimes = NO_SIMULTANEOUS_CLICKS;
        }
        if (isInDoubleClickTimeWindow(currentTimeInMilliseconds) && simultaneousPressedTimes > NO_SIMULTANEOUS_CLICKS) {
            handleSimultaneousClicks();
        } else {
            notifySubscribers(MouseEventType.SingleClick);
        }
    }

    private void resetMultiplePressCounters() {
        simultaneousPressedTimes = NO_SIMULTANEOUS_CLICKS;
        lastPressTime = NO_PRESS_TIME;
    }

    private boolean isMoving() {
        return state == MouseInnerState.Moving;
    }

    private boolean isPressed() {
        return state == MouseInnerState.Pressed;
    }

    private void checkForSimultaneousLeftButtonPress(long currentTimeInMilliseconds) {
        if (lastPressTime != -1) {
            if (isInDoubleClickTimeWindow(currentTimeInMilliseconds)) {
                simultaneousPressedTimes++;
            }
        }
    }

    private void handleSimultaneousClicks() {
        if (simultaneousPressedTimes == 1) {
            notifySubscribers(MouseEventType.DoubleClick);
        }
        if (simultaneousPressedTimes >= 2) {
            notifySubscribers(MouseEventType.TripleClick);
        }
    }

    public void subscribe(MouseEventListener listener) {
        listeners.add(listener);
    }

    private void notifySubscribers(MouseEventType eventType) {
        listeners.forEach(listener -> listener.handleMouseEvent(eventType));
    }
}
