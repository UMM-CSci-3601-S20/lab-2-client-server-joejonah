package umm3601.todo;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import umm3601.Utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests umm3601.user.TodoDatabase listTodo functionality
 */
public class OrderTodosFromDB {

  @ParameterizedTest
  @MethodSource("orderTodosParams")
  public void testOrderTodosByOwner(String dbFileName) throws IOException {
    TodoDatabase db = new TodoDatabase(dbFileName);
    Todo[] allTodos = db.listTodos(new HashMap<>());

    Todo[] orderedTodos = db.orderTodos(allTodos, "owner");

    Assertions.assertTrue(Utils.isSorted(
        List.of(orderedTodos),
        Comparator.comparing(todo -> todo.owner)));
  }

  @ParameterizedTest
  @MethodSource("orderTodosParams")
  public void testOrderTodosByCategory(String dbFileName) throws IOException {
    TodoDatabase db = new TodoDatabase(dbFileName);
    Todo[] allTodos = db.listTodos(new HashMap<>());

    Todo[] orderedTodos = db.orderTodos(allTodos, "category");

    Assertions.assertTrue(Utils.isSorted(
        List.of(orderedTodos),
        Comparator.comparing(todo -> todo.category)));
  }

  @ParameterizedTest
  @MethodSource("orderTodosParams")
  public void testOrderTodosByBody(String dbFileName) throws IOException {
    TodoDatabase db = new TodoDatabase(dbFileName);
    Todo[] allTodos = db.listTodos(new HashMap<>());

    Todo[] orderedTodos = db.orderTodos(allTodos, "body");

    Assertions.assertTrue(Utils.isSorted(
        List.of(orderedTodos),
        Comparator.comparing(todo -> todo.body)));
  }

  @ParameterizedTest
  @MethodSource("orderTodosParams")
  public void testOrderTodosByStatus(String dbFileName) throws IOException {
    TodoDatabase db = new TodoDatabase(dbFileName);
    Todo[] allTodos = db.listTodos(new HashMap<>());

    Todo[] orderedTodos = db.orderTodos(allTodos, "status");

    Assertions.assertTrue(Utils.isSorted(
        List.of(orderedTodos),
        Comparator.comparing(todo -> todo.status)));
  }

  private static final String[] dbFileNames = {
    "/test-todos-1.json",
    "/test-todos-2.json",
    "/test-todos-3.json",
  };

  public static Stream<Arguments> orderTodosParams() {
    return Stream.of(dbFileNames).map(fileName -> Arguments.of(fileName));
  }
}
