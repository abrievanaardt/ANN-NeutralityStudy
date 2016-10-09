package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Exp_2D_Tunable_S;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import ac.up.malan.phd.problem.AbsoluteValue;
import ac.up.malan.phd.problem.Ackley;
import ac.up.malan.phd.problem.AlmostFlat;
import ac.up.malan.phd.problem.Beale;
import ac.up.malan.phd.problem.Bohachevsky1;
import ac.up.malan.phd.problem.Bohachevsky2;
import ac.up.malan.phd.problem.Bohachevsky3;
import ac.up.malan.phd.problem.Booth;
import ac.up.malan.phd.problem.CarromTable;
import ac.up.malan.phd.problem.Cup;
import ac.up.malan.phd.problem.Giunta;
import ac.up.malan.phd.problem.GoldsteinPrice;
import ac.up.malan.phd.problem.Griewank;
import ac.up.malan.phd.problem.HalfCup;
import ac.up.malan.phd.problem.HalfParabola;
import ac.up.malan.phd.problem.HalfSad;
import ac.up.malan.phd.problem.HalfSpherical;
import ac.up.malan.phd.problem.Parabola;
import ac.up.malan.phd.problem.Quadric;
import ac.up.malan.phd.problem.Quartic;
import ac.up.malan.phd.problem.Rana;
import ac.up.malan.phd.problem.Rastrigin;
import ac.up.malan.phd.problem.Rosenbrock;
import ac.up.malan.phd.problem.Sad;
import ac.up.malan.phd.problem.Salomon;
import ac.up.malan.phd.problem.Schwefel2_22;
import ac.up.malan.phd.problem.Schwefel2_26;
import ac.up.malan.phd.problem.SchwefelProblem2_22;
import ac.up.malan.phd.problem.ShekelsFoxholes;
import ac.up.malan.phd.problem.SixHumpCamelBack;
import ac.up.malan.phd.problem.Spherical;
import ac.up.malan.phd.problem.Step;
import ac.up.malan.phd.problem.Table;

/**
 *
 * @author Abrie van Aardt
 */
public class Study_2D_Tunable_S extends Study {

    public Study_2D_Tunable_S() throws StudyConfigException {
        super();

        int dim = 2;

        try {
            problems = new RealProblem[]{
                new AbsoluteValue(dim),
                new Ackley(dim),
                new AlmostFlat(dim),
                new Beale(),
                new Bohachevsky1(),
                new Bohachevsky2(),
                new Bohachevsky3(),
                new Booth(),
                new CarromTable(),
                new Cup(),
                new Giunta(),
                new GoldsteinPrice(),
                new Griewank(dim),
                new HalfCup(),
                new HalfParabola(),
                new HalfSad(),
                new HalfSpherical(dim),
                new Parabola(),
                new Quadric(dim),
                new Quartic(dim),
                new Rana(dim),
                new Rastrigin(dim),
                new Rosenbrock(dim),
                new Sad(),
                new Salomon(dim),
                new Schwefel2_22(dim),
                new Schwefel2_26(dim),
                new SchwefelProblem2_22(dim),
                new ShekelsFoxholes(),
                new SixHumpCamelBack(),
                new Spherical(dim),
                new Step(dim),
                new Table(dim)
            };

        }
        catch (UnequalArgsDimensionException e) {
            throw new StudyConfigException(e.getMessage());
        }

    }

    @Override
    public Study setup(NeutralityMeasure nm) {
        super.setup(nm);

        for (RealProblem problem : problems) {
            experiments.add(new Exp_2D_Tunable_S(config, neutralityMeasure, problem));
        }

        return this;
    }

}
