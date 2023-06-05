package mouse;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class MouseSoftware {
    private final MouseHardware hardware;
    private BehaviorSubject<MouseEventType> subject = BehaviorSubject.create();
    private static final long TIME_WINDOW_IN_MILLISECONDS_FOR_DOUBLE_CLICK = 500;
    private static final long NO_TIME = -1;
    private static final int NO_SIMULTANEOUS_CLICKS = 0;
    private long lastPressTime = NO_TIME;
    private int simultaneousPressedTimes = NO_SIMULTANEOUS_CLICKS;

    public MouseSoftware(Coordinates initialPosition) {
        this.hardware = new MouseHardware(initialPosition);
        hardware.getStateSubject().subscribe(this::handleStateChange);
    }

    public void pressLeftButton(long currentTimeInMilliseconds) {
        lastPressTime = currentTimeInMilliseconds;
        hardware.pressLeftButton();
    }

    public void releaseLeftButton(long currentTimeInMilliseconds) {
        hardware.releaseLeftButton();
    }

    public void move(Coordinates coordinates, long currentTimeInMilliseconds) {
        hardware.move(coordinates);
    }

    private void handleStateChange(MouseHardwareState state) {
        switch (state) {
            case Moving:
                resetMultiplePressCounters();
                subject.onNext(MouseEventType.Drag);
                break;
            case Pressed:
                if (!(subject.getValue() == MouseEventType.Drag)) {
                    checkForSimultaneousLeftButtonPress(lastPressTime);
                }
                break;
            case Idle:
                if (subject.getValue() == MouseEventType.Drag) {
                    subject.onNext(MouseEventType.Drop);
                } else {
                    handleButtonRelease(lastPressTime);
                }
                break;
        }
    }

    public Subject<MouseEventType> getStateSubject() {
        return subject;
    }

    private void handleButtonRelease(long currentTimeInMilliseconds) {
        if (lastPressTime != NO_TIME){
            if (!isInDoubleClickTimeWindow(currentTimeInMilliseconds)) {
                simultaneousPressedTimes = NO_SIMULTANEOUS_CLICKS;
            }
            if (isInDoubleClickTimeWindow(currentTimeInMilliseconds) && simultaneousPressedTimes > 1) {
                handleSimultaneousClicks();
            } else {
                subject.onNext(MouseEventType.SingleClick);
            }
        }
    }

    private void handleSimultaneousClicks() {
        if (simultaneousPressedTimes == 2) {
            subject.onNext(MouseEventType.DoubleClick);
        }
        if (simultaneousPressedTimes >= 3) {
            subject.onNext(MouseEventType.TripleClick);
        }
    }

    private void resetMultiplePressCounters() {
        simultaneousPressedTimes = NO_SIMULTANEOUS_CLICKS;
        lastPressTime = NO_TIME;
    }

    private boolean isInDoubleClickTimeWindow(long currentTimeInMilliseconds) {
        return (currentTimeInMilliseconds - lastPressTime) < TIME_WINDOW_IN_MILLISECONDS_FOR_DOUBLE_CLICK;
    }

    private void checkForSimultaneousLeftButtonPress(long currentTimeInMilliseconds) {
        if (lastPressTime != NO_TIME) {
            if (isInDoubleClickTimeWindow(currentTimeInMilliseconds)) {
                simultaneousPressedTimes++;
            }
        }
    }
}
