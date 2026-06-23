package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RedundancyFilterTest {

    private final RedundancyFilter filter = new RedundancyFilter();

    @Test
    void eliminaPuntosDuplicadosConsecutivos() {
        Blueprint bp = new Blueprint("a", "b", List.of(
                new Point(1, 1), new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(3, 3)
        ));
        Blueprint result = filter.apply(bp);
        assertEquals(3, result.getPoints().size());
        assertEquals(new Point(1, 1), result.getPoints().get(0));
        assertEquals(new Point(2, 2), result.getPoints().get(1));
        assertEquals(new Point(3, 3), result.getPoints().get(2));
    }

    @Test
    void conservaDuplicadosNoCOnsecutivos() {
        Blueprint bp = new Blueprint("a", "b", List.of(
                new Point(1, 1), new Point(2, 2), new Point(1, 1)
        ));
        Blueprint result = filter.apply(bp);
        assertEquals(3, result.getPoints().size());
    }

    @Test
    void blueprintVacioRetornaVacio() {
        Blueprint bp = new Blueprint("a", "b", List.of());
        Blueprint result = filter.apply(bp);
        assertTrue(result.getPoints().isEmpty());
    }

    @Test
    void unSoloPuntoRetornaElMismo() {
        Blueprint bp = new Blueprint("a", "b", List.of(new Point(5, 5)));
        Blueprint result = filter.apply(bp);
        assertEquals(1, result.getPoints().size());
    }
}
