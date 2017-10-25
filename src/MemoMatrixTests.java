import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoMatrixTests {
    @Test
    void canCreateUsingEmptyConstructor () {
        MemoMatrix<Object> memo = new MemoMatrix<>();
        assertEquals(0, memo.getNumCols());
        assertEquals(0, memo.getNumRows());
    }

    @Test
    void canCreateUsingParametrizedConstructor () {
        MemoMatrix<Object> memo = new MemoMatrix<>(1, 1);
        assertEquals(1, memo.getNumCols());
        assertEquals(1, memo.getNumRows());

        MemoMatrix<Object> memo2 = new MemoMatrix<>(1, 2);
        assertEquals(1, memo2.getNumCols());
        assertEquals(2, memo2.getNumRows());
    }

    @Test
    void canMemoizeAndRecall () {
        MemoMatrix<Object> memo = new MemoMatrix<>(1,1);
        assertEquals(1, memo.getNumCols());
        assertEquals(1, memo.getNumRows());

        assertFalse(memo.isMemoized(0, 0));
        assertFalse(memo.isMemoized(1, 1));
        assertNull(memo.recall(0, 0));

        Object test = new Object();
        memo.memoize(0, 0, test);

        assertTrue(memo.isMemoized(0,0));
        assertFalse(memo.isMemoized(1, 1));
        assertEquals(test, memo.recall(0, 0));

        Object test2 = new Object();
        assertNotEquals(test2, test);
        memo.memoize(1,1, test2);

        assertEquals(2, memo.getNumCols());
        assertEquals(2, memo.getNumRows());

        assertTrue(memo.isMemoized(0,0));
        assertTrue(memo.isMemoized(1, 1));
        assertEquals(test2, memo.recall(1, 1));
    }
}
