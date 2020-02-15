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

  // This test makes the assumption that the database stores the
  // todos in the same order that they appear in the json file.
  // But this assumption is probably justified--FullUserListFromDB makes it as
  // well.
  @ParameterizedTest
  @MethodSource("firstTodoInFullListParams")
  public void firstTodoInFullList(
      String dbFileName,
      String firstTodoOwner,
      boolean firstTodoStatus,
      String firstTodoBody,
      String firstTodoCategory) throws IOException {
    Database db = new Database(dbFileName);
    Todo[] allTodos = db.listTodos(new HashMap<>());
    Todo firstTodo = allTodos[0];
    assertEquals(firstTodoOwner, firstTodo.owner, "Incorrect owner");
    assertEquals(firstTodoStatus, firstTodo.status, "Incorrect status");
    assertEquals(firstTodoBody, firstTodo.body, "Incorrect body");
    assertEquals(firstTodoCategory, firstTodo.category, "Incorrect category");
  }

  public Stream<Arguments> firstTodoInFullListParams() {
    return Stream.of(
        Arguments.of(
            "/test-todos-1.json",
            "Kang",
            true,
            "Quae res in civitate duae plurimum possunt, eae contra nos ambae faciunt in hoc tempore, summa gratia et eloquentia; quarum alteram, C. Aquili, vereor, alteram metuo.",
            "sales"),
        Arguments.of(
            "/test-todos-3.json",
            "",
            false,
            "<script>alert('Problem!');</script>",
            "😃"));
  }
}
