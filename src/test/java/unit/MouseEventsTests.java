package unit;

import mouse.Coordinates;
import mouse.Mouse;
import org.junit.Before;
import org.junit.Test;
import utils.MouseEventListenerSpy;

import java.util.Date;

public class MouseEventsTests {

    Mouse mouse;
    MouseEventListenerSpy spy;

    @Before
    public void setup() {
        mouse = new Mouse();
        spy = new MouseEventListenerSpy();
        mouse.subscribe(spy);
    }

    @Test
    public void press_and_release_left_button_emit_single_click_event() {
        doClick();

        assert (spy.isSingleClick());
    }

    @Test
    public void press_and_release_twice_left_button_fast_emit_double_click_event() {
        doDoubleClick();

        assert (spy.isDoubleClick());
    }

    @Test
    public void press_and_release_tree_simultaneous_times_left_button_fast_emit_triple_click_event() {
        doTripleClick();

        assert (spy.isTripleClick());
    }

    @Test
    public void press_left_button_and_move_emit_drag_event() {
        mouse.pressLeftButton(new Date().getTime());
        mouse.move(new Coordinates(10, 0), new Date().getTime());

        assert (spy.isDrag());
    }

    @Test
    public void press_left_button_and_move_and_release_emit_drop_event() {
        mouse.pressLeftButton(new Date().getTime());
        mouse.move(new Coordinates(10, 0), new Date().getTime());
        mouse.releaseLeftButton(new Date().getTime());

        assert (spy.isDrop());
    }

    // Double click edge cases
    @Test
    public void press_and_release_left_button_once_does_not_emit_double_click_event() {
        doClick();

        assert (!spy.isDoubleClick());
    }

    @Test
    public void press_and_release_left_button_three_times_fast_does_not_emit_double_click_event() {
        doTripleClick();

        assert (!spy.isDoubleClick());
    }

    // Triple click edge cases
    @Test
    public void press_and_release_left_button_twice_does_not_emit_triple_click_event() {
        doDoubleClick();

        assert (!spy.isTripleClick());
    }

    @Test
    public void press_and_release_left_button_four_times_fast_does_not_emit_triple_click_event() {
        doTripleClick();
        doClick();

        assert (!spy.isTripleClick());
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
        mouse.pressLeftButton(new Date().getTime());
        mouse.move(new Coordinates(10, 0), new Date().getTime());

        assert (!spy.isDrop());
    }

    @Test
    public void press_left_button_and_release_without_move_does_not_emit_drop_event() {
        mouse.pressLeftButton(new Date().getTime());
        mouse.releaseLeftButton(new Date().getTime());

        assert (!spy.isDrop());
    }


    private void doClick() {
        mouse.pressLeftButton(new Date().getTime());
        mouse.releaseLeftButton(new Date().getTime());
    }

    private void doDoubleClick() {
        doClick();
        doClick();
    }

    private void doTripleClick() {
        doClick();
        doClick();
        doClick();
    }

}

