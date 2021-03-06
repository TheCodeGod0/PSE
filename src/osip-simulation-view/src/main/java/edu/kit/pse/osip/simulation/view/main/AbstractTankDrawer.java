package edu.kit.pse.osip.simulation.view.main;

import edu.kit.pse.osip.core.model.base.AbstractTank;
import edu.kit.pse.osip.core.model.base.TankSelector;
import edu.kit.pse.osip.core.utils.language.Translator;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class visualizes a tank holding a colored liquid. It knows its position as well as the color
 * of the content. The changing part of the visualization are the fill level of the tank and, possibly,
 * the color of the liquid.
 *
 * @version 1.0
 * @author Niko Wilhelm
 */
public abstract class AbstractTankDrawer extends ObjectDrawer {
    /**
     * The tank this Drawer draws.
     */
    private final AbstractTank tank;
    /**
     * Name of the tank.
     */
    private final String tankName;

    private double inBoxHorPadding;
    private double inBoxVertPadding;
    private double relInBoxWidth;
    private double relInBoxHeight;
    private double outBoxHorPadding;
    private double outBoxVertPadding;
    private double relOutBoxWidth;
    private double relOutBoxHeight;
    private double relOvalHeight;

    /**
     * Sets the position by using super(pos) and sets the tank.
     * 
     * @param pos The upper left corner of the tank.
     * @param tank The tank to get the attributes from.
     * @param rows The number of rows in which the Tanks are ordered.
     * @param cols The number of columns in which the Tanks are ordered.
     */
    public AbstractTankDrawer(Point2D pos, AbstractTank tank, int rows, int cols) {
        super(pos);
        this.tank = tank;
        this.tankName = Translator.getInstance().getString(
                TankSelector.TRANSLATOR_LABEL_PREFIX + tank.getTankSelector().name());

        relInBoxHeight = ViewConstants.INBOX_HEIGHT / rows;
        relInBoxWidth = ViewConstants.INBOX_WIDTH / cols;
        relOutBoxHeight = ViewConstants.OUTBOX_PERCENT / rows;
        relOutBoxWidth = ViewConstants.OUTBOX_PERCENT / cols;

        inBoxHorPadding = (1 - ViewConstants.INBOX_WIDTH) / 2 / cols;
        inBoxVertPadding = (1 - ViewConstants.INBOX_HEIGHT) / 2 / rows;
        outBoxHorPadding = (1 - ViewConstants.OUTBOX_PERCENT) / 2 / cols;
        outBoxVertPadding = (1 - ViewConstants.OUTBOX_PERCENT) / 2 / rows;

        relOvalHeight = ViewConstants.OVAL_PERCENT / rows;
    }

    /**
     * Contains the main calls necessary to draw the tank. Uses the abstract method drawSensors() for detail.
     * 
     * @param context The GraphicsContext on which the tank is drawn.
     * @param time Time since last call.
     */
    @Override
    public final void draw(GraphicsContext context, double time) {
        // get the outside values
        Canvas canvas = context.getCanvas();
        double totalWidth = canvas.getWidth();
        double totalHeight = canvas.getHeight();

        double fillLevel = tank.getFillLevel();
        Color fluidColor = getTankColor();

        // start the calculated values
        double outBoxXPos = (getPosition().getX() + outBoxHorPadding) * totalWidth;
        double outBoxYPos = (getPosition().getY() + outBoxVertPadding) * totalHeight;
        double outBoxWidth = relOutBoxWidth * totalWidth;
        double outBoxHeight = relOutBoxHeight * totalHeight;

        double inBoxXPos = (getPosition().getX() + inBoxHorPadding) * totalWidth;
        double inBoxYPos = (getPosition().getY() + inBoxVertPadding) * totalHeight;
        double inBoxWidth = relInBoxWidth * totalWidth;
        double inBoxHeight = relInBoxHeight * totalHeight;

        double totalOvalHeight = relOvalHeight * totalHeight;
        double topOvalYPos = inBoxYPos - (totalOvalHeight / 2);
        double botOvalYPos = inBoxYPos + inBoxHeight - (totalOvalHeight / 2);

        double fluidTopYPos = inBoxYPos + (1 - fillLevel) * inBoxHeight;
        double fluidHeight = inBoxHeight * fillLevel;

        double fluidTopOvalYPos = fluidTopYPos - totalOvalHeight / 2;

        context.setFill(Color.LIGHTGREY);
        context.setLineWidth(3);
        context.fillRect(outBoxXPos, outBoxYPos, outBoxWidth, outBoxHeight);

        context.setStroke(Color.BLACK);
        context.setFill(Color.WHITE);
        context.fillRect(inBoxXPos, inBoxYPos, inBoxWidth, inBoxHeight);
        context.strokeRect(inBoxXPos, inBoxYPos, inBoxWidth, inBoxHeight);

        context.fillOval(inBoxXPos, botOvalYPos, inBoxWidth, totalOvalHeight);
        context.strokeOval(inBoxXPos, botOvalYPos, inBoxWidth, totalOvalHeight);
        context.fillOval(inBoxXPos, topOvalYPos, inBoxWidth, totalOvalHeight);

        // Draw no fluid at all if the tank is empty
        // Drawn by setting the bottom oval, a rectangle on top and then the top oval
        if (fillLevel > 0) {
            context.setFill(fluidColor);
            context.fillOval(inBoxXPos, botOvalYPos, inBoxWidth, totalOvalHeight);
            context.fillRect(inBoxXPos, fluidTopYPos, inBoxWidth, fluidHeight);

            context.fillOval(inBoxXPos, fluidTopOvalYPos, inBoxWidth, totalOvalHeight);
            context.setLineWidth(1);
            context.strokeOval(inBoxXPos, fluidTopOvalYPos, inBoxWidth, totalOvalHeight);
        }

        context.setFill(Color.BLACK);
        double fontSize = totalWidth * ViewConstants.ABSTRACT_TANK_FONT_SIZE;
        context.setFont(Font.font("Arial", fontSize));
        double textYPos = (getPosition().getY() + outBoxVertPadding) * totalHeight + outBoxHeight;

        context.fillText(tankName, outBoxXPos + 2, textYPos - 3);

        context.setLineWidth(3);
        context.strokeOval(inBoxXPos, topOvalYPos, inBoxWidth, totalOvalHeight);
        drawSensors(context, time);
    }

    /**
     * Converts the tank's color to a JavaFX color instance.
     * 
     * @return the converted color instance.
     */
    private Color getTankColor() {
        edu.kit.pse.osip.core.model.base.Color tankColor = tank.getLiquid().getColor();
        double red = tankColor.getR();
        double green = tankColor.getG();
        double blue = tankColor.getB();
        return Color.color(red, green, blue);
    }

    /**
     * Used by draw(). Adds some detail to the tank depending on tank type.
     * 
     * @param context The GraphicsContext on which the sensors are drawn.
     * @param timeDiff The time passed in minutes since the last call of drawSensors.
     */
    protected abstract void drawSensors(GraphicsContext context, double timeDiff);

    /**
     * Gets the point where pipes can attach to the bottom of the tank. This point lies in the
     * middle of the tank horizontally and and the lowest point of the bottom oval vertically.
     * 
     * @return The Point2D sitting at the bottom middle of the tank.
     */
    public Point2D getPipeStartPoint() {
        double xPos = getPosition().getX() + inBoxHorPadding + relInBoxWidth / 2;
        double yPos = getPosition().getY() + inBoxVertPadding + relInBoxHeight + relOvalHeight / 2;
        return new Point2D(xPos, yPos);
    }

    /**
     * Gets the point number pointnum at the top of the tank, assuming pointCount points
     * are wanted. All points are evenly spaced.
     * 
     * @param pointNum The number of the point that is wanted.
     * @param pointCount The number of pipes that have to fit onto the Tank.
     * @return The Point2D that sits at the top of the tank at (pointNum/ (tankCount + 1)).
     */
    public Point2D getPipeEndPoint(int pointNum, int pointCount) {
        double xPos = getPosition().getX() + inBoxHorPadding
                + (relInBoxWidth * pointNum / (pointCount + 1));
        double yPos = getPosition().getY() + inBoxVertPadding;
        return new Point2D(xPos, yPos);
    }

    /**
     * Gets the Point2D where a new pipe, marking an input into the production, starts at a Tank.
     * This Point2D marks the top middle of the outer box.
     * 
     * @return The Point2D sitting at the top middle of the tank.
     */
    public Point2D getPipeTopEntry() {
        double xPos = getPosition().getX() + inBoxHorPadding + relInBoxWidth / 2;
        double yPos = getPosition().getY() + outBoxVertPadding;
        return new Point2D(xPos, yPos);
    }

    /**
     * Gets the Point2D where a pipe, marking an output of the production, leaves the simulation after
     * coming from the tank.
     * This Point2D marks the bottom middle of the outer box.
     * 
     * @return The Point2D sitting at the bottom middle of the tank compartment.
     */
    public Point2D getPipeBotExit() {
        double xPos = getPosition().getX() + inBoxHorPadding + relInBoxWidth / 2;
        double yPos = getPosition().getY() + outBoxVertPadding + relOutBoxHeight;
        return new Point2D(xPos, yPos);
    }
}
