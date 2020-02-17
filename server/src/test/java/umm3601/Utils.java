package umm3601;

import java.util.Comparator;
import java.util.Iterator;

/**
 * This class contains miscellaneous, uncategorized utility methods.
 */
public class Utils {

  /**
   * Returns true if the input iterable is sorted from smallest to largest.
   *
   * <p>
   *   Note that empty iterables are considered to be sorted for the purposes
   *   of this function.
   * </p>
   *
   * @param <T> The type of elements in the iterable.
   * @param iterable The iterable of non-null elements which we are checking
   *   for sortedness.
   * @param comparator The comparator used to define an ordering on
   *   {@code T}.
   * @return {@code true} if the iterable is sorted, and {@code false} if
   *   it is not.
   */
  public static <T> boolean isSorted(
      Iterable<T> iterable,
      Comparator<? super T> comparator) {
    Iterator<T> iterator = iterable.iterator();

    T previous;
    T current;

    if (!iterator.hasNext()) {
      return true;
    }

    current = iterator.next();
    while (iterator.hasNext()) {
      previous = current;
      current = iterator.next();

      if (comparator.compare(previous, current) > 0) {
        return false;
      }
    }
    return true;
  }
}
