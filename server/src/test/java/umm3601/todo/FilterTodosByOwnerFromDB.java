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
public class FilterTodosByOwnerFromDB {

  @ParameterizedTest
  @MethodSource("namesAndCountsTodosParams")
  public void testFilterTodosByOwner(String dbFileName, String name, int count) throws IOException {
    TodoDatabase db = new TodoDatabase(dbFileName);
    Todo[] allTodos = db.listTodos(new HashMap<>());

    Todo[] nameTodos = db.filterTodosByOwner(allTodos, name);

    assertEquals(count, nameTodos.length, "Incorrect total number of name todos");
  }

  @ParameterizedTest
  @MethodSource("namesAndCountsTodosParams")
  public void testListTodosFilteredByOwner(String dbFileName, String name, int count) throws IOException {
    TodoDatabase db = new TodoDatabase(dbFileName);
    Map<String, List<String>> queryParams = new HashMap<>();

    queryParams.put("owner", Arrays.asList(new String[] { name }));
    Todo[] nameTodos = db.listTodos(queryParams);

    assertEquals(count, nameTodos.length, "Incorrect total number of name todos");
  }


  public static Stream<Arguments> namesAndCountsTodosParams() {
    return Stream.of(
        Arguments.of("/test-todos-1.json", "Kang", 1),
        Arguments.of("/test-todos-2.json", "Somebody who doesn't exist", 0),
        Arguments.of("/test-todos-3.json", "", 4));
  }
}
