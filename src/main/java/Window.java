import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Window {
    private final int width, height;
    private final String title;
    public final MouseListener mouseListener;
    public boolean stop = false;
    private long glfwWindow;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.mouseListener = new MouseListener();
        init();
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }
        configWindowHints();
        createWindow();
        setCallbacks();
    }

    private void configWindowHints() {
        // Configure GLFW
        glfwDefaultWindowHints();
        // this will make the window invisible until we are done setting it
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        // don't make thy window resizable
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        // do not maximize the window.
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);
        // assert some version stuff
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    }

    private void createWindow() {
        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        // Make the OpenGL context current, whatever that means.
        // a lot of stuff breaks if this isn't done
        glfwMakeContextCurrent(glfwWindow);
        GL.createCapabilities();

        // Enable v-sync
        // setting this to 1 will make the fps match the monitor's refresh rate
        // setting this to 0 will run this at maximum effort
        glfwSwapInterval(0);

        // Make the window visible
        glfwShowWindow(glfwWindow);
    }

    private void setCallbacks() {
        glfwSetCursorPosCallback(glfwWindow, mouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, mouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, mouseListener::mouseScrollCallback);
    }

    public void run() {
        //System.out.println("LWJGL Version " + Version.getVersion());
        loop();
        terminate();
    }

    public void stop() {
        stop = true;
    }

    private void loop() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        float fps = 0.5f;
        float perFrame = 1 / fps;
        float beginTime = (float) glfwGetTime();
        float endTime;
        float frameTime = 0;
        float dt = 0f;

        //glClearColor(0.1f, 0.09f, 0.1f, 1);
        glClearColor(0, 0, 0, 1);
        Button button = new Button(this, 0, 0, 500, 500, p -> System.out.println("click"));
        while (!glfwWindowShouldClose(glfwWindow) && !stop) {
            if (frameTime >= perFrame) {
                System.out.println("Interval");
                frameTime = 0;
                glClear(GL_COLOR_BUFFER_BIT);
                glfwSwapBuffers(glfwWindow);
            }
            glfwPollEvents();
            button.update();
            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            frameTime += dt;
            //System.out.println(MessageFormat.format("{0}ms, {1} FPS", dt, 1 / dt));
            beginTime = endTime;
        }
    }

    private void terminate() {
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}
