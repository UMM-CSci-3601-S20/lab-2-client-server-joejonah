package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import umm3601.Utils;

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
import io.javalin.http.NotFoundResponse;;

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

  @ParameterizedTest
  @MethodSource("params")
  public void GET_to_request_category_smiley_face_todos(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("category", Arrays.asList(new String[] { "😃" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);

    // Call the method on the mock controller
    todoController.getTodos(ctx);

    // Confirm that `json` was called with all the todos.
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    for (Todo todo : argument.getValue()) {
      assertEquals("😃", todo.category);
    }
  }

  @ParameterizedTest
  @MethodSource("params")
  public void GET_to_request_status_incomplete_todos(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("status", Arrays.asList(new String[] { "incomplete" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);

    // Call the method on the mock controller
    todoController.getTodos(ctx);

    // Confirm that `json` was called with all the todos.
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    for (Todo todo : argument.getValue()) {
      assertEquals(false, todo.status);
    }
  }

  @ParameterizedTest
  @MethodSource("params")
  public void GET_with_illegal_status_throws_error(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("status", Arrays.asList(new String[] { "Should auld acquaintance be forgot and never brought to mind" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);

    Assertions.assertThrows(BadRequestResponse.class, () -> {
      todoController.getTodos(ctx);
    });
  }

  @ParameterizedTest
  @MethodSource("params")
  public void GET_to_request_limit_2_todos(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("limit", Arrays.asList(new String[] { "2" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);

    // Call the method on the mock controller
    todoController.getTodos(ctx);

    // Confirm that `json` was called with all the todos.
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    assertEquals(Math.min(db.size(), 2), argument.getValue().length);
  }

  @ParameterizedTest
  @MethodSource("params")
  public void GET_with_illegal_limit_throws_error(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("limit", Arrays.asList(new String[] { "I'm not an integer!" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);

    Assertions.assertThrows(BadRequestResponse.class, () -> {
      todoController.getTodos(ctx);
    });
  }

  @ParameterizedTest
  @MethodSource("params")
  public void GET_with_negative_limit_is_treated_as_zero(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("limit", Arrays.asList(new String[] { "-50" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    todoController.getTodos(ctx);

    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    assertEquals(0, argument.getValue().length);
  }

  @ParameterizedTest
  @MethodSource("params")
  public void GET_with_hexidecimal_limit_throws_error(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("limit", Arrays.asList(new String[] { "0xFF" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);

    Assertions.assertThrows(BadRequestResponse.class, () -> {
      todoController.getTodos(ctx);
    });
  }

  @Test
  public void GET_to_request_todo_with_existent_id() throws IOException {
    when(ctx.pathParam("id", String.class)).thenReturn(new Validator<String>("cd06139aeaab215245d0164f0d1aea61", ""));
    todoControllers[0].getTodo(ctx);
    verify(ctx).status(201);
  }

  @Test
  public void GET_to_request_todo_with_nonexistent_id() throws IOException {
    when(ctx.pathParam("id", String.class)).thenReturn(new Validator<String>("nonexistent", ""));
    Assertions.assertThrows(NotFoundResponse.class, () -> {
      todoControllers[0].getTodo(ctx);
    });
  }

  @ParameterizedTest
  @MethodSource("params")
  public void GET_to_request_todos_ordered_by_owner(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("orderBy", Arrays.asList(new String[] { "owner" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);

    // Call the method on the mock controller
    todoController.getTodos(ctx);

    // Confirm that `json` was called with all the todos.
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    Assertions.assertTrue(Utils.isSorted(
        Arrays.asList(argument.getValue()),
        Comparator.comparing(todo -> todo.owner)));
  }

  @ParameterizedTest
  @MethodSource("params")
  public void GET_to_request_todos_ordered_by_category(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("orderBy", Arrays.asList(new String[] { "category" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);

    // Call the method on the mock controller
    todoController.getTodos(ctx);

    // Confirm that `json` was called with all the todos.
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    Assertions.assertTrue(Utils.isSorted(
        Arrays.asList(argument.getValue()),
        Comparator.comparing(todo -> todo.category)));
  }

  @ParameterizedTest
  @MethodSource("params")
  public void GET_to_request_todos_ordered_by_body(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("orderBy", Arrays.asList(new String[] { "body" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);

    // Call the method on the mock controller
    todoController.getTodos(ctx);

    // Confirm that `json` was called with all the todos.
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    Assertions.assertTrue(Utils.isSorted(
        Arrays.asList(argument.getValue()),
        Comparator.comparing(todo -> todo.body)));
  }

  @ParameterizedTest
  @MethodSource("params")
  public void GET_to_request_todos_ordered_by_status(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("orderBy", Arrays.asList(new String[] { "status" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);

    // Call the method on the mock controller
    todoController.getTodos(ctx);

    // Confirm that `json` was called with all the todos.
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    Assertions.assertTrue(Utils.isSorted(
        Arrays.asList(argument.getValue()),
        Comparator.comparing(todo -> todo.status)));
  }

  @ParameterizedTest
  @MethodSource("params")
  public void GET_to_request_todos_ordered_by_invalid_field(
      TodoDatabase db,
      TodoController todoController) throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("orderBy", Arrays.asList(new String[] { "Abbot and Costello" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);

    Assertions.assertThrows(BadRequestResponse.class, () -> {
      todoController.getTodos(ctx);
    });
  }

  public static Stream<Arguments> params() {
    Arguments[] arguments = new Arguments[dbFileNames.length];
    for (int i = 0; i < dbFileNames.length; i++) {
      arguments[i] = Arguments.of(dbs[i], todoControllers[i]);
    }

    return Stream.of(arguments);
  }
}
