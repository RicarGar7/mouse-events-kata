package unit;

import mouse.Coordinates;
import mouse.Mouse;
import mouse.MouseEventType;
import org.junit.Before;
import org.junit.Test;
import utils.MouseEventListenerSpy;

import java.util.Date;

public class MouseEventEgdeCasesTests {

    Mouse mouse;
    MouseEventListenerSpy spy;

    Coordinates initialPosition = new Coordinates(0, 0);

    @Before
    public void setup() {
        mouse = new Mouse(initialPosition);
        spy = new MouseEventListenerSpy();
        mouse.subscribe(spy);
    }

    // Single click edge cases
    @Test
    public void multiple_press_and_release_left_button_does_not_emit_single_click_event() {
        mouse.pressLeftButton(new Date().getTime());
        mouse.pressLeftButton(new Date().getTime());
        mouse.pressLeftButton(new Date().getTime());
        mouse.releaseLeftButton(new Date().getTime());

        assert (spy.isSingleClick());
    }

    // Double click edge cases
    @Test
    public void press_and_release_left_button_once_does_not_emit_double_click_event() {
        doPressAndRelease();

        assert (!spy.isDoubleClick());
    }

    @Test
    public void press_and_release_left_button_three_times_fast_does_not_emit_double_click_event() {
        doTriplePressAndRelease();

        assert (!spy.isDoubleClick());
    }

    // Triple click edge cases
    @Test
    public void press_and_release_left_button_twice_does_not_emit_triple_click_event() {
        doDoublePressAndRelease();

        assert (!spy.isTripleClick());
    }

    @Test
    public void press_and_release_left_button_four_times_fast_emit_triple_click_event() {
        doTriplePressAndRelease();
        doPressAndRelease();

        assert (spy.isTripleClick());
    }

    // Drag edge cases
    @Test
    public void press_left_button_and_not_move_does_not_emit_drag_event() {
        mouse.pressLeftButton(new Date().getTime());

        assert (!spy.isDrag());
    }

    @Test
    public void move_without_pressing_left_button_does_not_emit_drag_event() {
        mouse.move(new Coordinates(10, 0), new Date().getTime());

        assert (!spy.isDrag());
    }

    // Drop edge cases
    @Test
    public void press_left_button_and_move_and_not_release_does_not_emit_drop_event() {
        doPressAndMove();

        assert (!spy.isDrop());
    }

    @Test
    public void press_left_button_moving_and_return_to_initial_place_does_not_emit_drag_event() {
        doPressAndMove();

        assert (spy.isDrag());

        mouse.move(new Coordinates(0, 0), new Date().getTime());

        assert (!spy.isDrag());
        assert (!spy.isSingleClick());
    }

    @Test
    public void press_left_button_moving_and_release_and_press_and_release_again_does_not_emit_double_click_event() {
        doPressMoveAndRelease();
        doPressAndRelease();

        assert (!spy.isDoubleClick());
        assert (!spy.isDrop());
        assert (spy.isSingleClick());
    }

    @Test
    public void press_left_button_moving_and_return_to_initial_place_and_doing_press_and_release_does_not_emit_double_click_event() {
        doPressAndMove();

        assert (spy.isDrag());

        mouse.move(initialPosition, new Date().getTime());

        assert (!spy.isDrag());

        mouse.releaseLeftButton(new Date().getTime());

        assert (!spy.isDrop());

        doPressAndRelease();

        assert (spy.eventReceived == MouseEventType.SingleClick);
        assert (!spy.isDoubleClick());
        assert (spy.isSingleClick());
    }

    @Test
    public void press_left_button_and_release_without_move_does_not_emit_drop_event() {
        mouse.pressLeftButton(new Date().getTime());
        mouse.releaseLeftButton(new Date().getTime());

        assert (!spy.isDrop());
    }

    private void doPressAndRelease() {
        mouse.pressLeftButton(new Date().getTime());
        mouse.releaseLeftButton(new Date().getTime());
    }

    private void doDoublePressAndRelease() {
        doPressAndRelease();
        doPressAndRelease();
    }

    private void doTriplePressAndRelease() {
        doPressAndRelease();
        doPressAndRelease();
        doPressAndRelease();
    }

    private void doPressAndMove() {
        mouse.pressLeftButton(new Date().getTime());
        mouse.move(new Coordinates(10, 0), new Date().getTime());
    }


    private void doPressMoveAndRelease() {
        mouse.pressLeftButton(new Date().getTime());
        mouse.move(new Coordinates(10, 0), new Date().getTime());
        mouse.releaseLeftButton(new Date().getTime());
    }

}

