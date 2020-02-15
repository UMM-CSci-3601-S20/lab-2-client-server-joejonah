package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests umm3601.user.TodoDatabase listTodo functionality
 */
public class FilterTodosByStatusFromDB {

  @ParameterizedTest
  @MethodSource("completeAndIncompleteTodosParams")
  public void testFilterTodosByStatus(String dbFileName, int completeCount, int incompleteCount) throws IOException {
    TodoDatabase db = new TodoDatabase(dbFileName);
    Todo[] allTodos = db.listTodos(new HashMap<>());

    Todo[] completeTodos = db.filterTodosByStatus(allTodos, true);

    Todo[] incompleteTodos = db.filterTodosByStatus(allTodos, false);

    assertEquals(completeCount, completeTodos.length, "Incorrect total number of complete todos");
    assertEquals(incompleteCount, incompleteTodos.length, "Incorrect total number of incomplete todos");
  }

  @ParameterizedTest
  @MethodSource("completeAndIncompleteTodosParams")
  public void testListTodosFilteredByStatus(String dbFileName, int completeCount, int incompleteCount) throws IOException {
    TodoDatabase db = new TodoDatabase(dbFileName);
    Map<String, List<String>> queryParams = new HashMap<>();

    queryParams.put("status", Arrays.asList(new String[] { "complete" }));
    Todo[] completeTodos = db.listTodos(queryParams);

    queryParams.put("status", Arrays.asList(new String[] { "incomplete" }));
    Todo[] incompleteTodos = db.listTodos(queryParams);

    assertEquals(completeCount, completeTodos.length, "Incorrect total number of complete todos");
    assertEquals(incompleteCount, incompleteTodos.length, "Incorrect total number of incomplete todos");
  }


  public static Stream<Arguments> completeAndIncompleteTodosParams() {
    return Stream.of(
        Arguments.of("/test-todos-1.json", 2, 1),
        Arguments.of("/test-todos-2.json", 0, 0),
        Arguments.of("/test-todos-3.json", 0, 4));
  }
}
