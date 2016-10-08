package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Exp_1D_Tunable_Q;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure1;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;

/**
 *
 * @author Abrie van Aardt
 */
public class Study_M1_1D_Tunable_Q extends Study_1D_Tunable_Q {

    public Study_M1_1D_Tunable_Q() throws StudyConfigException {
        super();

        NeutralityMeasure neutralityMeasure = new NeutralityMeasure1(config.entries.get("epsilon"));
        
        for (RealProblem problem : problems) {
            experiments.add(new Exp_1D_Tunable_Q(config, neutralityMeasure, problem));
        }

    }

}
