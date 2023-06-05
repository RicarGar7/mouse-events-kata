package mouse;


import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class MouseHardware {
    private BehaviorSubject<MouseHardwareState> subject = BehaviorSubject.createDefault(MouseHardwareState.Idle);
    private final Coordinates initialPosition;

    public MouseHardware(Coordinates initialPosition) {
        this.initialPosition = initialPosition;
    }

    public void pressLeftButton() {
        if (subject.getValue() == MouseHardwareState.Idle) {
            subject.onNext(MouseHardwareState.Pressed);
        }
    }

    public void releaseLeftButton() {
        if (subject.getValue() != MouseHardwareState.Idle) {
            subject.onNext(MouseHardwareState.Idle);
        }
    }

    public void move(Coordinates coordinates) {
        if (subject.getValue() == MouseHardwareState.Pressed) {
            if (!coordinates.equals(initialPosition)) {
                subject.onNext(MouseHardwareState.Moving);
            }
        }
        if (subject.getValue() == MouseHardwareState.Moving) {
            if (coordinates.equals(initialPosition)) {
                subject.onNext(MouseHardwareState.Pressed);
            }
        }
    }

    public Subject<MouseHardwareState> getStateSubject() {
        return subject;
    }
}

