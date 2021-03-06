package edu.kit.pse.osip.simulation.view.main;

import edu.kit.pse.osip.core.model.base.Pipe;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This class visualizes a pipe connecting two tanks. It is specified by the wayPoints through
 * which the pipe leads. It needs at least two wayPoints to exist.
 *
 * @version 1.0
 * @author Niko Wilhelm
 */
public class PipeDrawer implements Drawer {
    private Point2D[] wayPoints;
    private ValveDrawer valve;

    /**
     * Creates a new pipe along the wayPoints 1 to n.
     * 
     * @param wayPoints The points that the pipe goes along.
     * @param pipe The pipe that is drawn.
     * @param rows The number of rows in which the Tanks are ordered.
     */
    public PipeDrawer(Point2D[] wayPoints, Pipe pipe, int rows) {
        // There need to be at least two wayPoints
        if (wayPoints.length < 2) {
            throw new InvalidWaypointsException(wayPoints);
        }
        // Lines between consecutive wayPoints have to be either horizontal or vertical
        // (they are same in one or both coordinates)
        for (int i = 0; i < wayPoints.length - 1; i++) {
            if (wayPoints[i].getX() != wayPoints[i + 1].getX()
                    && wayPoints[i].getY() != wayPoints[i + 1].getY()) {
                throw new InvalidWaypointsException(this.wayPoints);
            }
        }

        this.wayPoints = wayPoints;

        //Set the valve p*100 % of the way along the way connecting the first two wayPoints
        double p = 0.15;
        double valveX = ((1 - p) * wayPoints[0].getX() + p * wayPoints[1].getX());
        double valveY = ((1 - p) * wayPoints[0].getY() + p * wayPoints[1].getY());

        valve = new ValveDrawer(new Point2D(valveX, valveY), pipe, rows);
    }

    @Override
    public final void draw(GraphicsContext context, double timeDiff) {
        context.setStroke(Color.BLACK);
        Canvas canvas = context.getCanvas();
        double totalWidth = canvas.getWidth();
        double totalHeight = canvas.getHeight();

        // length - 1 as we always draw from the current to the next point.
        for (int i = 0; i < wayPoints.length - 1; i++) {
            double rectWidth = ViewConstants.PIPE_WIDTH * totalWidth;
            context.setLineWidth(rectWidth);

            double x1 = wayPoints[i].getX() * totalWidth;
            double y1 = wayPoints[i].getY() * totalHeight;
            double x2 = wayPoints[i + 1].getX() * totalWidth;
            double y2 = wayPoints[i + 1].getY() * totalHeight;

            // First and last point get an offset to the y position as the thickness of the line makes
            // it go beyond the wanted destination. The offset is not applied in the constructor as it
            // depends on the screen dimensions.
            if (i == 0 && wayPoints.length == 2) {
                y1 += rectWidth / 2;
                y2 -= rectWidth / 2;
            } else if (i == 0) {
                y1 += rectWidth / 2;
            } else if (i == wayPoints.length - 2) {
                y2 -= rectWidth / 2;
            }
            context.strokeLine(x1, y1, x2, y2);
        }

        valve.draw(context, timeDiff);
    }
}
