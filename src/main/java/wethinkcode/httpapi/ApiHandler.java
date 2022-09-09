package wethinkcode.httpapi;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.http.NotFoundResponse;
import org.eclipse.jetty.http.HttpStatus;

public class ApiHandler {
    private static final TasksDatabase database = new TasksDatabase();

    /**
     * Get all Tasks from the Database
     * Adds a 200 (OK) status regardless of the result
     *
     * @param context The Javalin Context for the HTTP GET Request
     */
    public static void getAllTasks(Context context) {
        context.status(HttpStatus.OK_200);
        context.json(database.all());
    }

    /**
     * Get one task from the Database using the ID of that task
     * Adds a 404 (NOT FOUND) status if the ID does not map to an existing task
     * Adds a 200 (OK) status if the ID was found
     *
     * @param context The Javalin Context for the HTTP GET Request
     */
    public static void getTaskFromID(Context context) {
        Integer id = context.pathParamAsClass("id", Integer.class).get();
        Task task = database.get(id);

        if (task == null) {
            context.status(HttpStatus.NOT_FOUND_404);
            throw new NotFoundResponse("HTTP not found: " + id);
        }

        context.status(HttpStatus.OK_200);
        context.json(task);
    }

    /**
     * Add a new task, using the body for the ID and description
     * Adds a 400 (BAD_REQUEST) status if a task for the given ID in the body already exists
     * Adds a 201 (CREATED) status if successfully added
     *
     * @param context The Javalin Context for the HTTP POST Request
     */
    public static void addTask(Context context) {
        Task task = context.bodyAsClass(Task.class);

        boolean isTaskAdded = database.add(task);

        if (!isTaskAdded) {
            context.status(HttpStatus.BAD_REQUEST_400);
            throw new BadRequestResponse("HTTP bad request: " + task);
        }

        context.header("Location", "/task/" + task.getId());
        context.status(HttpCode.CREATED);
    }
}
