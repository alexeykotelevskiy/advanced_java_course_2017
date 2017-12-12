package Solver;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class SolverTest {
    private static Solver solver;
    @BeforeClass
    public static void createSolver()
    {
        solver = new Solver();
    }
    @Test
    public void evaluate1() throws Exception {
        assertEquals(15, solver.evaluate("((2+3)  * 3)"), 0.0001);
    }

    @Test
    public void evaluate2() throws Exception {
        assertEquals(37.490771, solver.evaluate("11+23+3.33+(4.28-10)/3.11+1*2"), 0.0001);
    }

    @Test
    public void evaluate3() throws Exception {
        assertEquals(-3.051923, solver.evaluate("25.55 *  ( 3 / 2 / ( 8 + 5) ) - 6"), 0.0001);
    }


    @Test(expected = ParseExceprion.class)
    public void evaluate4() throws Exception {
           solver.evaluate("asdasd113asda");
    }

    @Test(expected = ParseExceprion.class)
    public void evaluate5() throws Exception {
        solver.evaluate("1+22*(3+2))");
    }

}