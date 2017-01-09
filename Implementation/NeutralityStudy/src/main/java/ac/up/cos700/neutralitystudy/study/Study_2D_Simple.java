package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Exp_2D_Simple;
import ac.up.cos700.neutralitystudy.function.problem.CrossLegTable;
import ac.up.cos700.neutralitystudy.function.problem.Cube;
import ac.up.cos700.neutralitystudy.function.problem.Easom;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.function.problem.Schwefel21;
import ac.up.cos700.neutralitystudy.function.problem.Step2;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import ac.up.malan.phd.problem.AbsoluteValue;
import ac.up.malan.phd.problem.AlmostFlat;
import ac.up.malan.phd.problem.Beale;
import ac.up.malan.phd.problem.Booth;
import ac.up.malan.phd.problem.HalfCup;
import ac.up.malan.phd.problem.Quartic;
import ac.up.malan.phd.problem.ShekelsFoxholes;
import ac.up.malan.phd.problem.Step;
import ac.up.malan.phd.problem.Table;

/**
 *
 * @author Abrie van Aardt
 */
public class Study_2D_Simple extends Study {

    public Study_2D_Simple() throws StudyConfigException {
        super();

        int dim = 2;

        problems = new RealProblem[]{
            new AbsoluteValue(dim),
            //new Ackley(dim),
            new AlmostFlat(dim),
            //new Beale(),//looks flat initially, but really isn't
            //new Bohachevsky1(),
            //new Bohachevsky2(),
            //new Bohachevsky3(),
            //new Booth(),//also only appears neutral some places
            //new CarromTable(),
            //new Cup(),
            //new Giunta(),
            //new GoldsteinPrice(),
            //new Griewank(dim),
            //new HalfCup(),
            //new HalfParabola(),
            //new HalfSad(),
            //new HalfSpherical(dim),
            //new Parabola(),
            //new Quadric(dim),
            //new Quartic(dim),
            //new Rana(dim),
            //new Rastrigin(dim),
            //new Rosenbrock(dim),
            //new Sad(),
            //new Salomon(dim),
            //new Schwefel2_22(dim),
            //new Schwefel2_26(dim),
            //new ShekelsFoxholes(),
            //new SixHumpCamelBack(),
            //new Spherical(dim),
            new Schwefel21(dim),
            new Easom(),
            new Step(dim),
            new Step2(dim),
            new Table(dim),            
        };

    }

    @Override
    public Study setup(NeutralityMeasure nm, double... otherParameters) {
        super.setup(nm);

        for (RealProblem problem : problems) {
            experiments.add(new Exp_2D_Simple(config, neutralityMeasure, problem));
        }

        return this;
    }

}
