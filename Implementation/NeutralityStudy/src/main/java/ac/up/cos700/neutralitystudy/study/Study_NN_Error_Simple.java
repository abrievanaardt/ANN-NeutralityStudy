package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Exp_ND_Simple;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;

/**
 *
 * @author Abrie van Aardt
 */
public class Study_NN_Error_Simple extends Study_NN_Error {

    public Study_NN_Error_Simple() throws StudyConfigException {
        super();
    }

    @Override
    public Study setup(NeutralityMeasure nm, double... otherParamters) {
        super.setup(nm);

        for (RealProblem problem : problems) {
            experiments.add(new Exp_ND_Simple(config, neutralityMeasure, problem));
        }

        return this;
    }

}
