package at.ac.tuwien.foop.mouserace.client.event;

public interface GameEventListener {
    void leftArrowPressed();
    void rightArrowPressed();

    void upArrowPressed();

    void downArrowPressed();

    void gameStopped();
}
