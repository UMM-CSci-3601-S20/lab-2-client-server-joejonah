package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests umm3601.user.TodoDatabase listTodo functionality
 */
public class LimitTodosFromDB {

  @ParameterizedTest
  @MethodSource("limitsAndCountsTodosParams")
  public void testFilterTodosWithLimit(String dbFileName, int limit, int count) throws IOException {
    TodoDatabase db = new TodoDatabase(dbFileName);
    Todo[] allTodos = db.listTodos(new HashMap<>());

    Todo[] limitedTodos = db.filterTodosWithLimit(allTodos, limit);

    assertEquals(count, limitedTodos.length, "Incorrect total number of limited todos");
  }

  @ParameterizedTest
  @MethodSource("limitsAndCountsTodosParams")
  public void testListTodosWithLimit(String dbFileName, int limit, int count) throws IOException {
    TodoDatabase db = new TodoDatabase(dbFileName);
    Map<String, List<String>> queryParams = new HashMap<>();

    queryParams.put("category", Arrays.asList(new String[] { limit }));
    Todo[] categoryTodos = db.listTodos(queryParams);

    assertEquals(count, categoryTodos.length, "Incorrect total number of limited todos");
  }


  public static Stream<Arguments> categoriesAndCountsTodosParams() {
    return Stream.of(
        Arguments.of("/test-todos-1.json", 2, 2),
        Arguments.of("/test-todos-2.json", 6, 0),
        Arguments.of("/test-todos-3.json", 0, 0));
  }
}
