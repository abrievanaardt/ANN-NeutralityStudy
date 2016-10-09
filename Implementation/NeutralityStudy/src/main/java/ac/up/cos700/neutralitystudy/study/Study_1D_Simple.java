package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Exp_1D_Simple;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import ac.up.malan.phd.problem.AbsoluteValue;
import ac.up.malan.phd.problem.AlmostFlat;
import ac.up.malan.phd.problem.BorPoliProblem;
import ac.up.malan.phd.problem.Flat;
import ac.up.malan.phd.problem.HoleInMountain;
import ac.up.malan.phd.problem.Niching1;
import ac.up.malan.phd.problem.Quartic;
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

    @Override
    public Study setup(NeutralityMeasure nm) {
        super.setup(nm);
        
        for (RealProblem problem : problems) {
            experiments.add(new Exp_1D_Simple(config, neutralityMeasure, problem));
        }
        
        return this;
    }

}
