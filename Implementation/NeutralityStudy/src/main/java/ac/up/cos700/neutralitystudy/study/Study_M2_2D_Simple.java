package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Exp_2D_Simple;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure1;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure2;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;

/**
 *
 * @author Abrie van Aardt
 */
public class Study_M2_2D_Simple extends Study_2D_Simple {

    public Study_M2_2D_Simple() throws StudyConfigException {
        super();

        NeutralityMeasure neutralityMeasure = new NeutralityMeasure2(config.entries.get("epsilon"));

        for (RealProblem problem : problems) {
            experiments.add(new Exp_2D_Simple(config, neutralityMeasure, problem));
        }

    }

}
