package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import io.javalin.core.validation.Validator;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import umm3601.Server;

/**
 * Tests the logic of the TodoController
 *
 * @throws IOException if the database can't be reached.
 */
public class TodoControllerSpec {

  private Context ctx = mock(Context.class);

  private static final String[] dbFileNames = {
    "/test-todos-1.json",
    "/test-todos-2.json",
    "/test-todos-3.json",
  };
  private static TodoDatabase[] dbs;
  private static TodoController[] todoControllers;

  static {
    dbs = Arrays.stream(dbFileNames)
        .map(fileName -> {
          try {
            return new TodoDatabase(fileName);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        })
        .toArray(TodoDatabase[]::new);

    todoControllers = Arrays.stream(dbs)
        .map(db -> new TodoController(db))
        .toArray(TodoController[]::new);
  }

  @BeforeEach
  public void setUp() throws IOException {
    ctx.clearCookieStore();
  }

  @ParameterizedTest
  @MethodSource("params")
  public void GET_to_request_all_todos(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    // Call the method on the mock controller
    todoController.getTodos(ctx);

    // Confirm that `json` was called with all the todos.
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    assertEquals(db.size(), argument.getValue().length);
  }

  @ParameterizedTest
  @MethodSource("params")
  public void GET_to_request_owner_Kodos_todos(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("owner", Arrays.asList(new String[] { "Kodos" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);

    // Call the method on the mock controller
    todoController.getTodos(ctx);

    // Confirm that `json` was called with all the todos.
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    for (Todo todo : argument.getValue()) {
      assertEquals("Kodos", todo.owner);
    }
  }


  public static Stream<Arguments> params() {
    Arguments[] arguments = new Arguments[dbFileNames.length];
    for (int i = 0; i < dbFileNames.length; i++) {
      arguments[i] = Arguments.of(dbs[i], todoControllers[i]);
    }

    return Stream.of(arguments);
  }
}
