package it.unibo.mvc.view;

import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.api.DrawResult;
import static java.lang.System.out;

/**
 * Implementation of the consol user interface.
 */
public final class DrawNumberStandardOutputView implements DrawNumberView {

    private static final String NEW_GAME = ": a new game starts!";

    /**
     * Builds a new {@link DrawNumberStandardOutputView}.
     */
    public DrawNumberStandardOutputView() {
        /*
         * simple comment to don't have any style problem
         */
    }

    /**
     * Sets the controller controlled by this view (if works as input).
     *
     * @param observer the controller to attach
     */
    @Override
    public void setController(final DrawNumberController observer) { }

    /**
     * it is a simple log function.
     * 
     * @param s is the string to pass in
     */
    private void log(final String s) {
        out.println(s);
    }

    /**
     * This method is called before the UI is used. It should finalize its status (if needed).
     */
    @Override
    public void start() { }

    /**
     * Tells the UI to display the result of the draw.
     *
     * @param res the result of the last draw
     */
    @Override
    public void result(final DrawResult res) {
        switch (res) {
            case YOURS_HIGH, YOURS_LOW -> {
                log(res.getDescription());
            }
            case YOU_WON -> log(res.getDescription() + NEW_GAME);
            case YOU_LOST -> log("You lost");
        }
    }
}
