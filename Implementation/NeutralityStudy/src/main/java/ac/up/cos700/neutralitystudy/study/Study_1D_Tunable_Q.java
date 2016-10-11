package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Exp_1D_Tunable_Q;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
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
import ac.up.malan.phd.problem.Sine;
import ac.up.malan.phd.problem.Spherical;
import ac.up.malan.phd.problem.Step;
import ac.up.malan.phd.problem.Table;
import ac.up.malan.phd.problem.TableLegs;
import ac.up.malan.phd.problem.VassilevSampleProblem;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abrie van Aardt
 */
public class Study_1D_Tunable_Q extends Study {

    public Study_1D_Tunable_Q() throws StudyConfigException {
        super();
        //todo: refactor into common class _Tunable
        try {
            int dim = 1;

            problems = new RealProblem[]{
                new AbsoluteValue(dim),
                new Ackley(dim),
                new AlmostFlat(dim),
                new BorPoliProblem(),
                new Flat(),
                new Griewank(dim),
                new HalfSpherical(dim),
                new HoleInMountain(),
                new Niching1(),
                new Quadric(dim),
                new Quartic(dim),
                new Rana(dim),
                new Rastrigin(dim),
                new Rosenbrock(dim),
                new Salomon(dim),
                new Schwefel2_22(dim),
                new Schwefel2_26(dim),
                new Sine(),
                new Spherical(dim),
                new Step(dim),
                new Table(dim),
                new TableLegs(),
                new VassilevSampleProblem()
            };
        }
        catch (UnequalArgsDimensionException ex) {
            Logger.getLogger(Study_1D_Tunable_Q.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Study setup(NeutralityMeasure nm, double... otherParameters) {
        super.setup(nm);

        for (RealProblem problem : problems) {
            experiments.add(new Exp_1D_Tunable_Q(config, neutralityMeasure, problem));
        }

        return this;
    }

}
