package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import ac.up.malan.phd.problem.AbsoluteValue;
import ac.up.malan.phd.problem.Ackley;
import ac.up.malan.phd.problem.AlmostFlat;
import ac.up.malan.phd.problem.BorPoliProblem;
import ac.up.malan.phd.problem.Flat;
import ac.up.malan.phd.problem.Griewank;
import ac.up.malan.phd.problem.HalfSpherical;
import ac.up.malan.phd.problem.HoleInMountain;
import ac.up.malan.phd.problem.Niching1;
import ac.up.malan.phd.problem.Quadric;
import ac.up.malan.phd.problem.Quartic;
import ac.up.malan.phd.problem.Rana;
import ac.up.malan.phd.problem.Rastrigin;
import ac.up.malan.phd.problem.Rosenbrock;
import ac.up.malan.phd.problem.Salomon;
import ac.up.malan.phd.problem.Schwefel2_22;
import ac.up.malan.phd.problem.Schwefel2_26;
import ac.up.malan.phd.problem.SchwefelProblem2_22;
import ac.up.malan.phd.problem.Sine;
import ac.up.malan.phd.problem.Spherical;
import ac.up.malan.phd.problem.Step;
import ac.up.malan.phd.problem.Table;
import ac.up.malan.phd.problem.TableLegs;
import ac.up.malan.phd.problem.VassilevSampleProblem;

/**
 *
 * @author Abrie van Aardt
 */
public class Study_1D_Simple extends Study {

    public Study_1D_Simple() throws StudyConfigException {
        super();

        int dim = 1;

        //all 1D problems
        problems = new RealProblem[]{
            new AbsoluteValue(dim),
            //new Ackley(dim),
            new AlmostFlat(dim),
            new BorPoliProblem(),
            new Flat(),
            //new Griewank(dim),
            //new HalfSpherical(dim),
            new HoleInMountain(),
            new Niching1(),
            //new Quadric(dim),
            new Quartic(dim),
            //new Rana(dim),
            //new Rastrigin(dim),
            //new Rosenbrock(dim),
            //new Salomon(dim),
            //new Schwefel2_22(dim),
            //new Schwefel2_26(dim),                
            //new Sine(),
            //new Spherical(dim),
            new Step(dim),
            new Table(dim),
            new TableLegs(),
            new VassilevSampleProblem()
        };

    }

    protected RealProblem[] problems;

}
