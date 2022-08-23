package wethinkcode.httpapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.json.JsonMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;


/**
 * Exercise 1
 * <p>
 * Application Server for the Tasks API
 */
public class TasksAppServer {
    private static final TasksDatabase database = new TasksDatabase();

    private final Javalin appServer;

    /**
     * Create the application server and configure it.
     */
    public TasksAppServer() {
        this.appServer = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.jsonMapper(createGsonMapper());
        });

        this.appServer.get("/tasks", this::getAllTasks);
        this.appServer.get("/task/{id}", this::getOneTask);
        this.appServer.post("/task", this::addTask);
    }

    /**
     * Use GSON for serialisation instead of Jackson
     * because GSON allows for serialisation of objects without noargs constructors.
     *
     * @return A JsonMapper for Javalin
     */
    private static JsonMapper createGsonMapper() {
        Gson gson = new GsonBuilder().create();
        return new JsonMapper() {
            @NotNull
            @Override
            public String toJsonString(@NotNull Object obj) {
                return gson.toJson(obj);
            }

            @NotNull
            @Override
            public <T> T fromJsonString(@NotNull String json, @NotNull Class<T> targetClass) {
                return gson.fromJson(json, targetClass);
            }
        };
    }

    /**
     * Start the application server
     *
     * @param port the port for the app server
     */
    public void start(int port) {
        this.appServer.start(port);
    }

    /**
     * Stop the application server
     */
    public void stop() {
        this.appServer.stop();
    }

    /**
     * Get all tasks
     * Adds a 200 status
     *
     * @param context the server context
     */
    private void getAllTasks(Context context) {
        context.contentType("application/json");
        context.json(database.all());
    }

    /**
     * Get a task of a specific ID
     * Adds a 404 status if the ID does not map to an existing task
     * Adds a 200 status if the ID was found
     *
     * @param context the server context
     */
    private void getOneTask(Context context){
        Integer id = context.pathParamAsClass("id", Integer.class).get();
        Task task = database.get(id);

        if (task == null) {
            context.status(HttpStatus.NOT_FOUND_404);
            return;
        }

        context.json(task);
    }

    /**
     * Add a task, using the body for the ID and description
     * Adds a 400 status if a task for the given ID in the body already exists
     * Adds a 201 status if successfully added
     *
     * @param context the server context
     */
    private void addTask(Context context) {
        Task task = context.bodyAsClass(Task.class);

        if (database.add(task)) {
            context.status(201);
            context.header("Location", "/task/" + task.getId());
            return;
        }

        context.status(HttpStatus.BAD_REQUEST_400);
    }

}
