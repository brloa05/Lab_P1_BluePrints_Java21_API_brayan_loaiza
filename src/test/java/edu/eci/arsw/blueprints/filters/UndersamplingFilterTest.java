package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UndersamplingFilterTest {

    private final UndersamplingFilter filter = new UndersamplingFilter();

    @Test
    void conservaIndicesPares() {
        Blueprint bp = new Blueprint("a", "b", List.of(
                new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3)
        ));
        Blueprint result = filter.apply(bp);
        assertEquals(2, result.getPoints().size());
        assertEquals(new Point(0, 0), result.getPoints().get(0));
        assertEquals(new Point(2, 2), result.getPoints().get(1));
    }

    @Test
    void retornaOriginalConDosOмеnosPuntos() {
        Blueprint bp = new Blueprint("a", "b", List.of(
                new Point(0, 0), new Point(1, 1)
        ));
        Blueprint result = filter.apply(bp);
        assertEquals(2, result.getPoints().size());
    }

    @Test
    void retornaOriginalConUnPunto() {
        Blueprint bp = new Blueprint("a", "b", List.of(new Point(5, 5)));
        Blueprint result = filter.apply(bp);
        assertEquals(1, result.getPoints().size());
    }

    @Test
    void cincoPuntosRetornaTres() {
        Blueprint bp = new Blueprint("a", "b", List.of(
                new Point(0, 0), new Point(1, 1), new Point(2, 2),
                new Point(3, 3), new Point(4, 4)
        ));
        Blueprint result = filter.apply(bp);
        assertEquals(3, result.getPoints().size());
        assertEquals(new Point(0, 0), result.getPoints().get(0));
        assertEquals(new Point(2, 2), result.getPoints().get(1));
        assertEquals(new Point(4, 4), result.getPoints().get(2));
    }
}
