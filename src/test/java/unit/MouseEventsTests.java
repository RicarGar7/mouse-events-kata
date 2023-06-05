package unit;

import mouse.Coordinates;
import mouse.MouseSoftware;
import org.junit.Before;
import org.junit.Test;
import utils.MouseEventListenerSpy;

import java.util.Date;

public class MouseEventsTests {

    MouseSoftware mouse;
    MouseEventListenerSpy spy;

    @Before
    public void setup() {
        mouse = new MouseSoftware(new Coordinates(0,0));
        spy = new MouseEventListenerSpy();
        mouse.getStateSubject().subscribe(spy::handleMouseEvent);
    }

    @Test
    public void press_and_release_left_button_emit_single_click_event() {
        doPressAndRelease();

        assert (spy.isSingleClick());
    }

    @Test
    public void press_and_release_twice_left_button_fast_emit_double_click_event() {
        doDoublePressAndRelease();

        assert (spy.isDoubleClick());
    }

    @Test
    public void press_and_release_tree_simultaneous_times_left_button_fast_emit_triple_click_event() {
        doTriplePressAndRelease();

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

}

