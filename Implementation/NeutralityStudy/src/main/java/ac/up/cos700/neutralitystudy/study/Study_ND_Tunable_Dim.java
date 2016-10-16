package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Exp_ND_Tunable_Dim;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import ac.up.malan.phd.problem.AbsoluteValue;
import ac.up.malan.phd.problem.Ackley;
import ac.up.malan.phd.problem.AlmostFlat;
import ac.up.malan.phd.problem.Griewank;
import ac.up.malan.phd.problem.HalfSpherical;
import ac.up.malan.phd.problem.Quadric;
import ac.up.malan.phd.problem.Quartic;
import ac.up.malan.phd.problem.Rana;
import ac.up.malan.phd.problem.Rastrigin;
import ac.up.malan.phd.problem.Rosenbrock;
import ac.up.malan.phd.problem.Salomon;
import ac.up.malan.phd.problem.Schwefel2_22;
import ac.up.malan.phd.problem.Schwefel2_26;
import ac.up.malan.phd.problem.SchwefelProblem2_22;
import ac.up.malan.phd.problem.Spherical;
import ac.up.malan.phd.problem.Step;
import ac.up.malan.phd.problem.Table;

/**
 *
 * @author Abrie van Aardt
 */
public class Study_ND_Tunable_Dim extends Study {

    public Study_ND_Tunable_Dim() throws StudyConfigException {
        super();

        int dim = 1;

        try {
            problems = new RealProblem[]{
                new AbsoluteValue(dim),
                new Ackley(dim),
                new AlmostFlat(dim),                
                new Griewank(dim),              
                new HalfSpherical(dim),                
                new Quadric(dim),
                new Quartic(dim),
                new Rana(dim),
                new Rastrigin(dim),
                new Rosenbrock(dim),               
                new Salomon(dim),
                new Schwefel2_22(dim),
                new Schwefel2_26(dim),
                new SchwefelProblem2_22(dim),                
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
    public Study setup(NeutralityMeasure nm, double... otherParameters) {
        super.setup(nm);

        for (RealProblem problem : problems) {
            experiments.add(new Exp_ND_Tunable_Dim(config, neutralityMeasure, problem));
        }

        return this;
    }

}
