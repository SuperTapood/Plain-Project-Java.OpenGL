import java.util.function.Consumer;

public class Button {
    private int x, y, w, h;
    private Consumer<Void> f;
    private Window window;

    public Button(Window window, int xPos, int yPos, int width, int height, Consumer<Void> resp) {
        x = xPos;
        y = yPos;
        w = width;
        h = height;
        f = resp;
        this.window = window;
    }

    public boolean doesHover() {
        float mouseX = window.mouseListener.getX();
        float mouseY = window.mouseListener.getY();
        return x + w > mouseX && mouseX > x &&
                y + h > mouseY && mouseY > y;
    }

    public boolean doesClick() {
        return window.mouseListener.mouseButtonDown(0);
    }

    public boolean doesPressButton() {
        return doesHover() && doesClick();
    }

    public void update() {
        if (doesPressButton()) {
            f.accept(null);
        }
    }
}
