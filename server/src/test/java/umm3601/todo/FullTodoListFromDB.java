package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests umm3601.user.Database listUser functionality
 */
public class FullTodoListFromDB {

  @ParameterizedTest
  @MethodSource("totalTodoCountParams")
  public void totalTodoCount(String dbFileName, int todosCount) {
    Database db = new Database(dbFileName);
    Todo[] allTodos = db.listTodos(new HashMap<>());
    assertEquals(todosCount, allTodos.length, "Incorrect total number of todos");
  }

  public Stream<Arguments> totalTodoCountParams() {
    return Stream.of(
        Arguments.of("/test-todos-1.json", 3),
        Arguments.of("/test-todos-2.json", 0),
        Arguments.of("/test-todos-3.json", 4));
  }
}
