package edu.kit.pse.osip.simulation.view.main;

import edu.kit.pse.osip.core.utils.language.Translator;
import java.io.File;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class is the MenuBar at the top of the simulation view. It provides options to control the
 * simulation, change the settings and get help.
 *
 * @author Niko Wilhelm
 * @version 1.0
 */
public class SimulationMenu extends MenuBar {
    /**
     * Menu containing all buttons leading to ways to control the simulation.
     */
    private Menu menuButtonView;
    /**
     * Menu item for showing the control window.
     */
    private MenuItem menuItemControl;

    /**
     * Menu containing all buttons leading to ways to change the settings of the simulation.
     */
    private Menu menuButtonFile;
    /**
     * Menu item for showing the settings.
     */
    private MenuItem menuItemSettings;
    /**
     * Menu item for resetting the simulation.
     */
    private MenuItem menuItemReset;

    /**
     * Menu containing all buttons for help.
     */
    private Menu menuOther;
    /**
     * Button in the help menu for showing the help dialog.
     */
    private MenuItem menuItemHelp;
    /**
     * Button in the about menu for showing the about dialog.
     */
    private MenuItem menuItemAbout;

    /**
     * The scenario menu.
     */
    private Menu menuScenario;
    /**
     * Menu item for starting a scenario.
     */
    private MenuItem menuItemStartScenario;
    /**
     * Menu item for stopping the current running scenario.
     */
    private MenuItem menuItemStopScenario;

    /**
     * The stage this menu belongs to.
     */
    private Stage primaryStage;

    /**
     * Creates and initializes the menu for the simulation view.
     * 
     * @param primaryStage The window to which this SimulationMenu belongs.
     */
    protected SimulationMenu(Stage primaryStage) {
        this.setStyle("-fx-font-size:" + ViewConstants.FONT_SIZE + "px;");
        Translator trans = Translator.getInstance();

        this.primaryStage = primaryStage;

        menuButtonFile = new Menu(trans.getString("simulation.view.menu.file"));
        menuItemSettings = new MenuItem(trans.getString("simulation.view.menu.file.settings"));
        menuItemReset = new MenuItem(trans.getString("simulation.view.menu.file.reset"));
        menuButtonFile.getItems().addAll(menuItemSettings, menuItemReset);

        menuButtonView = new Menu(trans.getString("simulation.view.menu.view"));
        menuItemControl = new MenuItem(trans.getString("simulation.view.menu.view.control"));
        menuButtonView.getItems().add(menuItemControl);

        menuOther = new Menu(trans.getString("simulation.view.menu.other"));
        menuItemHelp = new MenuItem(trans.getString("simulation.view.menu.other.help"));
        menuItemAbout = new MenuItem(trans.getString("simulation.view.menu.other.about"));
        menuOther.getItems().addAll(menuItemHelp, menuItemAbout);

        menuScenario = new Menu(trans.getString("simulation.view.menu.scenario"));
        menuItemStartScenario = new MenuItem(trans.getString("simulation.view.menu.scenario.start"));
        menuItemStopScenario = new MenuItem(trans.getString("simulation.view.menu.scenario.stop"));
        menuItemStopScenario.setDisable(true);
        menuScenario.getItems().addAll(menuItemStartScenario, menuItemStopScenario);

        this.getMenus().addAll(menuButtonFile, menuButtonView, menuScenario, menuOther);
    }

    /**
     * Sets the handler for pressing the control entry in the menu.
     * 
     * @param controlButtonHandler The handler to execute.
     */
    public final void setControlButtonHandler(EventHandler<ActionEvent> controlButtonHandler) {
        menuItemControl.setOnAction(controlButtonHandler);
    }

    /**
     * Sets the handler for pressing the settings entry in the menu.
     * 
     * @param settingsButtonHandler The handler to be called when the settings button is pressed.
     */
    public final void setSettingsButtonHandler(EventHandler<ActionEvent> settingsButtonHandler) {
        menuItemSettings.setOnAction(settingsButtonHandler);
    }

    /**
     * Sets the handler for pressing the reset entry in the menu.
     * 
     * @param resetButtonHandler The handler to execute.
     */
    public final void setResetButtonHandler(EventHandler<ActionEvent> resetButtonHandler) {
        menuItemReset.setOnAction(resetButtonHandler);
    }

    /**
     * Sets the handler for pressing the about entry in the menu.
     * 
     * @param aboutButtonHandler The handler to be called when the about button is pressed.
     */
    public final void setAboutButtonHandler(EventHandler<ActionEvent> aboutButtonHandler) {
        menuItemAbout.setOnAction(aboutButtonHandler);
    }

    /**
     * Sets the handler for pressing the help entry in the menu.
     * 
     * @param helpButtonHandler The handler to be called when the help button is pressed.
     */
    public final void setHelpButtonHandler(EventHandler<ActionEvent> helpButtonHandler) {
        menuItemHelp.setOnAction(helpButtonHandler);
    }

    /**
     * Sets the listener to start a scenario.
     * 
     * @param listener The listener called if the user starts a scenario.
     *                 The parameter is the path to the scenario file.
     */
    public void setScenarioStartListener(Consumer<String> listener) {
        menuItemStartScenario.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                    Translator.getInstance().getString("simulation.view.scenariofilechooser.filter"),
                    "*.scenario.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle(Translator.getInstance().getString("simulation.view.scenariofilechooser"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                menuItemStopScenario.setDisable(false);
                menuItemStartScenario.setDisable(true);
                listener.accept(selectedFile.getAbsolutePath());
            }
        });
    }

    /**
     * Sets the handler called if the scenario gets stopped by the user.
     * 
     * @param listener The handler function.
     */
    public void setScenarioStopListener(Runnable listener) {
        menuItemStopScenario.setOnAction(actionEvent -> listener.run());
    }

    /**
     * Called if the scenario has finished (either stopped by the user, finished or if it has an error).
     */
    public void setScenarioFinished() {
        menuItemStartScenario.setDisable(false);
        menuItemStopScenario.setDisable(true);
    }
}
