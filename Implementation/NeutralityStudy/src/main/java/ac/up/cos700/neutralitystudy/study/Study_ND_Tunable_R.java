package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Exp_ND_Tunable_Dim;
import ac.up.cos700.neutralitystudy.experiment.Exp_ND_Tunable_R;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abrie van Aardt
 */
public class Study_ND_Tunable_R extends Study {

    public Study_ND_Tunable_R() throws StudyConfigException {
        super();
    }

    @Override
    public Study setup(NeutralityMeasure nm, double... otherParameters) {
        super.setup(nm);

        try {
            experiments.add(new Exp_ND_Tunable_R(config, neutralityMeasure));
        }
        catch (UnequalArgsDimensionException ex) {
            Logger.getLogger(Study_ND_Tunable_R.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this;
    }

}
